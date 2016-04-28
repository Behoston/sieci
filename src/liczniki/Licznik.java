package liczniki;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Licznik {
    private int i;
    private Lock lock = new ReentrantLock();

    int zwieksz() {
        lock.lock();
        i++;
        lock.unlock();
        return i;
    }

    int zmniejsz() {
        lock.lock();
        i--;
        lock.unlock();
        return i;
    }
}
