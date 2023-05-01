package br.com.dv.hacker;

public enum DictionaryFile {
    LOGINS("/logins.txt");

    final String filePath;

    DictionaryFile(String filePath) {
        this.filePath = filePath;
    }
}
