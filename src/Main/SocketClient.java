package Main;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class SocketClient {
    static Socket clientSocket = null;
    static DataOutputStream os = null;
    static InputStream is = null;
    static BufferedReader br = null;

    public static void start() {
        if (clientSocket == null) {
            try {
                clientSocket = new Socket("localhost", 50001);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                os = new DataOutputStream(clientSocket.getOutputStream());
                os.writeBytes("start\n");
                os.flush();
                is = clientSocket.getInputStream();
                br = new BufferedReader(new InputStreamReader(is));
                //f = new ObjectOutputStream(new BufferedOutputStream(os));
//                String receivedData = br.readLine();
//                System.out.println("Received Data: " + receivedData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("This client is already connected.");
        }
    }

    public static void stop() {
        System.out.println(1);
        if (clientSocket != null) {
            System.out.println(2);
            try {
                os.writeBytes("stop\n");
                os.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("This client hasn't connection.");
        }
        clientSocket = null;
    }

    public static void clearVec() {
        if (clientSocket != null) {
            try {
                os.writeBytes("clear\n");
                os.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("This client hasn't connection.");
        }
    }

    public static int getVecSize() {
        if (clientSocket != null) {
            try {
                os.writeBytes("size\n");
                os.flush();
                int size = br.read();
                return size;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("This client hasn't connection.");
        }
        return -1;
    }

    public static void addToServer(ArrayList<Car> cars, int index) {
        if (clientSocket != null) {
            try {
                ObjectOutputStream f = new ObjectOutputStream(new BufferedOutputStream(os));
                if (index == -2) {
                    os.writeBytes("add\n");
                    os.flush();
                    f.writeObject(cars);
                    f.flush();
                } else {
                    if (0 <= index && index < cars.size()) {
                        os.writeBytes("addOne\n");
                        os.flush();
                        cars.get(index).Serialize(f);
                        f.flush();
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
        if (clientSocket != null) {
            try {
                os.writeBytes("getVec" + '\n');
                os.flush();
                ObjectInputStream f = new ObjectInputStream(new BufferedInputStream(is));
                cars = (ArrayList<Car>) f.readObject();
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
        if (clientSocket != null) {
            try {
                os.writeBytes("getOne" + '\n');
                os.write(index);
                os.flush();
                ObjectInputStream f = new ObjectInputStream(new BufferedInputStream(is));
                car = (Car) f.readObject();
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