package liczniki;


public class Uruchom {
    public static void main(String[] args) {
        Licznik licznik = new Licznik();
        Zmniejszacz zmniejszacz = new Zmniejszacz(licznik);
        Zwiekszacz zwiekszacz = new Zwiekszacz(licznik);
        zmniejszacz.start();
        zwiekszacz.start();
    }
}
