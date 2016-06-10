package yeti.client;

import javafx.util.Pair;
import yeti.NotSupportedException;
import yeti.algo.AlgorithmContext;
import yeti.algo.IsPrime;
import yeti.algo.Knapsack;
import yeti.algo.Sum;
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
        this.lastServer = 0;
        this.servers = new ArrayList<>();
        for (String ip : ips) {
            ServerConnection server = new ServerConnection(ip, this.context);
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
                e.printStackTrace();
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
            if (algorithm_line.startsWith("SUM")) {
                parseSum(id, packets, reader);
            } else if (algorithm_line.startsWith("ISPRIME")) {
                parseIsPrime(partsForServer, id, reader);
            } else if (algorithm_line.startsWith("KNAPSACK")) {
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
        Integer backpack = Integer.parseInt(reader.readLine());
        Integer dataLines = Integer.parseInt(reader.readLine());

        Integer linesPerPacket = dataLines / packets;
        if (dataLines % packets != 0) {
            // kiedy ilość linii jest niepodzielna dodaje jeden pakiet
            // zaiwerający resztkę danych
            packets++;
        }
        for (int packageId = 0; packageId != packets; packageId++) {
            List<Pair<Integer, Integer>> list = new ArrayList<>();
            for (int i = 0; i != linesPerPacket; i++) {
                String data_line = reader.readLine();
                if (data_line == null) {
                    break;
                }
                String[] pair = data_line.split(" ");
                Integer capacity = Integer.parseInt(pair[0]);
                Integer value = Integer.parseInt(pair[1]);
                list.add(new Pair<>(capacity, value));
            }
            Calculate calculate = new Calculate(new Knapsack(id, packageId, null, list, backpack, null));
            ServerConnection server = getNextServer();
            server.sendCommunicate(calculate);
        }
    }

    private void parseIsPrime(Integer partsForServer, Short id, BufferedReader reader) throws IOException {
        // FIXME: 10.06.16 poprawić dzielenie na mniejsze pakiety
        context.addAlgorithm(id, IS_PRIME);
        Integer number = Integer.parseInt(reader.readLine());
        Integer range = number / partsForServer;
        if (number % partsForServer != 0) {
            // praktycznie zawsze, bo jeśli nie, to znaczy że nie jest liczbą pierwszą...
            partsForServer++;
        }
        Integer range_start = 0;
        Integer range_end = range;
        for (int packageId = 0; packageId != partsForServer; packageId++) {
            Calculate calculate = new Calculate(new IsPrime(id, packageId, range_start, range_end, number, null, null));
            ServerConnection server = getNextServer();
            server.sendCommunicate(calculate);
            range_start = range_end;
            range_end += range;
            if (range_end > number) {
                range_end = number;
            }
        }
    }

    private void parseSum(Short id, Integer packets, BufferedReader reader) throws IOException {
        context.addAlgorithm(id, SUM);
        Integer dataLines = Integer.parseInt(reader.readLine());
        Integer linesPerPacket = dataLines / packets;
        if (dataLines % packets != 0) {
            // kiedy ilość linii jest niepodzielna dodaje jeden pakiet
            // zaiwerający resztkę danych
            packets++;
        }
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
        for (ServerConnection server : servers) {
            server.sendCommunicate(new PositionQuestion(id));
        }
    }

    private void quit() {
        context.getAllIds().forEach(this::cancel);
    }


}
