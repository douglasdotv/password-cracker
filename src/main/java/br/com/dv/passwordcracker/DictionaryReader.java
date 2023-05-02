package br.com.dv.passwordcracker;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class DictionaryReader {

    private DictionaryReader() {
    }

    public static List<String> readDictionary(String filePath) {
        List<String> lines = new ArrayList<>();

        try (InputStream is = DictionaryReader.class.getResourceAsStream(filePath)) {
            if (is == null) {
                System.out.println("File not found: " + filePath);
                return lines;
            }

            try (InputStreamReader isr = new InputStreamReader(is);
                 BufferedReader reader = new BufferedReader(isr)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading file: " + e.getMessage());
            e.printStackTrace();
        }

        return lines;
    }

}
