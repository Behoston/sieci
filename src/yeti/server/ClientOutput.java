package yeti.server;

import yeti.messages.Cancelled;
import yeti.messages.Overload;
import yeti.messages.PositionAnswer;
import yeti.messages.Result;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientOutput extends Thread {
    private DataOutputStream dataOutputStream;

    public ClientOutput(Socket socket) {
        try {
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendResult(Result result) throws IOException {
        result.writeToDataOutputStream(dataOutputStream);
    }

    public void sendPosition(PositionAnswer positionAnswer) throws IOException {
        positionAnswer.writeToDataOutputStream(dataOutputStream);
    }

    public void sendCancelled(Cancelled cancelled) throws IOException {
        cancelled.writeToDataOutputStream(dataOutputStream);
    }

    public void sendOverload(Overload overload) throws IOException {
        overload.writeToDataOutputStream(dataOutputStream);
    }

    public void sendError(yeti.messages.Error error) throws IOException {
        error.writeToDataOutputStream(dataOutputStream);
    }
}
