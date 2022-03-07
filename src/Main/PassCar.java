package Main;

import java.io.FileWriter;
import java.io.IOException;
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
}
