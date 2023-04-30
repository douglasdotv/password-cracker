package br.com.dv.hacker;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class PasswordHacker {

    private final DictionaryReader dictionaryReader = new DictionaryReader();
    private static final String FILE_PATH = "/passwords.txt";

    public void run(String[] args) {
        InputData inputData = validateInputs(args);
        if (inputData == null) {
            return;
        }

        List<String> passwords = dictionaryReader.readDictionary(FILE_PATH);
        String password = bruteForcePassword(inputData.ipAddress(), inputData.port(), passwords);
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

    private List<String> generateCombinations(String password) {
        List<String> combinations = new ArrayList<>();
        int numOfCombinations = 1 << password.length();

        for (int i = 0; i < numOfCombinations; i++) {
            char[] combination = password.toCharArray();
            for (int j = 0; j < password.length(); j++) {
                if (((i >> j) & 1) == 1) {
                    combination[j] = Character.toUpperCase(combination[j]);
                }
            }
            combinations.add(new String(combination));
        }

        return combinations;
    }

    private String bruteForcePassword(String ipAddress, int port, List<String> dictionary) {
        String currentPassword = "";
        String response;

        try (Socket socket = new Socket(ipAddress, port);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

            outerLoop:
            for (String password : dictionary) {
                List<String> combinations = generateCombinations(password);
                for (String combination : combinations) {
                    response = getResponse(input, output, combination);
                    if (response.contains("Connection success!")) {
                        currentPassword = combination;
                        break outerLoop;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred while connecting to the server: " + e.getMessage());
            e.printStackTrace();
        }

        return currentPassword;
    }

    private String getResponse(DataInputStream input, DataOutputStream output, String combination)
            throws IOException {
        output.writeUTF(combination);
        output.flush();
        return input.readUTF();
    }

}
