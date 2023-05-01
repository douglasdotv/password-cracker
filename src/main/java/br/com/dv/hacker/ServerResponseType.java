package br.com.dv.hacker;

public enum ServerResponseType {
    WRONG_LOGIN("Wrong login!"),
    WRONG_PASSWORD("Wrong password!"),
    BAD_REQUEST("Bad request!"),
    EXCEPTION("Exception happened during login"),
    CONNECTION_SUCCESS("Connection success!");

    final String result;

    ServerResponseType(String result) {
        this.result = result;
    }
}
