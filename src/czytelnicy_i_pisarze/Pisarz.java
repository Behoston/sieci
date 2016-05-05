package czytelnicy_i_pisarze;

import java.util.Random;

class Pisarz extends Thread {

    private Czytelnia czytelnia;
    private Random random = new Random();
    private Integer id;

    Pisarz(Czytelnia czytelnia, int id) {
        this.czytelnia = czytelnia;
        this.id = id;
    }

    @Override
    public void run() {
        while (true) {
            czytelnia.wejdz(this);
            pisz();
            czytelnia.wyjdz(this);
        }
    }

    private void pisz() {
        try {
            System.out.println("[" + id.toString() + "] Piszę");
            Thread.sleep(random.nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    Integer getIdent() {
        return id;
    }
}
