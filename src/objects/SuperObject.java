package objects;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.Point;

import main.GamePanel;

public class SuperObject {
    GamePanel gp;
    public BufferedImage image;
    public String name;
    public int x, y;
    public Point blockP, indexObj;
    public int tileX, tileY;
    public boolean collision = false;
    public Rectangle hitbox;
    public int hitboxDefaultX = 0;
    public int hitboxDefaultY = 0;
    public int tileSize;
    public PowerUp power;
    boolean exploded = false;

    public SuperObject(GamePanel gp){
        this.gp = gp;
        this.tileSize=gp.getTileSize();
        this.hitbox = new Rectangle(0, 0, tileSize ,tileSize);
        this.hitboxDefaultX = tileSize;
        this.hitboxDefaultY = tileSize;
    }

    public void draw(Graphics2D g2, GamePanel gp){

        g2.drawImage(image, x, y, tileSize, tileSize, null);
    }
    
    public void destroy(){
    }

    public void update(){

    }
}
