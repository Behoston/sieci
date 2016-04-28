package tcp;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ClientIdSource {
    private Integer id = 0;

    private Lock lock = new ReentrantLock();

    Integer giveMeId() {
        lock.lock();
        Integer id = this.id;
        this.id++;
        lock.unlock();
        return id;
    }
}
