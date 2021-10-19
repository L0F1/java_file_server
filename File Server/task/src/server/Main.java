package server;

import java.io.*;
import java.net.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {

    private static final int SERVER_PORT = 23456;
    private static final Path STORAGE_ROOT = Path.of(System.getProperty("user.dir"),
            "src", "server", "data");


    public static void main(String[] args) {
        /*ClientDebug client = new ClientDebug();
        client.start();*/
        startServer();
    }

    private static void startServer() {

        try (ServerSocket server = new ServerSocket(SERVER_PORT)) {

            if (!Files.exists(STORAGE_ROOT))
                Files.createDirectory(STORAGE_ROOT);

            System.out.println("Server started!");
            boolean isServerRun = true;

            while (isServerRun) {

                try (Socket socket = server.accept();
                     var in = new DataInputStream(socket.getInputStream());
                     var out = new DataOutputStream(socket.getOutputStream()))
                {

                    String request = in.readUTF();

                    if (request.equals("EXIT")) {
                        isServerRun = false;
                    }
                    else handleRequest(request, out);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleRequest(String request, DataOutputStream out) throws IOException {
        String method = request.split(" ")[0];
        String fileName = request.split(" ")[1];
        Path filePath = Path.of(STORAGE_ROOT.toString(), fileName);

        switch (method) {
            case "GET": {
                if (Files.exists(filePath)) {
                    var sc = new Scanner(filePath);
                    var content = new StringBuilder();
                    while (sc.hasNext()) {
                        content.append(sc.next());
                    }
                    out.writeUTF("200 " + content.toString());
                }
                else {
                    out.writeUTF("404");
                }
                break;
            }
            case "PUT": {
                try {
                    Path file = Files.createFile(filePath);
                    var pw = new PrintWriter(file.toFile());
                    String content = request.substring(method.length() + fileName.length() + 2);
                    pw.print(content);
                    pw.flush();
                    out.writeUTF("200");
                }
                catch (FileAlreadyExistsException e) {
                    out.writeUTF("403");
                }
                break;
            }
            case "DELETE": {
                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                    out.writeUTF("200");
                }
                else {
                    out.writeUTF("404");
                }
                break;
            }
        }
        out.flush();
    }
}