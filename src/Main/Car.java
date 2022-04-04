package Main;

import java.awt.*;
import java.io.*;
import java.util.Random;

public abstract class Car implements Serializable {
    int cx, cy;
    int h = 74, w = 100;
    int ImInd;
    Color color;
    boolean isMove = true;

    public void setCx(int cx) {
        this.cx = cx;
    }
    public int getCx() {
        return cx;
    }
    public void setCy(int cy) {
        this.cy = cy;
    }
    public int getCy() {
        return cy;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public Color getColor() {
        return color;
    }
    public void setH(int h) {
        this.h = h;
    }
    public int getH() {
        return h;
    }
    public void setImInd(int ImInd1) {
        ImInd = ImInd1;
    }
    public int getImInd() {
        return ImInd;
    }
    public void setMove(boolean isMove1) {
        isMove = isMove1;
    }
    public boolean getMove(){
        return isMove;
    }
    public void setW(int w) {
        this.w = w;
    }
    public int getW() {
        return w;
    }
    public static Image[] getIm() {
        return Im;
    }
    public static void setIm(Image[] im) {
        Im = im;
    }
    public static String[] getImName() {
        return ImName;
    }
    public static void setImName(String[] imName) {
        ImName = imName;
    }

    private static boolean isInit = false;
    static Image[] Im = new Image[16];
    public static String[] ImName = new String[]{"Images/1.png", "Images/2.png",
            "Images/3.png", "Images/4.png",
            "Images/5.png", "Images/6.png",
            "Images/7.png", "Images/8.png",
            "Images/c1.png", "Images/c2.png",
            "Images/c3.png", "Images/c4.png",
            "Images/c5.png", "Images/c6.png",
            "Images/c7.png"};

    public Car() {
        InitImages();
    }

    public Car(int WW, int WH) {
        Random rand = new Random();
        cx = rand.nextInt(WW - w) + w / 2;
        cy = rand.nextInt(WH - h) + h / 2;
        color = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
        InitImages();
    }

    public static void InitImages() {
        if (!isInit) {
            Toolkit tool = Toolkit.getDefaultToolkit();
            for (int i = 0; i < 15; i++) {
                Im[i] = tool.getImage(ImName[i]);
            }
        }
        isInit = true;
    }

    public void paint(Graphics2D g) {
        g.setColor(color);
        g.fillRect(cx - w / 2, cy - h / 2, w, h);
        g.drawImage(Im[ImInd], cx - w / 2, cy - h / 2, w, h, null);
        //g.drawOval(cx, cy, 5, 5);
    }

    public boolean isIn(int x, int y) {
        return (cx - w / 2 <= x && x <= cx + w / 2) && (cy - h / 2 <= y && y <= cy + h / 2);
    }

    abstract public void move();

    public void saveText(FileWriter f) {
        try {
            f.write(String.valueOf(cx) + '\n');
            f.write(String.valueOf(cy) + '\n');
            f.write(String.valueOf(w) + '\n');
            f.write(String.valueOf(h) + '\n');
            f.write(String.valueOf(ImInd) + '\n');
            f.write(String.valueOf(isMove) + '\n');
            f.write(String.valueOf(color.getRed()) + ';');
            f.write(String.valueOf(color.getGreen()) + ';');
            f.write(String.valueOf(color.getBlue()) + '\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadText(FileReader f) {
        cx = FileActions.readInt(f);
        cy = FileActions.readInt(f);
        w = FileActions.readInt(f);
        h = FileActions.readInt(f);
        ImInd = FileActions.readInt(f);
        isMove = FileActions.readBool(f);
        color = FileActions.readColor(f);
    }

    public void saveBin(FileOutputStream f) {
        try {
            f.write(BinFileAction.IntToBytes(cx));
            f.write(BinFileAction.IntToBytes(cy));
            f.write(BinFileAction.IntToBytes(w));
            f.write(BinFileAction.IntToBytes(h));
            f.write(BinFileAction.IntToBytes(ImInd));
            f.write(BinFileAction.BoolToByte(isMove));
            f.write(BinFileAction.ColorToBytes(color));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    abstract public void loadBin(byte[] b);

    void Serialize(ObjectOutputStream f)
    {
        try {
            f.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void Deserialize(ObjectInputStream f);
}
