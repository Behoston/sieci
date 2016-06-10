package yeti.server;

import yeti.OverloadException;
import yeti.algo.Algorithm;
import yeti.messages.Cancelled;
import yeti.messages.PositionAnswer;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Yeti extends Thread {
    private Queue<Algorithm> queue;
    private Algorithm actual;
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Integer limit;

    public Yeti(Integer power) {
        this.limit = power;
        queue = new ConcurrentLinkedQueue<>();
    }

    public void calculate(Algorithm algorithm) throws OverloadException {
        lock.readLock().lock();
        Integer packages = 0;
        for (Algorithm a : queue) {
            if (a.getIp().equals(algorithm.getIp()) && a.getId() == algorithm.getId()) {
                packages++;
            }
        }
        if (actual != null && actual.getIp().equals(algorithm.getIp()) && actual.getId() == algorithm.getId()) {
            packages++;
        }
        lock.readLock().unlock();
        if (packages > limit) {
            throw new OverloadException();
        }
        lock.writeLock().lock();
        queue.add(algorithm);
        lock.writeLock().unlock();
    }

    public Cancelled cancel(short id, String ip) {
        lock.writeLock().lock();
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
        if (actual != null && ip.equals(actual.getIp()) && actual.getId() == id) {
            actual.interrupt();
            return null;
        } else{
            return new Cancelled(id);
        }

    }

    public PositionAnswer getPosition(short id, String ip) {
        int position = 0;
        int length = 0;
        int tmpPosition = 0;
        lock.readLock().lock();
        if (actual != null && ip.equals(actual.getIp()) && actual.getId() == id) {
            position = 0;
            length = 1;
        }
        for (Algorithm a : queue) {
            tmpPosition++;
            if (ip.equals(a.getIp()) && a.getId() == id) {
                length += 1;
                position = tmpPosition;
            }
        }
        lock.readLock().unlock();
        return new PositionAnswer(id, length, position);
    }

    @Override
    public void run() {
        while (true) {
            try {
                actual = null;
                lock.writeLock().lock();
                if (!queue.isEmpty()) {
                    actual = queue.poll();
                }
                lock.writeLock().unlock();
                if (actual == null) {
                    Thread.sleep(1000);
                } else {
                    actual.run();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
