package marg.util;

import java.io.File;
import java.io.IOException;

public class MyFileReader {
    public static String read(File file) throws IOException {
        java.io.FileReader reader = new java.io.FileReader(file);
        String s = new String();
        try {
            int c;
            while ((c = reader.read()) != -1) {
                s += (char) c;
            }
        }
        catch (IOException e) {
            throw e;
        }
        finally {
            reader.close();
        }
        return s;
    }
}
