package br.com.dv.hacker;

public final class InputValidator {

    private InputValidator() {
    }

    public static InputData validateInputs(String[] args) {
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

    private static int parsePort(String portString) {
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

}
