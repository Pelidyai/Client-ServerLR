package Main;

import java.awt.*;
import java.util.Random;

public abstract class Car {
    int cx,cy;
    int h = 74, w = 100;
    int ImInd;
    Color color;
    boolean isMove = true;

    private static boolean isInit =false;
    static Image[] Im = new Image[16];
    public static String[] ImName = new String[]{"Images/1.png", "Images/2.png",
            "Images/3.png", "Images/4.png",
            "Images/5.png","Images/6.png",
            "Images/7.png","Images/8.png",
            "Images/c1.png", "Images/c2.png",
            "Images/c3.png", "Images/c4.png",
            "Images/c5.png","Images/c6.png",
            "Images/c7.png"};
    public Car(int WW, int WH)
    {
        Random rand = new Random();
        cx = rand.nextInt(WW-w)+w/2;
        cy = rand.nextInt(WH-h)+h/2;
        color = new Color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255));
        InitImages();
    }
    private void InitImages()
    {
        Toolkit tool = Toolkit.getDefaultToolkit();
        for(int i=0;i<15;i++)
        {
            Im[i] = tool.getImage(ImName[i]);
        }
        isInit=true;
    }
    public void paint(Graphics2D g) {
        g.setColor(color);
        g.fillRect(cx-w/2, cy-h/2, w,h);
        g.drawImage(Im[ImInd], cx-w/2, cy-h/2, w, h, null);
        //g.drawOval(cx, cy, 5, 5);
    }
    public boolean isIn(int x, int y)
    {
        return (cx-w/2<=x&&x<=cx+w/2)&&(cy-h/2<=y&&y<=cy+h/2);
    }
    abstract public void move();
}
