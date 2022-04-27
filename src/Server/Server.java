package Server;

import Main.Car;
import Main.Cargo;
import Main.PassCar;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public static ArrayList<Car> cars = new ArrayList<>();
    static DatagramServer server = null;

    public static void main(String[] args) {
        try {
            server = new DatagramServer(50402, 50401);
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        while (true) {
            try {
                System.out.println("Listening");
                while(server.receivedCount() == 0) Thread.sleep(1);
                String command = server.pullString();
                System.out.println("Command on server: " + command);
                if (command == null)
                    continue;
                switch (command) {
                    case "stop": {
                        System.out.println("Client disconnected.");
                        break;
                    }
                    case "start": {
                        System.out.println("Client connected.");
                        break;
                    }
                    case "clear": {
                        System.out.println("Vector was cleared.");
                        cars.clear();
                        break;
                    }
                    case "size": {
                        System.out.println("return size: " + cars.size() + ".");
                        server.SendString(Integer.toString(cars.size()));
                        break;
                    }
                    case "add": {
                        System.out.println("Add vector.");
                        System.out.println("Vector size before operation: " + cars.size() + ".");
                        while(server.receivedCount() == 0) Thread.sleep(1);
                        ByteArrayInputStream b_in = new ByteArrayInputStream(server.pullBytes());
                        ObjectInputStream o_in = new ObjectInputStream(b_in);
                        cars = (ArrayList<Car>) o_in.readObject();
                        System.out.println("Vector size after operation: " + cars.size() + ".");
                        break;
                    }
                    case "addOne": {
                        System.out.println("Add One to vector.");
                        System.out.println("Vector size before operation: " + cars.size() + ".");
                        /*ObjectInputStream f = new ObjectInputStream(new BufferedInputStream(serverInput));

                        try {
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
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                        System.out.println("Vector size after operation: " + cars.size() + ".");
                        break;
                    }
                    case "getVec": {
                        try {
                            System.out.printf("Send Vector. Size %d\n", cars.size());
                            ByteArrayOutputStream b_out = new ByteArrayOutputStream();
                            ObjectOutputStream o_out = new ObjectOutputStream(b_out);
                            o_out.writeObject(cars);
                            server.SendBytes(b_out.toByteArray(), b_out.size());
                            while(server.isSending())Thread.sleep(1);
                            System.out.println("OK.");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case "getOne": {
                        /*int index = br.read();
                        ObjectOutputStream f = new ObjectOutputStream(new BufferedOutputStream(serverOutput));
                        if (index < 0 || index > cars.size() - 1) {
                            f.writeObject(null);
                        } else {
                            f.writeObject(cars.get(index));
                        }
                        f.flush();*/
                        System.out.println("Object send.");
                        break;
                    }
                    default:
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}