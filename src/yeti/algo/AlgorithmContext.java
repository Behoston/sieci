package yeti.algo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * This class should be used only in one client context due to id is unique only for client not global
 */
public class AlgorithmContext {
    private ReadWriteLock lock;

    private class AlgorithmIdentifier {
        private Short id;
        private Byte algorithmId;


        AlgorithmIdentifier(Short id, Byte algorithmId) {
            this.id = id;
            this.algorithmId = algorithmId;
        }

        Short getId() {
            return id;
        }

        Byte getAlgorithmId() {
            return algorithmId;
        }
    }

    private List<AlgorithmIdentifier> identifiers;

    public AlgorithmContext() {
        identifiers = new ArrayList<>();
        this.lock = new ReentrantReadWriteLock();
    }

    public void addAlgorithm(Short id, Byte algorithmId) {
        AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(id, algorithmId);
        this.lock.writeLock().lock();
        identifiers.add(algorithmIdentifier);
        this.lock.writeLock().unlock();
    }

    public Byte identify(Short id) {
        Byte algorithmId = null;
        lock.readLock().lock();
        for (AlgorithmIdentifier a : identifiers) {
            if (a.getId().equals(id)) {
                algorithmId = a.getAlgorithmId();
            }
        }
        lock.readLock().unlock();
        return algorithmId;
    }


}
