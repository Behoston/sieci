package tcp.server;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class MessageRouter {
    private Set<ClientConnection> clients;
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    MessageRouter() {
        clients = new HashSet<>();
    }


    void connectClient(ClientConnection client) {
        lock.writeLock().lock();
        clients.add(client);
        lock.writeLock().unlock();
    }

    void routeMessage(String message, String sender) {
        if (message == null) {
            return;
        }
        if (message.toUpperCase().contains("/END")) {
            sendFor(sender, "/END", sender);
        }
        if (message.startsWith("@")) {
            String[] msg = message.split("@", 3);
            sendFor(msg[1], msg[2], sender);
        } else {
            sendForAll(message, sender);
        }

    }

    private void sendForAll(String message, String sender) {
        lock.readLock().lock();
        for (ClientConnection client : clients) {
            client.sendToMyClient(message, sender, false);
        }
        lock.readLock().unlock();
    }

    private void sendFor(String ip, String message, String sender) {
        lock.readLock().lock();
        for (ClientConnection client : clients) {
            if (client.getIp().equals(ip)) {
                client.sendToMyClient(message, sender, true);
            }
        }
        lock.readLock().unlock();
    }
}
