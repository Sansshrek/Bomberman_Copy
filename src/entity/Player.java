package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;
import objects.Bomb;
import objects.BombHandler;

public class Player extends Entity{
    // public ArrayList<SuperObject> bombObj = new ArrayList<>();

    //largezza e altezza dell' immagine del player
    public final int width;
    public final int height; 
    
    //larghezza e altezza dell' hitbox
    public int bombNumber, lifeNumber, score, heartNumber;
    public boolean pauseGame;
    int spriteDeathCounter = 0;

    public Player(GamePanel gp, KeyHandler keyH){
        super(gp);
        this.keyH = keyH;
        int scale = gp.getScale();
        this.tileSize = gp.getTileSize();
        maxSpriteNum = 4;

        this.image=null;

        //largezza e altezza dell' immagine del player
        this.width = 16*scale; // larghezza del player
        this.height = 29*scale; // altezza del player 

        //codinate top left dell' hitbox
        this.hitboxX = 2*scale;// dove parte hitbox del player (2 pixel a destra rispetto a dove parte l'immagine)
        this.hitboxY = 12*scale; // dove parte hitbox del player (12 pixel sotto rispetto a dove parte l'immagine)
        
        //larghezza e altezza dell' hitbox
        this.hitboxWidth = 12*scale;// larghezza dell'hitbox del player
        this.hitboxHeight = 12*scale;// altezza dell'hitbox del player

        this.movementBehaviour = new BasePlayerBehaviour();
        this.drawBehaviour = new PlayerDrawBehaviour();

        System.out.println("Caricando il player");  // da eliminare
        gp.bombH.addBombNumber();  // aggiunge la prima bomba al player
        gp.bombH.addBombNumber();  // aggiunge la prima bomba al player
        setPlayerDefaultValues();
        getPlayerImage();
        notifyObservers();

    }
    
    @Override
    public void notifyObservers(){
        for (int i = 0; i < observers.size(); i++) {
            EntityObserver observer = (EntityObserver)observers.get(i);
            observer.updateEntity(this);
        }
    }
/* 
    public void setEntityDefaultValues(){
        setPlayerDefaultValues();
    }
    */
	public void setPlayerDefaultValues(){
        int x=0;
        int y=0;
        x = (x*tileSize) + (tileSize+tileSize/2);   // posizione x del player IN ALTO A SINISTRA
        y = (y*tileSize) + 2*tileSize;    // posizione y del player IN ALTO A SINISTRA
        this.imageP = new Point(x, y);
        speed = 2;
        direction = "down";
        firePower = 1;
        lifeNumber = 5;
        heartNumber = 1;
        score = 0;
        invulnerable = true;
        died = false;
        extinguished = false;
        checkDeathJump = false;
        checkDeathFall = false;
        spriteDeathCounter = 0;
        spriteNum = 0;
        this.hitbox = new Rectangle(hitboxX+imageP.x, hitboxY+imageP.y, hitboxWidth, hitboxHeight);
        //setEntityVar(imageP, hitbox, invulnerable, died, extinguished, speed);
        notifyObservers();
    }

    public void kill(){
        heartNumber -= 1;  // perde una vita del player stesso (non perde il numero di player)
        invulnerable = true;  // diventa invulnerabile
        if(heartNumber == 0){  // se perde tutte le vite del player
            if(!died){  // se non è morto
                startDeathY = imageP.y;  // inizia lo sprite della morte
                died = true;  // imposta la morte a true
            }
        }
        if(extinguished){  // quando è completamente morto resetta i valori
            int x=0;
            int y=0;
            x = (0*tileSize) + (tileSize+tileSize/2);   // posizione x del player IN ALTO A SINISTRA
            y = (y*tileSize) + 2*tileSize;    // posizione y del player IN ALTO A SINISTRA
            this.imageP = new Point(x, y);
            direction = "down";
            invulnerable = true;
            died = false;
            extinguished = false;
            checkDeathJump = false;
            checkDeathFall = false;
            spriteDeathCounter = 0;
            spriteNum = 0;
            lifeNumber -= 1;  // diminuisce di 1 la vita
            heartNumber = 1;
            this.hitbox = new Rectangle(hitboxX+imageP.x, hitboxY+imageP.y, hitboxWidth, hitboxHeight);
            gp.hud.resetTimer();
            //setEntityVar(imageP, hitbox, invulnerable, died, extinguished, speed);
        }
        notifyObservers();
    }

    public void getPlayerImage(){
        try{  // prova a caricare le immagini nelle variabili
            for(int dir=0; dir<4; dir++){
                imageList[dir] = new ArrayList<>();
                ogImage[dir] = new ArrayList<>();
                whiteImage[dir] = new ArrayList<>();
                String directionImage = "";
                if(dir == 0) {directionImage = "up";}
                else if(dir == 1) {directionImage = "down";}
                else if(dir == 2) {directionImage = "left";}
                else {directionImage = "right";}
                for(int sprite=1; sprite<=maxSpriteNum; sprite++){  // per quante sprite ci stanno in una direzione
                    BufferedImage ogImg = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Original/"+directionImage+String.valueOf(sprite)+".png"));
                    imageList[dir].add(ogImg);
                    ogImage[dir].add(ogImg);
                    whiteImage[dir].add(ImageIO.read(getClass().getResourceAsStream("../res/player/walking Invincible/"+directionImage+String.valueOf(sprite)+".png")));
                }
            }
            for(int sprite=1; sprite<=7; sprite++){
                deathImage.add(ImageIO.read(getClass().getResourceAsStream("../res/player/Player Death/death"+String.valueOf(sprite)+".png")));
            }
            
        }catch(IOException e){
            e.printStackTrace();
        }
        notifyObservers();
    }

    public void update(){  // update viene chiamato 60 volte al secondo
        if(extinguished)
            kill();
        if(!died){ // se non è morto allora puo eseguire l'update
            if(lifeNumber == 0){
                gp.resetLevel();
            }

            movementBehaviour.updateMovement(this);

            if(keyH.statsPressed){ // da eliminare
                System.out.println("\nFire "+firePower);
                System.out.println("Speed "+speed);
                System.out.println("BombNumber "+bombNumber);
                System.out.println("Life "+lifeNumber);
                keyH.statsPressed = false;
            }
            if(keyH.firePressed){
                firePower++;
                System.out.println("Range fuoco aumentato");
                keyH.firePressed = false;
            }
            if(keyH.resetPressed){
                gp.resetLevel();
                keyH.resetPressed = false;
            }
        }
        notifyObservers();
    }

    public void updateKey(){
        if(keyH.pausePressed){ //se viene premuto Enter
            if(pauseGame){  // se il gioco è gia in pausa fa ricominciare il gioco
                pauseGame = false;
            }else{  // se il gioco non è in pausa allora lo ferma
                pauseGame = true;
            }
            keyH.pausePressed = false;
        }
    }
/* 
    public void playerDeathJump(){
        if(died){
            for(int i=0; i<gp.tileSize*2; i++){
                imageP.y -= 1;
                hitbox.y -= 1;
                
            }
            for(int i=0; i<gp.tileSize*2; i++){
                imageP.y += 1;
                hitbox.y += 1;
                g2.drawImage(death1, imageP.x, imageP.y, gp.player.width, gp.player.height, null);
            }
            extinguished = true;
        }
    }
*/
    public void powerUpHandler(Point index){
        if(index.x != 999 && gp.obj[index.y][index.x].name != "block" && gp.obj[index.y][index.x] != null && !(gp.obj[index.y][index.x] instanceof Bomb)){  // se non è il valore default o un blocco
            String objName = gp.obj[index.y][index.x].name;
            gp.obj[index.y][index.x] = null;
            switch(objName){
                // powerUp
                case "fire":
                    score += 10;
                    firePower += 1;
                break;
                case "bomb":
                    score += 10;
                    gp.bombH.addBombNumber();
                break;
                case "skate":
                    score += 10;
                    speed += 1;
                    //setStatus(invulnerable, died, extinguished, speed);
                break;
                case "life":
                    score += 50;
                    lifeNumber += 1;
                break;
                case "death":
                    lifeNumber -= 1;
                break;
                case "heart":
                    score += 800;
                    heartNumber += 1;
                break;
                // cibi
                case "onigiri":
                    score += 150;
                break;
                case "apple":
                    score += 300;
                break;
                case "ice_cream":
                    score += 500;
                break;
                case "cake":
                    score += 1000;
                break;
                case "exit":
                    gp.nextLevel();
                break;
                case "nothing":
                break;
            }
            notifyObservers();
        }
    }
    

    public void draw(){
        
        drawBehaviour.draw(this);

        

    }
}