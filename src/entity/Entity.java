package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.awt.Graphics2D;
import java.awt.Point;
import main.KeyHandler;

import main.GamePanel;

public class Entity implements EntityObservable{
    ArrayList<EntityObserver> observers = new ArrayList<>();

    public EntityMovementBehaviour movementBehaviour;  // interfaccia movement behaviour
    public EntityDrawBehaviour drawBehaviour; //interfaccia draw behaviour
    KeyHandler keyH;
    GamePanel gp;
    int tileSize;
    public Graphics2D g2;
    // public int x, y;  // le coordinate nel mondo
    public Point imageP;  // le coordinate in alto a sinistra dell'immagine
    public Point findP;
    public double speed;
    public EntityEnum name;  // definisce il comportamento dell'entita in base al tipo
    public int uniCode;  // codice univoco dell'entita per la ricerca dentro la lista delle entita
    public BufferedImage image;
    public String direction;
    public ArrayList<Node> pathSearch; 

    public int firePower, spriteCounter, spriteNum, maxSpriteNum, invulnerableStart, invulnerableSec, invulnerableTimer = 0, lifeNumber = 1, heartNumber = 1, width, height, spriteDeathNum = 0, startDeathY;

    public Rectangle hitbox;
    public int offsetX, offsetY, hitboxWidth, hitboxHeight;
    public boolean collisionOn = false, died = false, extinguished = false, bombExitHitbox = false, invulnerable = false, endAnimation = false;
    public boolean checkDeathJump = false, checkDeathFall = false;
    public String type;

    ArrayList<BufferedImage>[] imageList = new ArrayList[4];
    ArrayList<BufferedImage>[] ogImage = new ArrayList[4];
    ArrayList<BufferedImage>[] whiteImage = new ArrayList[4];
    ArrayList<BufferedImage> deathImage = new ArrayList<>();
    
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
        this.spriteNum = 0;
    }

    public int getCenterX(){
        // calcola la media del punto centrale della x
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
    
    public void changeSpriteDirection(){
        if(!collisionOn){ // se si puo muovere
            switch(direction){
                case "up": 
                    imageP.y -= speed;
                    hitbox.y -= speed;
                    break;  // la posizione Y diminuisce della velocita del player
                case "down": 
                    imageP.y += speed;
                    hitbox.y += speed;
                    break;
                case "left": 
                    imageP.x -= speed; 
                    hitbox.x -= speed;
                    break;
                case "right": 
                    imageP.x += speed; 
                    hitbox.x += speed;
                    break;
            }
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

    public void invincibleCheck(){
        int d, r;
        if(invulnerable){  // se il player è invulnerabile 
            invulnerableTimer++;  // aumenta il timer per l'invulnerabilità
            if(invulnerableTimer>=invulnerableSec*60-2*60){
                d=6;
                r=3;
            }else if (invulnerableTimer>=invulnerableSec*60-4*60){
                d=15;
                r=9;
            }else{  
                d=30;
                r=15;
            }
            if (invulnerableTimer%d==r){  // divide per 30 (quindi la metà di 60 FPS del gioco, cioe ogni mezzo secondo) e per ogni quarto di secondo cambia lo sprite 
                for(int dir=0; dir<4; dir++){
                    imageList[dir] = whiteImage[dir];  // sostituiamo le arraylist della lista image originale
                }
            }
            if (invulnerableTimer%d==0){
                for(int dir=0; dir<4; dir++){
                    imageList[dir] = ogImage[dir];
                }
            }
            // System.out.println(invulnerableTimer);
            if(invulnerableTimer == invulnerableSec*60){  // quando finisce il timer
                System.out.println("Finita invulnerabilita");
                invulnerable = false;  // finisce l'invulnerabilità
                // resetta le immagini originali per sicurezza
                for(int dir=0; dir<4; dir++){
                    imageList[dir] = ogImage[dir];
                }
                invulnerableTimer = 0;  // resetta il timer
                //setStatus(invulnerable, died, extinguished, speed);
            }
            notifyObservers();
        }
        
    }
}