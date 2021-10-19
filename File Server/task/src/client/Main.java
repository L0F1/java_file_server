package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 23456;

    public static void main(String[] args) {

        var sc = new Scanner(System.in);

        try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                var in = new DataInputStream(socket.getInputStream());
                var out  = new DataOutputStream(socket.getOutputStream())
        ) {
            System.out.println("Client started!");
            System.out.println("Enter action (1 - get a file, 2 - create a file, 3 - delete a file):");
            String choice = sc.nextLine();

            if (choice.strip().equals("exit")) {
                out.writeUTF("EXIT");
                System.out.println("The request was sent.");
            }
            else if (isNumeric(choice)){

                System.out.println("Enter filename:");
                String fileName = sc.nextLine();

                switch (choice) {
                    case "1": {
                        out.writeUTF("GET " + fileName);
                        System.out.println("The request was sent.");
                        String response = in.readUTF();
                        int statusCode = Integer.parseInt(response.substring(0, 3));
                        if (statusCode == 200) {
                            System.out.println("The content of the file is:" + response.substring(3));
                        } else {
                            System.out.println("The response says that the file was not found!");
                        }
                        break;
                    }
                    case "2": {
                        System.out.println("Enter file content:");
                        String content = sc.nextLine();
                        out.writeUTF("PUT " + fileName + " " + content);
                        System.out.println("The request was sent.");
                        String response = in.readUTF();
                        int statusCode = Integer.parseInt(response);
                        if (statusCode == 200) {
                            System.out.println("The response says that the file was created!");
                        } else {
                            System.out.println("The response says that creating the file was forbidden!");
                        }
                        break;
                    }
                    case "3": {
                        out.writeUTF("DELETE " + fileName);
                        System.out.println("The request was sent.");
                        String response = in.readUTF();
                        int statusCode = Integer.parseInt(response);
                        if (statusCode == 200) {
                            System.out.println("The response says that the file was successfully deleted!");
                        } else {
                            System.out.println("The response says that the file was not found!");
                        }
                        break;
                    }
                }
            }
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isNumeric(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
