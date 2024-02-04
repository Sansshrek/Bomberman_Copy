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
    ArrayList<EntityObserver> observers = new ArrayList<>();  // lista degli osservatori dell'entita'

    public EntityMovementBehaviour movementBehaviour;  // interfaccia movement behaviour (Strategy Pattern)
    public EntityDrawBehaviour drawBehaviour; //interfaccia draw behaviour (Strategy Pattern)
    KeyHandler keyH;  // oggetto keyHandler per la gestione degli input da tastiera
    GamePanel gp;  // oggetto GamePanel per accedere a variabili e metodi del gioco
    int tileSize;  // dimensione di un tile del livello
    public Graphics2D g2; // oggetto grafico per disegnare l'entita'
    // public int x, y;  // le coordinate nel mondo
    public Point imageP;  // le coordinate in alto a sinistra dell'immagine
    public Point findP;  // le coordinate di un reachingPoint
    public double speed;  // la velocita' dell'entita'
    public int uniCode;  // codice univoco dell'entita per la ricerca attraverso chiave dentro la l'concurrentHashSet(Observable) delle entita 
    public BufferedImage image;  // immagine dell'entita'che viene disegnata e cambia a seconda della direzione e dello spriteCounter
    public String direction;  // direzione dell'entita'
    public ArrayList<Node> pathSearch; //percorso di ricerca utilizzato a seconda del comportamento dell'entita' (Utilizzato insieme al findP)

    //Proprietà delle entità
    public int firePower, spriteCounter, spriteNum, maxSpriteNum, invulnerableStart, invulnerableSec, invulnerableTimer = 0, lifeNumber = 1, heartNumber = 1, width, height, spriteDeathNum = 0, startDeathY;

    //Hitbox per il controllo delle collisioni 
    public Rectangle hitbox, hittableHitbox;

    //Proprietà per il controllo delle collisioni e l'inizializzazione delle immagini e delle hitbox
    public int offsetX, offsetY, hitboxWidth, hitboxHeight, mouseX, mouseY;

    //Condizioni delle entità
    public boolean collisionOn = false, died = false, extinguished = false, bombExitHitbox = false, invulnerable = false, endAnimation = false;
    public boolean checkDeathJump = false, checkDeathFall = false, reverseAnimation = false, startAttack = false, canAttack = true;
    

    //Controlli per i vari tipi di nemici
    public String type;

    //Array di immagini per le animazioni a seconda delle condizioni
    ArrayList<BufferedImage>[] imageList = new ArrayList[4];
    ArrayList<BufferedImage>[] ogImage = new ArrayList[4];
    ArrayList<BufferedImage>[] whiteImage = new ArrayList[4];
    ArrayList<BufferedImage> deathImage = new ArrayList<>();
    
    //Costruttore dell'entità
    public Entity(GamePanel gp){
        this.gp=gp;
        this.tileSize = gp.tileSize;
        this.hitboxWidth = tileSize;
        this.hitboxHeight = tileSize;
        setEntityDefaultValues();
    }

    //Metodo per quando un entità viene colpita
    public void kill(){}

    //Metodo per impostare i campi comuni a tutte le entità
    public void setEntityDefaultValues(){
        this.direction = "down";
        this.died = false;
        this.extinguished = false;
        this.bombExitHitbox = false;
        this.speed = 5;

        this.spriteCounter = 0;
        this.spriteNum = 0;
    }

    //Metodo per ottenere il centro sull'asse x preciso al pixel dell'entità
    public int getCenterX(){
        // calcola la media del punto centrale della x
        return (hitbox.x + hitbox.x + hitbox.width) / 2;
    }

    //Metodo per ottenere il centro sull'asse y preciso al pixel dell'entità
    public int getCenterY(){
        return (hitbox.y + hitbox.y + hitbox.height) / 2;
    }

    //Metodo per ottenere il centro sull'asse x approssimato al tile dell'entità
    public int getTileNumCol() {
        int adjustedX = getCenterX() - 72; // Spostamento a sinistra
        // prende il centro del player meno la distanza da dove parte a sinistra la mappa (gp.gameBorderLeftX)
        return adjustedX / gp.tileSize;
    }

    //Metodo per ottenere il centro sull'asse y approssimato al tile dell'entità
    public int getTileNumRow() {
        int adjustedY = getCenterY() - 120; // Spostamento verso l'alto
        // prende il centro del player meno la distanza da dove parte sopra la mappa (gp.gameBorderUpY)
        return adjustedY / gp.tileSize;
    }

    //Metodo per ottenere il punto in alto a sinistra della tile di appartenenza dell'entità
    public int getTileX() {
        return getTileNumCol() * gp.tileSize + 72; // TilePlayerX*48 + lo spostamento a sinistra
    }

    //Metodo per ottenere il punto in alto a sinistra della tile di appartenenza dell'entità
    public int getTileY() {
        return getTileNumRow() * gp.tileSize + 120; // TilePlayerY*48 + lo spostamento verso l'alto
    }

    //Metodo per controllare se l'entita si trova all'interno interamente a un altra hitbox
    public boolean fullyInsideTile(){  // se l'hitbox del player è completamente all'interno della hitbox del blocco in cui sta allora ritorna true
        Rectangle tileHitbox = new Rectangle(getTileX(), getTileY(), gp.tileSize, gp.tileSize);
        return tileHitbox.contains(hitbox);
    }

    //Metodo per l'aggiunta di un osservatore alla lista degli osservatori dell'entità
    @Override
    public void registerObserver(EntityObserver observer) {
        observers.add(observer);
    }

    //Metodo per la rimozione di un osservatore dalla lista degli osservatori dell'entità
    @Override
    public void removeObserver(EntityObserver observer){
        observers.remove(observer);
    }
    
    //Metodo per notificare tutti gli osservatori dell'entità
    @Override
    public void notifyObservers(){
        for (int i = 0; i < observers.size(); i++) {
            EntityObserver observer = (EntityObserver)observers.get(i);
            observer.updateEntity(this);
        }
    }
    
    //Metodo per il movimento dell'entità
    public void changeSpriteDirection(){
        if(!collisionOn){ // se si puo muovere
            switch(direction){
                case "up": 
                    imageP.y -= speed;
                    hitbox.y -= speed;
                    hittableHitbox.y -= speed;
                    break;  // la posizione Y diminuisce della velocita del player
                case "down": 
                    imageP.y += speed;
                    hitbox.y += speed;
                    hittableHitbox.y += speed;
                    break;
                case "left": 
                    imageP.x -= speed; 
                    hitbox.x -= speed;
                    hittableHitbox.x -= speed;
                    break;
                case "right": 
                    imageP.x += speed; 
                    hitbox.x += speed;
                    hittableHitbox.x += speed;
                    break;
            }
        }
    }

    //set Method
    public void setEntityVar(Point imageP, Rectangle hitbox, Rectangle hittableHitbox, boolean invulnerable, boolean died, boolean extinguished, double speed){
        this.imageP = imageP;
        this.hitbox = hitbox;
        this.hittableHitbox=hittableHitbox;
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
        if(!gp.pauseGame){
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
}