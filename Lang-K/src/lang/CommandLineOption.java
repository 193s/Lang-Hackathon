package lang;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;

public class CommandLineOption {
    private File file;

    public CommandLineOption(String[] args)
            throws InputMismatchException,
            FileNotFoundException {

        for (String s : args) {
            if (s.startsWith("-")) {
            }
            else file = readFile(s);
        }
        if (file == null) throw new InputMismatchException();
    }

    public String read() throws IOException {
        FileReader reader = new FileReader(file);
        String s = "";
        int c;
        try {
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

    private static File readFile(String s)
            throws FileNotFoundException {
        File file = new File(s);
        if (!file.exists()) throw new FileNotFoundException();
        return file;
    }
}
