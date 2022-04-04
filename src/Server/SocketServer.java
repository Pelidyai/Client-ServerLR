package Server;

import Main.Car;
import Main.Cargo;
import Main.PassCar;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class SocketServer {
    public static ArrayList<Car> cars = new ArrayList<>();
    public static final int SERVER_PORT = 50001;

    public static void main(String[] args) {
//        JFrame serverFrame = new JFrame();
//        serverFrame.setSize(400, 500);
//        serverFrame.setVisible(true);
        ServerSocket server;
        try {
            server = new ServerSocket(SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        while (true) {
            try {
                Socket clientConn = server.accept();
                InputStream serverInput = clientConn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(serverInput));


                DataOutputStream serverOutput = new DataOutputStream(clientConn.getOutputStream());

                while (!clientConn.isClosed()) {
                    String command = br.readLine();
                    System.out.println("Command on server: " + command);
                    if (command == null)
                        continue;
                    switch (command) {
                        case "stop": {
                            System.out.println("Client disconnected.");
                            clientConn.close();
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
                            serverOutput.writeInt(cars.size());
                            break;
                        }
                        case "add": {
                            System.out.println("Add vector.");
                            System.out.println("Vector size before operation: " + cars.size() + ".");
                            ObjectInputStream f = new ObjectInputStream(serverInput);
                            try {
                                try {
                                    cars = (ArrayList<Car>) f.readObject();
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            System.out.println("Vector size after operation: " + cars.size() + ".");
                            break;
                        }
                        case "addOne": {
                            ObjectInputStream f = new ObjectInputStream(serverInput);
                            System.out.println("Add One to vector.");
                            System.out.println("Vector size before operation: " + cars.size() + ".");
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
                            }
                            System.out.println("Vector size after operation: " + cars.size() + ".");
                            break;
                        }
                        default:
                            throw new IllegalStateException("Unexpected value: " + command);
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}