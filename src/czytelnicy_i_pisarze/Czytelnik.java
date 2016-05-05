package czytelnicy_i_pisarze;

import java.util.Random;

class Czytelnik extends Thread {

    private Czytelnia czytelnia;
    private Random random = new Random();
    private Integer id;


    Czytelnik(Czytelnia czytelnia, int id) {
        this.id = id;
        this.czytelnia = czytelnia;
    }

    @Override
    public void run() {
        while (true) {
            czytelnia.wejdz(this);
            czytaj();
            czytelnia.wyjdz(this);
        }
    }

    private void czytaj() {
        try {
            Thread.sleep(random.nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    Integer getIdent() {
        return id;
    }
}
