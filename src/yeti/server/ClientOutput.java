package yeti.server;

import yeti.messages.*;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientOutput {
    private DataOutputStream dataOutputStream;
    private String ip;

    public ClientOutput(Socket socket) {
        ip = socket.getInetAddress().getHostAddress();
        try {
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendCommunicate(Communicate communicate) {
        try {
            communicate.writeToDataOutputStream(dataOutputStream);
        } catch (IOException e) {
            return;
        }
    }

    public void sendResult(Result result) {
        System.out.println("Sending result to: " + ip +
                " for calculation: " + result.getId() +
                " package: " + result.getPackageId());
        sendCommunicate(result);
    }

    public void sendPosition(PositionAnswer positionAnswer) {
        System.out.println("Sending position to: " + ip + " for calculation: " + positionAnswer.getId());
        sendCommunicate(positionAnswer);
    }

    public void sendCancelled(Cancelled cancelled) {
        System.out.println("Cancelled " + ip + " 's calculation: " + cancelled.getId());
        sendCommunicate(cancelled);
    }

    public void sendOverload(Overload overload) {
        System.out.println("User " + ip + " overloaded his limit sending calculation:" + overload.getId() +
                " package: " + overload.getPackageId());
        sendCommunicate(overload);
    }

    public void sendError(yeti.messages.Error error) {
        System.out.println("User " + ip + " data error for calculation: " + error.getId() +
                " package: " + error.getPackageId());
        sendCommunicate(error);
    }


}
