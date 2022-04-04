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

                is = clientSocket.getInputStream();
                br = new BufferedReader(new InputStreamReader(is));
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("This client hasn't connection.");
        }
    }

    public static int getVecSize(){
        if (clientSocket != null) {
            try {
                os.writeBytes("size\n");
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

    public static void addToServer(ArrayList<Car> cars, int index)
    {
        if (clientSocket != null) {
            try {
                if(index == -2){
                    os.writeBytes("add\n");
                    ObjectOutputStream f = new ObjectOutputStream(os);
                    f.flush();
                    f.writeObject(cars);
                }
                else{
                    if(0<=index && index<cars.size()) {
                        os.writeBytes("addOne\n");
                        ObjectOutputStream f = new ObjectOutputStream(os);
                        cars.get(index).Serialize(f);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("This client hasn't connection.");
        }
    }
}