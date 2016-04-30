package yeti;

public interface AlgorithmResult {

    byte[] toByteArray();

    AlgorithmResult parseBytes(byte[] bytes);

}
