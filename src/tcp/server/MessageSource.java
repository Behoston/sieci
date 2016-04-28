package tcp.server;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class MessageSource {
    private Set<Message> messages;
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    MessageSource() {
        messages = new HashSet<>();
    }

    Boolean isMessageForMe(Integer me) {
        lock.readLock().lock();
        for (Message m : messages) {
            if (m.getTo().equals(me)) {
                lock.readLock().unlock();
                return Boolean.TRUE;
            }
        }
        lock.readLock().unlock();
        return Boolean.FALSE;
    }

    Message getMessage(Integer me) {
        lock.writeLock().lock();
        for (Message m : messages) {
            if (m.getTo().equals(me)) {
                messages.remove(m);
                lock.writeLock().unlock();
                return m;
            }
        }
        return null;
    }

    void putMessage(Message message) {
        lock.writeLock().lock();
        messages.add(message);
        lock.writeLock().unlock();
    }

}
