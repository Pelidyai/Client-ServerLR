package Main;

import Server.DatagramServer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Client {
    static DatagramServer client = null;

    public static void start() {
        try {
            client = new DatagramServer(50401, 50402);
            client.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearVec() {
        if (client != null) {
            try {
                client.SendString("clear");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("This client hasn't connection.");
        }
    }

    public static int getVecSize() {
        if (client != null) {
            try {
                client.SendString("size");
                //int size = br.read();
                return 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("This client hasn't connection.");
        }
        return -1;
    }

    public static void addToServer(ArrayList<Car> cars, int index) {
        if (client != null) {
            try {
                if (index == -2) {
                    client.SendString("add");
                    while(client.isSending()) Thread.sleep(1);
                    Thread.sleep(5);
                    ByteArrayOutputStream b_out = new ByteArrayOutputStream();
                    ObjectOutputStream o_out = new ObjectOutputStream(b_out);
                    o_out.writeObject(cars);
                    client.SendBytes(b_out.toByteArray(), b_out.size());
                    while(client.isSending())Thread.sleep(1);
                } else {
                    if (0 <= index && index < cars.size()) {
                        client.SendString("addOne");
                        //cars.get(index).Serialize(f);
                        //f.flush();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("This client hasn't connection.");
        }
    }

    public static ArrayList<Car> getVecFromServer() {
        ArrayList<Car> cars = null;
        if (client != null) {
            try {
                client.SendString("getVec");
                while(client.isSending()) Thread.sleep(1);
                Thread.sleep(5);
                while(client.receivedCount() == 0) Thread.sleep(1);
                ByteArrayInputStream b_in = new ByteArrayInputStream(client.pullBytes());
                ObjectInputStream o_in = new ObjectInputStream(b_in);
                cars = (ArrayList<Car>) o_in.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("This client hasn't connection.");
        }
        return cars;
    }

    public static Car getOneFromServer(int index) {
        Car car = null;
        if (client != null) {
            try {
                client.SendString("getOne");
                client.SendString(Integer.toString(index));
                //ObjectInputStream f = new ObjectInputStream(new BufferedInputStream(is));
                //car = (Car) f.readObject();
                System.out.println("Object received.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("This client hasn't connection.");
        }
        return car;
    }
}