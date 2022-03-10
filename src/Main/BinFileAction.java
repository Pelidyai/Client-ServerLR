package Main;


import java.awt.*;

public class BinFileAction {
    static byte[] IntToBytes(int val) {
        byte[] mas = new byte[8];
        mas[0] = (byte)((val&0xf0000000)>>28);
        mas[1] = (byte)((val&0x0f000000)>>24);
        mas[2] = (byte)((val&0x00f00000)>>20);
        mas[3] = (byte)((val&0x000f0000)>>16);
        mas[4] = (byte)((val&0x0000f000)>>12);
        mas[5] = (byte)((val&0x00000f00)>>8);
        mas[6] = (byte)((val&0x000000f0)>>4);
        mas[7] = (byte)(val&0x0000000f);
        return mas;
    }

    static int ByteToInt(byte[] b) {
        int val=0;
        for(int i=0; i<8; i++)
        {
            val+=((int)b[i])<<((7-i)*4);
        }
        return val;
        //return ((((((((int) b[0]) << 8) | b[1]) << 8) |  b[2]) << 8) | b[3])&0b01111111111111111111111111111111;
    }

    static byte[] ColorToBytes(Color c) {
        int[] col = new int[3];
        byte[] b = new byte[3*8];
        col[0] = c.getRed();
        col[1] = c.getGreen();
        col[2] = c.getBlue();
        for(int i=0; i<3;i++)
        {
            byte[] buf = IntToBytes(col[i]);
            for(int j=0; j<8;j++)
            {
                b[i*8+j] = buf[j];
            }
        }
        return b;
    }

    static Color ByteToColor(byte[] b) {
        int[] col = new int[3];
        for(int i=0; i<3; i++)
        {
            byte[] buf = new byte[8];
            for(int j=0; j<8;j++)
            {
                buf[j] = b[i*8+j];
            }
            col[i] = ByteToInt(buf);
        }
        return new Color(col[0], col[1], col[2]);
    }
    static byte BoolToByte(boolean is) {
        if (is) return 1;
        return 0;
    }

    static Boolean ByteToBool(byte b) {
        return b == 1;
    }
}
