package main;

import java.awt.Graphics2D;

public interface Panel{
    public void drawPanel(Graphics2D g2, GamePanel gp);
    public void chooseOptions(GamePanel gp);

}