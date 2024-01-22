package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.List;
import java.awt.Point;
import main.KeyHandler;

import main.GamePanel;

public class Entity implements EntityObservable{
    ArrayList<EntityObserver> observers = new ArrayList<>();

    protected EntityBehavior behavior;  // comportamento delle entita
    KeyHandler keyH;
    GamePanel gp;
    int tileSize;
    public Graphics2D g2;
    // public int x, y;  // le coordinate nel mondo
    public Point imageP;  // le coordinate in alto a sinistra dell'immagine
    public double speed;
    public EntityEnum name;  // definisce il comportamento dell'entita in base al tipo
    public int uniCode;  // codice univoco dell'entita per la ricerca dentro la lista delle entita
    public BufferedImage image;
    public String direction;

    public int firePower, spriteCounter, spriteNum, maxSpriteNum, invulnerableStart, lifeNumber = 1, width, height;

    public Rectangle hitbox;
    public int hitboxX, hitboxY, hitboxWidth, hitboxHeight;
    public boolean collisionOn = false, died = false, extinguished = false, bombExitHitbox = false, invulnerable = false;

    BufferedImage up1, up2, up3, down1, down2, down3, left1, left2, left3, right1, right2, right3;
    BufferedImage ogUp1, ogUp2, ogUp3, ogDown1, ogDown2, ogDown3, ogLeft1, ogLeft2, ogLeft3, ogRight1, ogRight2, ogRight3;
    BufferedImage whiteUp1, whiteUp2, whiteUp3, whiteDown1, whiteDown2, whiteDown3, whiteLeft1, whiteLeft2, whiteLeft3, whiteRight1, whiteRight2, whiteRight3;
    BufferedImage death1, death2, death3, death4, death5, death6, death7;
    
    public Entity(GamePanel gp){
        this.gp=gp;
        this.tileSize = gp.tileSize;
        this.hitboxWidth = tileSize;
        this.hitboxHeight = tileSize;
        setEntityDefaultValues();
    }

    public void kill(){}

    public void setEntityDefaultValues(){
        this.direction = "down";
        this.died = false;
        this.extinguished = false;
        this.bombExitHitbox = false;
        this.speed = 1;

        this.spriteCounter = 0;
        this.spriteNum = 1;
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
    @Override
    public void registerObserver(EntityObserver observer) {
        observers.add(observer);
        notifyObservers();
    }

    @Override
    public void removeObserver(EntityObserver observer){
        observers.remove(observer);
    }
    
    @Override
    public void notifyObservers(){
        for (int i = 0; i < observers.size(); i++) {
            EntityObserver observer = (EntityObserver)observers.get(i);
            observer.updateEntity(this);
        }
    }

    //set Method
    public void setEntityVar(Point imageP, Rectangle hitbox, boolean invulnerable, boolean died, boolean extinguished, double speed){
        this.imageP = imageP;
        this.hitbox = hitbox; 
        this.invulnerable = invulnerable;
        this.died = died;
        this.extinguished = extinguished;
        this.speed = speed;
        notifyObservers();
    }

    public void setLocation(Point imageP, Rectangle hitbox){
        this.imageP = imageP;
        this.hitbox = hitbox;
        notifyObservers();
    }

    public void setStatus(boolean invulnerable, boolean died, boolean extinguished, double speed){
        this.invulnerable = invulnerable;
        this.died = died;
        this.extinguished = extinguished;
        this.speed = speed;
        notifyObservers();
    }

    public void setExtingused(boolean extinguished){
        this.extinguished = extinguished;
        notifyObservers();
    }

    public void powerUpHandler(Point index){};

}