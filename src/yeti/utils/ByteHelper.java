package yeti.utils;

import java.nio.ByteBuffer;
import java.util.Locale;

public class ByteHelper {

    public static byte[] longToBytes(long l) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(l);
        return buffer.array();
    }

    public static Long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getLong();
    }

    public static byte[] intToBytes(int i) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putLong(i);
        return buffer.array();
    }

    public static Integer bytesToInt(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getInt();
    }


    public static byte[] shortToBytes(short s) {
        ByteBuffer buffer = ByteBuffer.allocate(Short.BYTES);
        buffer.putShort(s);
        return buffer.array();
    }

    public static Short bytesToShort(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Short.BYTES);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getShort();
    }

    public static byte[] byteToBytes(byte b) {
        ByteBuffer buffer = ByteBuffer.allocate(Byte.BYTES);
        buffer.put(b);
        return buffer.array();
    }

    public static Byte bytesToByte(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Byte.BYTES);
        buffer.put(bytes);
        buffer.flip();
        return buffer.get();
    }

    public static byte[] concat(byte[]... bytes) {
        int length = 0;
        for (byte[] b : bytes) {
            length += b.length;
        }
        byte[] c = new byte[length];
        int last_len = 0;
        for (byte[] b : bytes) {
            System.arraycopy(b, 0, c, last_len, b.length);
        }
        return c;
    }
}
