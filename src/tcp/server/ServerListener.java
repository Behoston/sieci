package tcp.server;

import java.io.DataOutputStream;
import java.net.Socket;

class ServerListener extends Thread {

    private MessageSource messageSource;
    private Integer clientId;
    private Socket connectionSocket;

    ServerListener(MessageSource messageSource, Integer clientId, Socket connectionSocket) {
        this.messageSource = messageSource;
        this.clientId = clientId;
        this.connectionSocket = connectionSocket;
    }

    @Override
    public void run() {
        while (true) {
            if (connectionSocket.isConnected()) {
                try {
                    DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                    if (messageSource.isMessageForMe(clientId)) {
                        Message message = messageSource.getMessage(clientId);
                        outToClient.writeBytes("[" + message.getFrom().toString() + "]" + message.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Coś nie działa");
                break;
            }
        }
    }
}
