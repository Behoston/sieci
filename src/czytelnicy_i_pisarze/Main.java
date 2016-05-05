package czytelnicy_i_pisarze;

public class Main {


    public static void main(String argv[]) {
        int czytelnicy = 100;
        int pisarze = 10;
        Czytelnia czytelnia = new Czytelnia();
        for (int i = 0; i < pisarze; i++) {
            new Pisarz(czytelnia, i).start();
        }
        for (int i = 0; i < czytelnicy; i++) {
            new Czytelnik(czytelnia, i).start();
        }

    }
}
