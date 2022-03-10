package Main;

import java.io.*;
import java.util.Random;

import static java.lang.Math.sin;

public class Cargo extends Car {
    double t = 0;
    int W = w, H = h;

    public Cargo() {
        super();
    }

    public Cargo(int WW, int WH) {
        super(WW, WH);
        Random rand = new Random();
        ImInd = rand.nextInt(7) + 8;
    }

    @Override
    public void move() {
        if (isMove) {
            t += 0.01;
            w = (int) (W * (sin(t) + 1.0));
            h = (int) (H * (sin(t) + 1.0));
        }
    }

    @Override
    public void saveText(FileWriter f) {
        try {
            f.write("Cargo" + '\n');
            super.saveText(f);
            f.write(String.valueOf((int) t) + '\n');
            f.write(String.valueOf(W) + '\n');
            f.write(String.valueOf(H) + '\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadText(FileReader f) {
        super.loadText(f);
        t = FileActions.readInt(f);
        W = FileActions.readInt(f);
        H = FileActions.readInt(f);
    }

    @Override
    public void saveBin(FileOutputStream f) {
        try {
            f.write(("Cargo" + '\n').getBytes());
            super.saveBin(f);
            f.write(BinFileAction.IntToBytes((int) t));
            f.write(BinFileAction.IntToBytes(W));
            f.write(BinFileAction.IntToBytes(H));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadBin(byte[] b) {
        byte[] buf = new byte[24];
        int i = 0;
        int intNum = 8;
        for (int j = 0; j < intNum; j++, i++)
            buf[j] = b[i];
        cx = BinFileAction.ByteToInt(buf);

        for (int j = 0; j < intNum; j++, i++)
            buf[j] = b[i];
        cy = BinFileAction.ByteToInt(buf);

        for (int j = 0; j < intNum; j++, i++)
            buf[j] = b[i];
        w = BinFileAction.ByteToInt(buf);

        for (int j = 0; j < intNum; j++, i++)
            buf[j] = b[i];
        h = BinFileAction.ByteToInt(buf);

        for (int j = 0; j < intNum; j++, i++)
            buf[j] = b[i];
        ImInd = BinFileAction.ByteToInt(buf);

        isMove = BinFileAction.ByteToBool(b[i]);
        i++;

        for (int j = 0; j < intNum * 3; j++, i++)
            buf[j] = b[i];
        color = BinFileAction.ByteToColor(buf);

        for (int j = 0; j < intNum; j++, i++)
            buf[j] = b[i];
        t = BinFileAction.ByteToInt(buf);

        for (int j = 0; j < intNum; j++, i++)
            buf[j] = b[i];
        W = BinFileAction.ByteToInt(buf);

        for (int j = 0; j < intNum; j++, i++)
            buf[j] = b[i];
        H = BinFileAction.ByteToInt(buf);
    }
}
