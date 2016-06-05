package yeti.server;

import yeti.InvalidDataException;
import yeti.algo.Algorithm;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Yeti extends Thread {
    private Queue<Algorithm> queue;
    private Algorithm actual;
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public void calculate(Algorithm algorithm) {
        queue = new ConcurrentLinkedQueue<>();
        lock.writeLock().lock();
        queue.add(algorithm);
        lock.writeLock().unlock();
        queue.notify();
    }

    public void cancel(short id, String ip) {
        lock.writeLock().lock();
        if (ip.equals(actual.getIp()) && actual.getId() == id) {
            lock.writeLock().unlock();
            actual.interrupt();
        } else {
            Queue<Algorithm> newQueue = new ConcurrentLinkedQueue<>();
            for (Algorithm a : queue) {
                if (a.getIp().equals(ip) && a.getId() == id) {
                    a.interrupt();
                } else {
                    newQueue.add(a);
                }
            }
            this.queue = newQueue;
            lock.writeLock().unlock();
        }
    }

    public int getPosition(int id, String ip) {
        int position = 1;
        lock.readLock().lock();
        if (ip.equals(actual.getIp()) && actual.getId() == id) {
            lock.readLock().unlock();
            return position;
        }
        for (Algorithm a : queue) {
            position++;
            if (ip.equals(actual.getIp()) && a.getId() == id) {
                lock.readLock().unlock();
                return position;
            }
        }
        lock.readLock().unlock();
        return -1;
    }

    @Override
    public void run() {
        while (true) {
            try {
                lock.writeLock().lock();
                actual = queue.poll();
                lock.writeLock().unlock();
                if (actual == null) {
                    queue.wait();
                } else {
                    actual.run();
                }
            } catch (InvalidDataException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
