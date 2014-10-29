package marg.util;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class MyFileReader {
    public static String read(File file) throws IOException {
        String s = "";
        try (java.io.FileReader reader = new java.io.FileReader(file)) {
            int c;
            while ((c = reader.read()) != -1) {
                s += (char) c;
            }
        } catch (IOException e) {
            throw e;
        }
        return s;
    }
}
