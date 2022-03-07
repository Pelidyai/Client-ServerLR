package Main;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;

public class FileActions {
    static String readStr(FileReader f) {
        StringBuilder buf = new StringBuilder();
        try {
            char symbol;
            do {
                symbol = (char) f.read();
                if (symbol == '\n') break;
                buf.append(symbol);
            } while (true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buf.toString();
    }

    static int readInt(FileReader f) {
        return Integer.parseInt(readStr(f), 10);
    }

    static Boolean readBool(FileReader f) {
        String val = readStr(f);
        return val.equals("true");
    }

    static Color readColor(FileReader f) {
        String val = readStr(f);
        int red;
        int green;
        int blue;
        StringBuilder buf = new StringBuilder();
        int i = 0;
        for (; ';' != val.charAt(i); i++) {
            buf.append(val.charAt(i));
        }
        i++;
        red = Integer.parseInt(buf.toString());
        buf = new StringBuilder();
        for (; ';' != val.charAt(i); i++) {
            buf.append(val.charAt(i));
        }
        i++;
        green = Integer.parseInt(buf.toString());
        buf = new StringBuilder();
        for (; i < val.length(); i++) {
            buf.append(val.charAt(i));
        }
        blue = Integer.parseInt(buf.toString());
        return new Color(red, green, blue);
    }
}
