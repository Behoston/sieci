package yeti.client;

import javafx.util.Pair;
import yeti.NotSupportedException;
import yeti.algo.AlgorithmContext;
import yeti.algo.IsPrime;
import yeti.algo.Knapsack;
import yeti.algo.Sum;
import yeti.algo.results.IsPrimeResultData;
import yeti.algo.results.KnapsackResultData;
import yeti.algo.results.SumResultData;
import yeti.messages.Calculate;
import yeti.messages.Cancel;
import yeti.messages.PositionQuestion;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Client extends Thread {
    private AlgorithmContext context;
    private Integrator integrator;
    private List<String> ips;
    private List<ServerConnection> servers;
    private Short id = 0;
    private Integer lastServer;
    private final static short SUM = 1;
    private final static short KNAPSACK = 2;
    private final static short IS_PRIME = 3;


    public Client(List<String> ips) {
        this.ips = ips;
        this.context = new AlgorithmContext();
        this.integrator = new Integrator();
        this.lastServer = 0;
        this.servers = new ArrayList<>();
        for (String ip : ips) {
            ServerConnection server = new ServerConnection(ip, this.context, this.integrator);
            this.servers.add(server);
            server.start();
        }
    }

    public void run() {
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        String request;
        while (true) {
            try {
                request = inFromUser.readLine();
                parseRequest(request);
            } catch (IOException e) {
                return;
            }
        }
    }

    private void parseRequest(String request) {
        String[] requestParts = request.split(" ");
        if ("run".equals(requestParts[0])) {
            String file = requestParts[1];
            Integer partsForServer = Integer.parseInt(requestParts[2]);
            calculate(file, partsForServer);
        } else if ("cancel".equals(requestParts[0])) {
            Short id = Short.parseShort(requestParts[1]);
            cancel(id);
        } else if ("status".equals(requestParts[0])) {
            Short id = Short.parseShort(requestParts[1]);
            status(id);
        } else if ("quit".equals(requestParts[0])) {
            quit();
        } else {
            System.out.println("Message unreadable, try again");
        }
    }

    private Short getNextId() {
        id++;
        return id;
    }

    private ServerConnection getNextServer() {
        lastServer++;
        if (lastServer == servers.size()) {
            lastServer = 0;
        }
        return servers.get(lastServer);
    }

    private void calculate(String file, Integer partsForServer) {
        String algorithm_line;
        Short id = getNextId();
        System.out.println("Calculation id: " + id);
        Integer packets = servers.size() * partsForServer;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            algorithm_line = reader.readLine();
            if (algorithm_line.toUpperCase().startsWith("SUM")) {
                parseSum(id, packets, reader);
            } else if (algorithm_line.toUpperCase().startsWith("ISPRIME")) {
                parseIsPrime(partsForServer, id, reader);
            } else if (algorithm_line.toUpperCase().startsWith("KNAPSACK")) {
                parseKnapsack(id, packets, reader);

            } else {
                throw new NotSupportedException();
            }
        } catch (IOException | NotSupportedException e) {
            e.printStackTrace();
        }
    }

    private void parseKnapsack(Short id, Integer packets, BufferedReader reader) throws IOException {
        context.addAlgorithm(id, KNAPSACK);
        Integer backpackCapacity = Integer.parseInt(reader.readLine());
        Integer dataLines = Integer.parseInt(reader.readLine());

        long configurations = (long) Math.pow(2, dataLines);
        System.out.println("Configurations: " + configurations);
        long step = configurations / packets;
        long rest = configurations % packets;


        List<Pair<Integer, Integer>> list = new ArrayList<>();
        for (int i = 0; i != dataLines; i++) {
            String data_line = reader.readLine();
            if (data_line == null) {
                break;
            }
            String[] pair = data_line.split(" ");
            Integer capacity = Integer.parseInt(pair[0]);
            Integer value = Integer.parseInt(pair[1]);
            list.add(new Pair<>(capacity, value));
        }
        integrator.setupFinish(packets, id, new KnapsackResultData(0L));
        long start = 0;
        long end;
        for (int packageId = 0; packageId != packets; packageId++) {
            end = start + step;
            // jeśli nie da się równo podzielić to kilka pierwszych pakietów zawiera o 1 konfigurację więcej

            if (rest != 0) {
                end++;
                rest--;
            }
            if (end > configurations - 1) {
                end = configurations - 1;
            }
            System.out.println("Start: " + start);
            System.out.println("End: " + end);
            Knapsack knapsack = new Knapsack(id, packageId, null, list, backpackCapacity, start, end, null);
            Calculate calculate = new Calculate(knapsack);
            ServerConnection server = getNextServer();
            server.sendCommunicate(calculate);
            start = end + 1;
        }
    }

    private void parseIsPrime(Integer partsForServer, Short id, BufferedReader reader) throws IOException {
        context.addAlgorithm(id, IS_PRIME);
        reader.readLine(); // zawsze równe 1 bo mamy jedną linię do zczytania z pliku dalej
        Integer number = Integer.parseInt(reader.readLine());
        Integer parts = partsForServer * servers.size();
        Integer end = (int) Math.ceil(Math.sqrt(number));
        Integer range = (end - 2) / parts;
        Integer rest = (end - 2) % parts;
        integrator.setupFinish(partsForServer, id, new IsPrimeResultData(0));
        Integer range_start = 2;
        Integer range_end;
        for (int packageId = 0; packageId != partsForServer; packageId++) {
            range_end = range_start + range;
            // Kiedy ilość danych jest niepodzielna równo to dodaje 1 do zakresu dla pierwszych x paczek
            if (rest > 0 && range != 0) {
                range_end++;
                rest--;
            }
            if (range_end > end) {
                range_end = end;
            }
            Calculate calculate = new Calculate(new IsPrime(id, packageId, range_start, range_end, number, null, null));
            ServerConnection server = getNextServer();
            server.sendCommunicate(calculate);
            range_start = range_end + 1;
        }
    }

    private void parseSum(Short id, Integer packets, BufferedReader reader) throws IOException {
        context.addAlgorithm(id, SUM);
        Integer dataLines = Integer.parseInt(reader.readLine());
        Integer linesPerPacket = dataLines / packets;
        Integer rest = dataLines % packets;
        integrator.setupFinish(packets, id, new SumResultData(0L));
        for (int packageId = 0; packageId != packets; packageId++) {
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i != linesPerPacket; i++) {
                String data_line = reader.readLine();
                if (data_line == null) {
                    break;
                }
                int number = Integer.parseInt(data_line);
                list.add(number);
            }
            // kiedy danych nie da się podzielić po równo, do pierwszych x pakietów
            // dodaję o jedno miejsce więcej
            if (rest > 0) {
                rest--;
                String data_line = reader.readLine();
                if (data_line == null) {
                    break;
                }
                int number = Integer.parseInt(data_line);
                list.add(number);
            }
            Calculate calculate = new Calculate(new Sum(id, packageId, list, null, null));
            ServerConnection server = getNextServer();
            server.sendCommunicate(calculate);
        }
    }

    private void cancel(Short id) {
        for (ServerConnection server : servers) {
            server.sendCommunicate(new Cancel(id));
        }
    }

    private void status(Short id) {
        integrator.setupStatus(servers.size(), id);
        for (ServerConnection server : servers) {
            server.sendCommunicate(new PositionQuestion(id));
        }
    }

    private void quit() {
        context.getAllIds().forEach(this::cancel);
        servers.forEach(ServerConnection::quit);
    }


}
