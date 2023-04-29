package br.com.dv.hacker;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class PasswordHacker {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Invalid number of arguments.");
            return;
        }

        String ipAddress = args[0];
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

        String password = bruteForcePassword(ipAddress, port);
        System.out.println(password);
    }

    private static String bruteForcePassword(String ipAddress, int port) {
        String currentPassword = "";
        String response = "";

        try (Socket socket = new Socket(ipAddress, port);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
            while (!response.contains("Connection success!")) {
                currentPassword = generateNextPassword(currentPassword);
                output.writeUTF(currentPassword);
                output.flush();
                response = input.readUTF();
            }
        } catch (Exception e) {
            System.out.println("An error occurred while connecting to the server: " + e.getMessage());
            e.printStackTrace();
        }

        return currentPassword;
    }

    private static String generateNextPassword(String currentPassword) {
        StringBuilder newPassword = new StringBuilder(currentPassword);
        int index = newPassword.length() - 1;

        while (index >= 0) {
            char currentChar = newPassword.charAt(index);
            if (currentChar == 'z') {
                newPassword.setCharAt(index, '0');
                break;
            } else if (currentChar == '9') {
                newPassword.setCharAt(index, 'a');
                index--;
            } else {
                newPassword.setCharAt(index, (char) (currentChar + 1));
                break;
            }
        }

        if (index < 0) {
            newPassword.insert(0, 'a');
        }

        return newPassword.toString();
    }
}
