package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Point;

import main.GamePanel;

public class Entity {
    GamePanel gp;
    int tileSize;
    public Graphics2D g2;
    // public int x, y;  // le coordinate nel mondo
    public Point imageP;  // le coordinate dell'immagine
    public double speed;

    public int hitboxX, hitboxY;
    
    
    public String direction;

    public int spriteCounter = 0;
    public int spriteNum = 1;

    public Rectangle hitbox;
    public int hitboxDefaultX, hitboxDefaultY;
    public int hitboxWidth;
    public int hitboxHeight;
    public boolean collisionOn = false;
    public boolean died = false, extinguished = false;

    public Entity(GamePanel gp){
        this.gp=gp;
        this.tileSize = gp.tileSize;
        this.hitboxWidth = tileSize;
        this.hitboxHeight = tileSize;
    }

    public int getCenterX(){
        return (hitbox.x + hitbox.x + hitbox.width) / 2;
    }
    public int getCenterY(){
        return (hitbox.y + hitbox.y + hitbox.height) / 2;
    }
    public int getTileNumCol() {
        int adjustedX = getCenterX() - 72; // Spostamento a sinistra
        // prende il centro del player meno la distanza da dove parte a sinistra la mappa (gp.gameBorderLeftX)
        return adjustedX / gp.tileSize;
    }
    public int getTileNumRow() {
        int adjustedY = getCenterY() - 120; // Spostamento verso l'alto
        // prende il centro del player meno la distanza da dove parte sopra la mappa (gp.gameBorderUpY)
        return adjustedY / gp.tileSize;
    }
    public int getTileX() {
        return getTileNumCol() * gp.tileSize + 72; // TilePlayerX*48 + lo spostamento a sinistra
    }
    
    public int getTileY() {
        return getTileNumRow() * gp.tileSize + 120; // TilePlayerY*48 + lo spostamento verso l'alto
    }
}