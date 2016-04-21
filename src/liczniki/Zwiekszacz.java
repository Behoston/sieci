package liczniki;

import java.util.concurrent.locks.Lock;

public class Zwiekszacz extends Thread {

    private Licznik licznik;
    private Lock lock;

    public Zwiekszacz(Licznik licznik) {
        this.licznik = licznik;
    }

    @Override
    public void run() {
        System.out.println("Zwiększacz uruchomiony!");
        while (true) {
            int wartosc = licznik.zwieksz();
            System.out.println("Zwiększacz: " + wartosc);
            if (wartosc > 1000) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
