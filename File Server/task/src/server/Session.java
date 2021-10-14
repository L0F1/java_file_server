package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Session extends Thread{
    private final Socket socket;

    public Session(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            String msg = input.readUTF();
            System.out.println("Received: " + msg);
            output.writeUTF("All files were sent!");
            System.out.println("Sent: All files were sent!");
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
