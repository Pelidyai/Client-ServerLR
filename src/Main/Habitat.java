package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
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
                    case 'b': {
                        h.saveBin("BinCars.txt");
                        break;
                    }
                    case 'v': {
                        h.loadBin("BinCars.txt");
                        break;
                    }
                    case 's': {
                        h.Serialize("SerCar.dat");
                        break;
                    }
                    case 'a': {
                        h.Deserialize("SerCar.dat");
                        break;
                    }
                    case 'x': {
                        h.SaveXML("XMLCars.xml");
                        break;
                    }
                    case 'z': {
                        h.LoadXML("XMLCars.xml");
                        break;
                    }
                    case 'p':
                    {
                        SocketClient.start();
                        break;
                    }
                    case 'o':
                    {
                        SocketClient.stop();
                        break;
                    }
                    case 'i':
                    {
                        SocketClient.clearVec();
                        break;
                    }
                    case 'u':
                    {
                        System.out.println("Received size: " + SocketClient.getVecSize() + ".");
                        break;
                    }
                    case 'y':{
                        SocketClient.addToServer(h.cars, -2);
                        break;
                    }
                    case '\'':{
                        SocketClient.addToServer(h.cars, 0);
                        break;
                    }
//                    case 'j':{
//                        try {
//                            FileOutputStream f = new FileOutputStream("tets.txt");
//                            int val = 65152;
//                            byte[] b= BinFileAction.IntToBytes(val);
//                            for(byte by:b)
//                                System.out.println(by);
//                            val = BinFileAction.ByteToInt(b);
//                            System.out.println(val);
//
//                        } catch (FileNotFoundException fileNotFoundException) {
//                            fileNotFoundException.printStackTrace();
//                        }
//                        break;
//                    }
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

    public ArrayList<Car> cars = new ArrayList<>();
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
        System.out.println("Text file saved.");
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
        System.out.println("Text file loaded.");
    }

    public void saveBin(String filename){
        try {
            FileOutputStream f = new FileOutputStream(filename);
            f.write(BinFileAction.IntToBytes(cars.size()));
            for (Car car : cars) {
                car.saveBin(f);
            }
            f.close();
            System.out.println("Bin saved");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        System.out.println("Binary file saved.");
    }

    public void loadBin(String filename) {
        try {
            FileInputStream f = new FileInputStream(filename);
            cars.clear();
            byte[] b = new byte[f.available()];
            byte[] buf = new byte[90];
            f.read(b);
            int i=0;
            int IntNum = 8;
            int CrB = 89;
            int PsB = 65;

            for(int j=0; j<IntNum; j++, i++)
                buf[j] = b[i];
            int count = BinFileAction.ByteToInt(buf);
            for (int k = 0; k < count; k++) {
                int j=0;
                StringBuilder sb = new StringBuilder();
                while(true){
                    if((char)b[i]=='\n')
                        break;
                    sb.append((char)b[i]);
                    i++;
                }
                i++;
                String name = sb.toString();
                Car c;
                int numB = PsB;
                try {
                    Class<?> t = Class.forName("Main." + name);
                    c = (Car) t.newInstance();
                    if (name.equals("Cargo")) numB = CrB;
                    for(j=0; j<numB; j++, i++)
                        buf[j] = b[i];
                    c.loadBin(buf);
                    cars.add(c);
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
            }
            f.close();
            System.out.println("Bin loaded");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        System.out.println("Binary file loaded.");
    }

    public void Serialize(String file){
        try {
            OutputStream fstream = new FileOutputStream(file);
            ObjectOutputStream f = new ObjectOutputStream(fstream);
            int i=0;
            f.write(cars.size());
            while (i<this.cars.size()) {
                cars.get(i).Serialize(f);
                i++;
            }
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Serialized.");
    }

    public void Deserialize(String file){
        cars.clear();
        try {
            InputStream fstream = new FileInputStream(file);
            ObjectInputStream f = new ObjectInputStream(fstream);
            int n = f.read();
            int i=0;
            while (i<n) {
                String name = f.readUTF();
                Car c;
                try {
                    Class<?> t = Class.forName("Main." + name);
                    if (name.equals("Cargo"))
                        c = (Cargo) t.newInstance();
                    else
                        c = (PassCar) t.newInstance();
                    c.Deserialize(f);
                    cars.add(c);
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
                i++;
            }
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Deserialized.");
    }

    public void SaveXML(String file){
        try {
            XMLEncoder f = new XMLEncoder(new FileOutputStream(file));
            f.writeObject(cars.size());
            int i=0;
            while(i<cars.size())
            {
                f.writeObject(cars.get(i));
                i++;
            }
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("XML file saved.");
    }

    public void LoadXML(String file){
        try {
            XMLDecoder f = new XMLDecoder(new FileInputStream(file));
            Car.InitImages();
            int n = (int) f.readObject();
            int i=0;
            cars.clear();
            while(i<n)
            {
                cars.add((Car)f.readObject());
                i++;
            }
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("XML file loaded.");
    }
}
