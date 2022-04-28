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
                server.waitReceive(-1);
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
                        server.SendString(Integer.toString(cars.size()), true);
                        break;
                    }
                    case "add": {
                        System.out.println("Add vector.");
                        System.out.println("Vector size before operation: " + cars.size() + ".");
                        if(!server.waitReceive(500)) continue;
                        ByteArrayInputStream b_in = new ByteArrayInputStream(server.pullBytes());
                        if(b_in.available() != 0) {
                            ObjectInputStream o_in = new ObjectInputStream(b_in);
                            cars = (ArrayList<Car>) o_in.readObject();
                        }
                        System.out.println("Vector size after operation: " + cars.size() + ".");
                        break;
                    }
                    case "addOne": {
                        System.out.println("Add One to vector.");
                        System.out.println("Vector size before operation: " + cars.size() + ".");
                        if(!server.waitReceive(500)) continue;
                        int index = Integer.parseInt(server.pullString());
                        if(!server.waitReceive(500)) continue;
                        ByteArrayInputStream b_in = new ByteArrayInputStream(server.pullBytes());
                        if(b_in.available() != 0) {
                            ObjectInputStream f = new ObjectInputStream(b_in);

                            try {
                                String name = f.readUTF();
                                System.out.println(name);
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
                            }
                        }
                        System.out.println("Vector size after operation: " + cars.size() + ".");
                        break;
                    }
                    case "getVec": {
                        try {
                            System.out.printf("Send Vector. Size %d\n", cars.size());
                            ByteArrayOutputStream b_out = new ByteArrayOutputStream();
                            ObjectOutputStream o_out = new ObjectOutputStream(b_out);
                            o_out.writeObject(cars);
                            server.SendBytes(b_out.toByteArray(), b_out.size(), true);
                            System.out.println("OK.");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case "getOne": {
                        if(!server.waitReceive(500)) continue;
                        int index = Integer.parseInt(server.pullString());
                        ByteArrayOutputStream b_out = new ByteArrayOutputStream();
                        ObjectOutputStream o_out = new ObjectOutputStream(b_out);
                        if (index < 0 || index > cars.size() - 1) {
                            o_out.writeObject(null);
                        } else {
                            o_out.writeObject(cars.get(index));
                        }
                        server.SendBytes(b_out.toByteArray(), b_out.size(), true);
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