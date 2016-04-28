package liczniki;


public class Uruchom {
    public static void main(String[] args) {
        Licznik licznik = new Licznik();
        Zmniejszacz zmniejszacz = new Zmniejszacz(licznik);
        Zwiekszacz zwiekszacz1 = new Zwiekszacz(licznik);
        Zwiekszacz zwiekszacz2 = new Zwiekszacz(licznik);
        zmniejszacz.start();
        zwiekszacz1.start();
        zwiekszacz2.start();
    }
}
