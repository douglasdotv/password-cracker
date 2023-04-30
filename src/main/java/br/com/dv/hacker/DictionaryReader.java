package br.com.dv.hacker;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DictionaryReader {

    public List<String> readDictionary(String filePath) {
        List<String> passwords = new ArrayList<>();

        try (InputStream is = getClass().getResourceAsStream(filePath)) {
            if (is == null) {
                System.out.println("File not found: " + filePath);
                return passwords;
            }
            try (InputStreamReader isr = new InputStreamReader(is);
                 BufferedReader reader = new BufferedReader(isr)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    passwords.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading dict file: " + e.getMessage());
            e.printStackTrace();
        }

        return passwords;
    }

}
