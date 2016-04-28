package liczniki;


class Zmniejszacz extends Thread {

    private Licznik licznik;

    Zmniejszacz(Licznik licznik) {
        this.licznik = licznik;
    }

    @Override
    public void run() {
        System.out.println("Zmnejszacz uruchomiony!");
        while (true) {
            int wartosc = licznik.zmniejsz();
            System.out.println("Zmniejszacz: " + wartosc);
            if (wartosc < -1000) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
