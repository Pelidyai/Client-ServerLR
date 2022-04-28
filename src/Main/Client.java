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
                client.SendString("clear", true);
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
                client.SendString("size", true);
                if(!client.waitReceive(500)) return 0;
                return Integer.parseInt(client.pullString());
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
                    client.SendString("add", true);
                    ByteArrayOutputStream b_out = new ByteArrayOutputStream();
                    ObjectOutputStream o_out = new ObjectOutputStream(b_out);
                    o_out.writeObject(cars);
                    client.SendBytes(b_out.toByteArray(), b_out.size(), true);
                } else {
                    if (0 <= index && index < cars.size()) {
                        client.SendString("addOne", true);
                        client.SendString(Integer.toString(index), true);
                        ByteArrayOutputStream b_out = new ByteArrayOutputStream();
                        ObjectOutputStream o_out = new ObjectOutputStream(b_out);
                        cars.get(index).Serialize(o_out);
                        client.SendBytes(b_out.toByteArray(), b_out.size(), true);
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
                client.SendString("getVec", true);
                if(!client.waitReceive(500)) return null;
                ByteArrayInputStream b_in = new ByteArrayInputStream(client.pullBytes());
                if(b_in.available() != 0) {
                    ObjectInputStream o_in = new ObjectInputStream(b_in);
                    cars = (ArrayList<Car>) o_in.readObject();
                }
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
                client.SendString("getOne", true);
                client.SendString(Integer.toString(index), true);
                if(!client.waitReceive(500)) return null;
                ByteArrayInputStream b_in = new ByteArrayInputStream(client.pullBytes());
                if(b_in.available() != 0) {
                    ObjectInputStream f = new ObjectInputStream(b_in);
                    car = (Car) f.readObject();
                }
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