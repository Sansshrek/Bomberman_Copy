package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Point;

import main.GamePanel;

public class Entity {
    GamePanel gp;
    int ogTileSize;
    int scale;
    int tileSize;
    public Graphics2D g2;
    // public int x, y;  // le coordinate nel mondo
    public Point imageP;  // le coordinate dell'immagine
    public double speed;
    
    
    public String direction;

    public int spriteCounter = 0;
    public int spriteNum = 1;

    public Rectangle hitbox;
    public int hitboxDefaultX, hitboxDefaultY;
    public int hitboxWidth;
    public int hitboxHeight;
    public boolean collisionOn = false;

    public Entity(GamePanel gp){
        this.gp=gp;
        this.tileSize = gp.tileSize;
        this.hitboxWidth = tileSize;
        this.hitboxHeight = tileSize;
    }
}