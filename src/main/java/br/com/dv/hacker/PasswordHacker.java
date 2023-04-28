package br.com.dv.hacker;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class PasswordHacker {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Invalid number of arguments.");
            return;
        }

        String host = args[0];
        int port;
        try {
            port = Integer.parseInt(args[1]);
            if (port < 0 || port > 65535) {
                System.out.println("Invalid port number.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid port number.");
            return;
        }
        String message = args[2];

        try (Socket socket = new Socket(host, port);
             DataOutputStream output = new DataOutputStream(socket.getOutputStream());
             DataInputStream input = new DataInputStream(socket.getInputStream())
        ) {
            output.writeUTF(message);
            String response = input.readUTF();
            System.out.println(response);
        } catch (Exception e) {
            System.out.println("An error occurred while connecting to the server: " + e.getMessage());
        }
    }
}
