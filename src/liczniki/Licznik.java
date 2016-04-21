package liczniki;


public class Licznik {
    private int i;

    public synchronized int zwieksz() {
        i++;
        return i;
    }

    public synchronized int zmniejsz() {
        i--;
        return i;
    }
}
