package Main;

import java.util.Random;

import static java.lang.Math.sin;

public class Cargo extends Car{
    double t = 0;
    int W = w, H=h;
    public Cargo(int WW, int WH)
    {
        super(WW, WH);
        Random rand = new Random();
        ImInd = rand.nextInt(8)+7;
    }
    public void move()
    {
        t += 0.01;
        w = (int)(W*(sin(t)+1.0));
        h = (int)(H*(sin(t)+1.0));
    }
}
