package Main;

import Server.DatagramServer;

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
                //ObjectOutputStream f = new ObjectOutputStream(new BufferedOutputStream(os));
                if (index == -2) {
                    client.SendString("add");
                    //f.writeObject(cars);
                    //f.flush();
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
                //ObjectInputStream f = new ObjectInputStream(new BufferedInputStream(is));
                //cars = (ArrayList<Car>) f.readObject();
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