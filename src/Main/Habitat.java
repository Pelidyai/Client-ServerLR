package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Habitat extends JPanel {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Habitat h = new Habitat();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 700);

        h.setVisible(true);

        h.Init();
        h.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switch (e.getButton()) {
                    case 1: {
                        boolean flag = true;
                        for (int i = 0; i < h.cars.size(); i++) {
                            if (h.cars.get(i).isIn(e.getX(), e.getY())) {
                                synchronized (h.cars) {
                                    h.cars.remove(i);
                                }
                                //System.out.println("tik");
                                flag = false;
                            }
                        }
                        if (flag) {
                            Car car;
                            Random rand = new Random();
                            if (rand.nextBoolean())
                                car = new Cargo(750, 650);
                            else
                                car = new PassCar(750, 650);
                            car.cx = e.getX();
                            car.cy = e.getY();
                            synchronized (h.cars) {
                                h.cars.add(car);
                            }
                        }
                        break;
                    }
                    case 3: {
                        for (int i = 0; i < h.cars.size(); i++) {
                            if (h.cars.get(i).isIn(e.getX(), e.getY())) {
                                synchronized (h.cars) {
                                    h.cars.get(i).isMove = !h.cars.get(i).isMove;
                                }
                            }
                        }
                        break;
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                switch (e.getKeyChar()) {
                    case ' ': {
                        Check.isMove = !Check.isMove;
                        break;
                    }
                    case 't': {
                        h.SaveText("Cars.txt");
                        break;
                    }
                    case 'r': {
                        h.LoadText("Cars.txt");
                        break;
                    }
                }

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        frame.add(h);
        frame.setVisible(true);
//        while(true)
//        h.repaint();
//        while(true)
//            frame.repaint();
    }

    public ArrayList<Car> cars = new ArrayList<Car>();
    Threads.PaintThread pThread;
    Threads.AIThread aiThread;
    Threads.GenThread genThread;
    ImagePanel imPan;

    public void Init() {
        //genThread.setH(this);
        // genThread.start();
        pThread.setH(this);
        pThread.start();
        aiThread.setH(this);
        aiThread.start();
    }

    public Habitat() {
        pThread = new Threads.PaintThread();
        aiThread = new Threads.AIThread();
        // genThread = new Threads.GenThread(750, 650);
        //eThread = new Threads.EmptyThread();
        imPan = new ImagePanel(cars);
        this.setLayout(new BorderLayout());
        this.add(imPan, BorderLayout.CENTER);
    }

    @Override
    public synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public void SaveText(String filename) {
        try {
            FileWriter f = new FileWriter(filename);
            f.write(String.valueOf(cars.size()) + '\n');
            for (Car car : cars) {
                car.saveText(f);
            }
            f.close();
            System.out.println("save");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void LoadText(String filename) {
        try {
            FileReader f = new FileReader(filename);
            cars.clear();
            int count = FileActions.readInt(f);
            for (int i = 0; i < count; i++) {
                String name = FileActions.readStr(f);
                Car c;
                try {
                    Class<?> t = Class.forName("Main." + name);
                    c = (Car) t.newInstance();
                    c.loadText(f);
                    cars.add(c);
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
            }
            f.close();
            System.out.println("load");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
