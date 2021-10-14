package server;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;
import java.util.ArrayList;

public class Main {

    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 23456;

    public static void main(String[] args) {
        startServer();
        menu();
    }

    private static void startServer() {

        try (ServerSocket server = new ServerSocket(SERVER_PORT)) {

            System.out.println("Server started!");
            Session session = new Session(server.accept());
            session.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void menu() {
        Scanner sc = new Scanner(System.in);
        ArrayList<String> allowedFileNames = new ArrayList<>();
        ArrayList<String> fileStorage = new ArrayList<>();
        String line;

        for (int i = 1; i <= 10; i++) {
            allowedFileNames.add("file" + i);
        }

        do {
            line = sc.nextLine();

            if(line.startsWith("add")) {
                String file = line.substring(4);
                if(allowedFileNames.contains(file) && !fileStorage.contains(file)) {
                    System.out.println(
                            String.format("The file %s added successfully",
                                    file));
                    fileStorage.add(file);
                }
                else {
                    System.out.println(String.format("Cannot add the file %s",
                            file));
                }
            }
            else if(line.startsWith("get")) {
                String file = line.substring(4);
                if(fileStorage.contains(file)) {
                    System.out.println(
                            String.format("The file %s was sent",
                                    file));
                }
                else {
                    System.out.println(String.format("The file %s not found",
                            file));
                }
            }
            else if(line.startsWith("delete")) {
                String file = line.substring(7);
                if(fileStorage.contains(file)) {
                    System.out.println(
                            String.format("The file %s was deleted",
                                    file));
                    fileStorage.remove(file);
                }
                else {
                    System.out.println(String.format("The file %s not found",
                            file));
                }
            }

        } while (!line.equals("exit"));
    }
}