package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Session extends Thread{
    private final Socket socket;

    public Session(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            var input = new DataInputStream(socket.getInputStream());
            var output = new PrintWriter(socket.getOutputStream());

            String msg = input.readUTF();
            System.out.println("Received: " + msg);
            output.print("All files were sent!");
            System.out.println("Sent: All files were sent!");
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
