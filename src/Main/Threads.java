package Main;

import java.awt.*;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Random;

/**
 * Created by User on 26.03.2021.
 */
public class Threads {

    public static class PaintThread extends Thread {
        Habitat h;

        PaintThread() {
            super("Paint Thread");

        }

        public void setH(Habitat hab) {
            h = hab;
        }

        @Override
        public void run() {
            while (!Check.isExit)
                while (Check.isWork) {
                    h.repaint();
                    //System.out.println("Print");
                    try {
                        sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    public static class AIThread extends Thread {
        Habitat h;

        AIThread() {
            super("GenThread");
        }

        public void setH(Habitat hab) {
            h = hab;
        }

        @Override
        public void run() {
            while (Check.isWork) {
                if (Check.isMove) {
                    for (int i = 0; i < h.cars.size(); i++)
                        h.cars.get(i).move();
                }
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class GenThread extends Thread {
        GenThread(int WW, int WH) {
            super("GenThread");
            last = LocalTime.now().toSecondOfDay();
            rv = new Dimension(WW, WH);
        }

        Dimension rv;
        Habitat h;
        public static long last;

        public void setH(Habitat hab) {
            h = hab;
        }

        @Override
        public void run() {
            while (!Check.isExit) {
                while (Check.isWork) {
                    Car car;
                    Random rand = new Random();
                    if (rand.nextBoolean())
                        car = new Cargo(rv.width, rv.height);
                    else
                        car = new PassCar(rv.width, rv.height);
                    synchronized (h.cars) {
                        h.cars.add(car);
                    }
                    last = LocalTime.now().toSecondOfDay();
                    //System.out.println("Gen");
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.gc();
                }
            }
        }
    }

}
