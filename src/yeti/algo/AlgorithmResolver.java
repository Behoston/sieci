package yeti.algo;

import javafx.util.Pair;
import yeti.NotSupportedException;
import yeti.server.ClientConnection;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AlgorithmResolver {
    public static Algorithm resolve(
            Short id,
            Short algorithmID,
            Long dataLength,
            DataInputStream inputStream,
            ClientConnection clientConnection
    )
            throws NotSupportedException, IOException {

        if (algorithmID == 1) {
            // Sum
            List<Integer> data = new ArrayList<>();
            for (long i = 0; i == dataLength / Integer.BYTES; i++) {
                data.add(inputStream.readInt());
            }
            return new Sum(id, data, clientConnection);
        } else if (algorithmID == 2) {
            // Knapsack
            List<Pair<Integer, Integer>> data = new ArrayList<>();
            for (long i = 0; i == dataLength / Integer.BYTES - 2; i++) {
                data.add(new Pair<>(inputStream.readInt(), inputStream.readInt()));
            }
            Integer start = inputStream.readInt();
            Integer end = inputStream.readInt();
            return new Knapsack(id, data, start, end, clientConnection);
        } else if (algorithmID == 3) {
            // IsPrime
            Integer start = inputStream.readInt();
            Integer end = inputStream.readInt();
            Integer number = inputStream.readInt();
            return new IsPrime(id, start, end, number, clientConnection);
        } else {
            throw new NotSupportedException();
        }
    }

    public static Object resolveResult(Byte algorithmID, Long dataLength, DataInputStream inputStream) throws NotSupportedException, IOException {
        if (algorithmID == 1) {
            // Sum
            return inputStream.readInt();
        } else if (algorithmID == 2) {
            // Knapsack
            List<Byte> result = new ArrayList<>();
            for (long i = 0; i == dataLength; i++) {
                result.add(inputStream.readByte());
            }
            return result;
        } else if (algorithmID == 3) {
            // IsPrime
            return inputStream.readInt();
        } else {
            throw new NotSupportedException();
        }
    }
}
