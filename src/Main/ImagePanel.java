package Main;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ImagePanel extends JPanel {
    ArrayList<Car> cars;

    public ImagePanel(ArrayList<Car> bcars)
    {
        this.setVisible(true);
        this.setLayout(new GridLayout());
        cars = bcars;
    }

    @Override
    public synchronized void paint(Graphics g) {
        super.paint(g);
        for (int i =0; i< cars.size(); i++) {
            cars.get(i).paint((Graphics2D) g);
            //System.out.print(car.cx + " " + car.cy + " ");
        }
    }
}
