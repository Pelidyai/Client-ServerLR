package Server;

import java.nio.ByteBuffer;
import java.util.zip.Adler32;

public class DatagramPacketClass {
    public long checkSum;
    public int seq;
    public byte Ibyte;
    public byte Mbyte;
    public byte[] data;
    public int dataLength;
    public int length;
    private final int headerSize = 14;
    Adler32 check = new Adler32();

    public DatagramPacketClass(int Seq, byte I, byte M, byte[] Data, int DataLength){
        seq = Seq; Ibyte = I; Mbyte = M; data = Data; dataLength = DataLength; length = dataLength + headerSize;
        checkSum = checkSumCalc();
    }

    public DatagramPacketClass(DatagramPacketClass dpc){
        seq = dpc.seq; Ibyte = dpc.Ibyte; Mbyte = dpc.Mbyte; data = dpc.data; dataLength = dpc.dataLength; length = dpc.length;
        checkSum = dpc.checkSum;
    }

    public DatagramPacketClass(byte[] packet, int length) {
        ByteBuffer buffer = ByteBuffer.wrap(packet, 0, length);
        checkSum = buffer.getLong(0);
        seq = buffer.getInt(8);
        Ibyte = buffer.get(12);
        Mbyte = buffer.get(13);
        dataLength = length - headerSize;
        this.length = length;
        data = new byte[dataLength];
        for (int j = 0; j < dataLength; j++)
            data[j] = buffer.get(headerSize + j);

    }

    public boolean isValidCheckSum(){
        return (checkSum == checkSumCalc());
    }

    public long checkSumCalc(){
        ByteBuffer buffer = ByteBuffer.allocate(length - 8);
        buffer.putInt(seq);
        buffer.put(Ibyte);
        buffer.put(Mbyte);
        for (int j = 0; j < dataLength; j++)
            buffer.put(data[j]);
        check.update(buffer.array());
        return check.getValue();
    }

    public byte[] toBytes(){
        ByteBuffer buffer = ByteBuffer.allocate(length);
        buffer.putLong(checkSum);
        buffer.putInt(seq);
        buffer.put(Ibyte);
        buffer.put(Mbyte);
        for (int j = 0; j < dataLength; j++)
            buffer.put(data[j]);
        return buffer.array();
    }
}

