package Main;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import static java.lang.Math.sin;

public class Cargo extends Car{
    double t = 0;
    int W = w, H=h;
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
    public void saveText(FileWriter f){
        try{
            f.write("Cargo" + '\n');
            super.saveText(f);
            f.write(String.valueOf((int)t) + '\n');
            f.write(String.valueOf(W)+ '\n');
            f.write(String.valueOf(H) + '\n');
        }catch(IOException e){
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
}
