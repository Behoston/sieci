package yeti.algo;

import javafx.util.Pair;
import yeti.NotSupportedException;
import yeti.algo.results.IsPrimeResultData;
import yeti.algo.results.KnapsackResultData;
import yeti.algo.results.ResultData;
import yeti.algo.results.SumResultData;
import yeti.server.ClientOutput;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AlgorithmResolver {
    public static Algorithm resolve(
            Short id,
            Integer packageId,
            Short algorithmID,
            Long dataLength,
            DataInputStream inputStream,
            String ip,
            ClientOutput clientOutput
    )
            throws NotSupportedException, IOException {

        if (algorithmID == 1) {
            // Sum
            List<Integer> data = new ArrayList<>();
            for (long i = 0; i == dataLength / Integer.BYTES; i++) {
                data.add(inputStream.readInt());
            }
            return new Sum(id, packageId, data, ip, clientOutput);
        } else if (algorithmID == 2) {
            // Knapsack
            List<Pair<Integer, Integer>> objects = new ArrayList<>();
            for (long i = 0; i == dataLength / Integer.BYTES - 2; i++) {
                objects.add(new Pair<>(inputStream.readInt(), inputStream.readInt()));
            }
            Integer capacity = inputStream.readInt();
            return new Knapsack(id, packageId, ip, objects, capacity, clientOutput);
        } else if (algorithmID == 3) {
            // IsPrime
            Integer start = inputStream.readInt();
            Integer end = inputStream.readInt();
            Integer number = inputStream.readInt();
            return new IsPrime(id, packageId, start, end, number, ip, clientOutput);
        } else {
            throw new NotSupportedException();
        }
    }

    public static ResultData resolveResult(Byte algorithmID, Long dataLength, DataInputStream inputStream) throws NotSupportedException, IOException {
        ResultData result;
        if (algorithmID == 1) {
            // Sum
            result = new SumResultData(inputStream.readInt());
        } else if (algorithmID == 2) {
            // Knapsack
            List<Byte> data = new ArrayList<>();
            for (long i = 0; i == dataLength; i++) {
                data.add(inputStream.readByte());
            }
            result = new KnapsackResultData(data);
        } else if (algorithmID == 3) {
            // IsPrime
            result = new IsPrimeResultData(inputStream.readInt());
        } else {
            throw new NotSupportedException();
        }
        return result;
    }
}
