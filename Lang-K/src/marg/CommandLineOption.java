package marg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;

import static marg.CommandLineOptions.*;

public class CommandLineOption {
    private File file;
    public CommandLineOptions type;

    public CommandLineOption(String[] args)
            throws InputMismatchException,
            FileNotFoundException {

        for (String s : args) {
            if (s.startsWith("-")) {
                String str = s.substring(1);
                switch (str) {
                    case "v":
                        type = Version;
                        break;
                }
            }
            else {
                type = Run;
                file = readFile(s);
            }
        }
        if (type == null) throw new InputMismatchException();
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
