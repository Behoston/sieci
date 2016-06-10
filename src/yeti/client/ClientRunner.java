package yeti.client;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ClientRunner {
    public static void main(String[] argv) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(argv[0])));
        Integer numberOfServers = Integer.parseInt(reader.readLine());
        List<String> ips = new ArrayList<>(numberOfServers);
        for (int i = 0; numberOfServers != i; i++) {
            ips.add(reader.readLine());
        }
        new Client(ips).start();
    }


}
