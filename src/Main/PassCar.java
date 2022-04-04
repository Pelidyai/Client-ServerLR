package Main;

import java.io.*;
import java.util.Random;

public class PassCar extends Car {
    public PassCar() {
        super();
    }
    public PassCar(int WW, int WH) {
        super(WW, WH);
        Random rand = new Random();
        ImInd = rand.nextInt(8);
    }
    @Override
    public void move() {
        if (isMove) {
            Random rand = new Random();
            int step = rand.nextInt(30) + 1;
            cx += rand.nextInt(step) - step / 2;
            cy += rand.nextInt(step) - step / 2;
        }
    }
    @Override
    public void saveText(FileWriter f){
        try{
            f.write("PassCar" + '\n');
            super.saveText(f);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void saveBin(FileOutputStream f) {
        try {
            f.write(("PassCar" + '\n').getBytes());
            super.saveBin(f);
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
    }

    @Override
    void Serialize(ObjectOutputStream f)
    {
        try {
            f.writeUTF("PassCar");
            super.Serialize(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void Deserialize(ObjectInputStream f)
    {
        try {
            PassCar base = (PassCar) f.readObject();
            this.cx = base.cx;
            this.cy = base.cy;
            this.w = base.w;
            this.h = base.h;
            this.ImInd = base.ImInd;
            this.isMove = base.isMove;
            this.color = base.color;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
