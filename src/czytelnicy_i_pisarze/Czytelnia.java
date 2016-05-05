package czytelnicy_i_pisarze;


class Czytelnia {
    private final Object monitor = new Object();
    private Integer czytelnicy = 0;
    private Boolean pisarz = false;

    synchronized void wejdz(Czytelnik czytelnik) {
        synchronized (monitor) {
            if (pisarz) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Czytelnik " + czytelnik.getIdent().toString() + " IN");
            czytelnicy++;
        }

    }

    void wyjdz(Czytelnik czytelnik) {
        synchronized (monitor) {
            System.out.println("Czytelnik " + czytelnik.getIdent().toString() + " OUT");
            czytelnicy--;
            if (czytelnicy == 0) {
                monitor.notify();
            }
        }

    }


    synchronized void wejdz(Pisarz pisarz) {
        synchronized (monitor) {
            if (czytelnicy != 0 || this.pisarz.equals(Boolean.TRUE)) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Pisarz " + pisarz.getIdent().toString() + " IN");
            this.pisarz = true;
        }
    }

    void wyjdz(Pisarz pisarz) {
        synchronized (monitor) {
            System.out.println("Pisarz " + pisarz.getIdent().toString() + " OUT");
            this.pisarz = false;
            monitor.notifyAll();
        }
    }


}
