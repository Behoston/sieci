package yeti.komunikaty;


public interface Komunikat {

    short getId();

    byte getType();

    byte[] toByteArray();

}
