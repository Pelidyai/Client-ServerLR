package Main;

import java.util.Random;

public class PassCar extends Car{
    public PassCar(int WW, int WH)
    {
        super(WW, WH);
        Random rand = new Random();
        ImInd = rand.nextInt(8);
    }
    public void move()
    {
        Random rand = new Random();
        int step = rand.nextInt(30)+1;
        cx += rand.nextInt(step)-step/2;
        cy += rand.nextInt(step)-step/2;
    }
}
