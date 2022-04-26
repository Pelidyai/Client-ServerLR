package Server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.temporal.ChronoField;
import java.util.*;

public class DatagramServer {
    // Specified server port
    public static int clientPort = 50501;
    public static int serverPort = 50502;

    public int MTU = 50;
    private final int headerSize = 14;
    public Map<Integer, DatagramPacketClass> sendMap = new HashMap<>();
    public SortedMap<Integer, DatagramPacketClass> receiveDatagramMap = new TreeMap<>();
    public ArrayList<byte[]> receiveByteArray = new ArrayList<>();
    public static DatagramSocket ds;
    private boolean isStop = false;
    private boolean isStart = false;

    public DatagramServer(int localport, int destport) throws SocketException {
        serverPort = localport;
        clientPort = destport;
        ds = new DatagramSocket(serverPort);
    }

    public void start(){
        if(isStart) return;
        isStop = false;
        isStart = true;
        SocketListener sl = new SocketListener();
        SocketSendTimer sst = new SocketSendTimer();
        SocketReceiveProcessTimer srpt = new SocketReceiveProcessTimer();
        sl.start();
        sst.start();
        srpt.start();
    }

    public void stop(){
        if(!isStart) return;
        isStop = true;
        isStart = false;
    }

    private class SocketListener extends Thread{
        @Override
        public void run() {
            super.run();
            byte[] buffer = new byte[MTU];
            while(!isStop){
                try {
                    DatagramPacket p = new DatagramPacket(buffer, MTU);
                    ds.receive(p);
                    DatagramPacketClass dpc = new DatagramPacketClass(p.getData(), p.getLength());
                    if(dpc.dataLength == 0) {
                        sendMap.remove(dpc.seq);
                    }
                    else if(dpc.isValidCheckSum()){
                        receiveDatagramMap.put(dpc.seq, new DatagramPacketClass(dpc));
                        dpc.dataLength = 0;
                        dpc.length = headerSize;
                        ds.send(new DatagramPacket(dpc.toBytes(), dpc.length, InetAddress.getLocalHost(), clientPort));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class SocketReceiveProcessTimer extends Thread{
        ArrayList<DatagramPacketClass> buf = new ArrayList<>();
        boolean initFlag = false;
        int lastSeq = 0;
        private void PacketProcessing(){
            if(buf.isEmpty()) return;
            ByteBuffer res = ByteBuffer.allocate((MTU - headerSize) * (buf.size() - 1) + buf.get(buf.size() - 1).dataLength);
            for(int i = 0; i < buf.size(); i++){
                receiveDatagramMap.remove(buf.get(i).seq);
                res.put(buf.get(i).data);
            }
            System.out.println("r " + new String(res.array()) + " - " + Integer.toString(res.array().length) + " - " + Integer.toString(buf.size()));
            receiveByteArray.add(res.array());
        }
        @Override
        public void run() {
            super.run();
            while(!isStop){
                Map<Integer, DatagramPacketClass> map_buf = new TreeMap<>(receiveDatagramMap);
                for (Map.Entry<Integer, DatagramPacketClass> obj : map_buf.entrySet()) {
                    DatagramPacketClass dpc = obj.getValue();
                    if(dpc.Mbyte == (byte)0 && initFlag){
                        if(lastSeq + 1 == dpc.seq){
                            buf.add(dpc);
                            PacketProcessing();
                        }
                        else{
                            initFlag = false;
                        }
                        buf.clear();
                        continue;
                    }
                    if(dpc.Ibyte == (byte)1){
                        buf.clear();
                        buf.add(dpc);
                        if(dpc.Mbyte == (byte)0) {
                            PacketProcessing();
                            buf.clear();
                        }
                        else {
                            initFlag = true;
                            lastSeq = dpc.seq;
                        }
                        continue;
                    }
                    if(initFlag && lastSeq + 1 == dpc.seq){
                        lastSeq = dpc.seq;
                        buf.add(dpc);
                    }
                    else if(initFlag){
                        initFlag = false;
                        buf.clear();
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private class SocketSendTimer extends Thread {
        @Override
        public void run() {
            super.run();
            while (!isStop) {
                Map<Integer, DatagramPacketClass> buf = new HashMap<>(sendMap);
                for (Map.Entry<Integer, DatagramPacketClass> obj : buf.entrySet()) {
                    DatagramPacketClass dpc = obj.getValue();
                    try {
                        ds.send(new DatagramPacket(dpc.toBytes(), dpc.length, InetAddress.getLocalHost(), clientPort));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void SendBytes(byte[] packet, int len){
        int packetCount = (len - 1) / (MTU - headerSize) + 1;
        int seq = new Random(Instant.now().get(ChronoField.MICRO_OF_SECOND)).nextInt();
        for(int i = 0; i < packetCount; i++) {
            int datagramSize = MTU;
            if (i == (packetCount - 1)) datagramSize = len % (MTU - headerSize) + headerSize;
            DatagramPacketClass dpc = new DatagramPacketClass(
                    seq,
                    (byte) (i == 0 ? 1 : 0),
                    (byte) (i != packetCount - 1 ? 1 : 0),
                    Arrays.copyOfRange(packet, i * (MTU - headerSize),
                            (i != packetCount - 1) ? ((i + 1) * (MTU - headerSize)) : len ),
                    datagramSize - headerSize
            );
            sendMap.put(dpc.seq, dpc);
            try {
                ds.send(new DatagramPacket(dpc.toBytes(), dpc.length, InetAddress.getLocalHost(), clientPort));
            } catch (IOException e) {
                e.printStackTrace();
            }
            seq++;
        }
        System.out.println("s " + new String(Arrays.copyOfRange(packet,0,len)) + " - " + Integer.toString(len) + " - " + Integer.toString(packetCount));
    }

    public void SendString(String str){
        SendBytes(str.getBytes(), str.length());
    }

    public String pullString(){
        if(receiveByteArray.size() == 0) return "";
        String str = new String(receiveByteArray.get(0));
        receiveByteArray.remove(0);
        return str;
    }

    public byte[] pullBytes(){
        if(receiveByteArray.size() == 0) return null;
        byte[] bytes = receiveByteArray.get(0);
        receiveByteArray.remove(0);
        return bytes;
    }

    public boolean isSending(){
        return sendMap.size() > 0;
    }

    public int receivedCount(){
        return receiveByteArray.size();
    }
}