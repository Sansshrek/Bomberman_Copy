package entity;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;
import objects.Bomb;

public class Player extends Entity{
    // public ArrayList<SuperObject> bombObj = new ArrayList<>();
    
    //larghezza e altezza dell' hitbox
    public int bombNumber, firePower, gamesWon, gamesLost, gamesPlayed;
    int spriteDeathCounter = 0; 
    public String nickname = "player";
    public String avatarColor = "white";

    public Player(GamePanel gp){
        super(gp);
        this.keyH = KeyHandler.getInstance();
        int scale = gp.getScale();
        this.tileSize = gp.getTileSize();
        this.maxSpriteNum = 3;
        this.invulnerableSec = 12;
        this.type = "player";

        this.image=null;

        //largezza e altezza dell' immagine del player
        this.hittableWidth = 16*scale; // larghezza del player
        this.hittableHeight = 29*scale; // altezza del player 

        //codinate top left dell' hitbox
        this.offsetImageX = 2*scale;// dove parte hitbox del player (2 pixel a destra rispetto a dove parte l'immagine)
        this.offsetImageY = 10*scale; // dove parte hitbox del player (12 pixel sotto rispetto a dove parte l'immagine)
        
        //larghezza e altezza dell' hitbox
        this.hitboxWidth = 12*scale;// larghezza dell'hitbox del player
        this.hitboxHeight = 12*scale;// altezza dell'hitbox del player

        this.movementBehaviour = new BasePlayerBehaviour();
        this.drawBehaviour = new PlayerDrawBehaviour();

        gp.bombH.addBombNumber();  // aggiunge la prima bomba al player
        setPlayerDefaultValues();
        getPlayerImage();
        notifyObservers();
    }

	public void setPlayerDefaultValues(){  // setta i valori iniziali di quando inizia il gioco
        speed = 2;
        firePower = 1;
        lifeNumber = 5;
        score = 0;
        gp.bombH.setBombNumber(1);  // resetta il numero di bombe del player
        resetPlayerGameValue();
    }

    public void resetPlayerGameValue(){  // cambia i valori quando muore 
        int hitboxX = (0*tileSize) + (tileSize+tileSize/2) + offsetImageX;   // posizione x del player IN ALTO A SINISTRA
        int hitboxY = (0*tileSize) + 2*tileSize + offsetImageY;    // posizione y del player IN ALTO A SINISTRA
        this.hitbox = new Rectangle(hitboxX, hitboxY, hitboxWidth, hitboxHeight);
        this.imageP = new Point(hitboxX-offsetImageX, hitboxY-offsetImageY);
        this.hittableHitbox = new Rectangle(hitboxX, hitboxY, hitboxWidth, hitboxHeight);
        direction = "down";
        died = false;
        extinguished = false;
        checkDeathJump = false;
        checkDeathFall = false;
        spriteDeathCounter = 0;
        spriteDeathNum = 0;
        spriteNum = 0;
        heartNumber = 1;
        mouseX = 0;
        mouseY = 0;
        gp.hud.resetTimer();
        resetInvincibility();
        notifyObservers();
    }

    public void kill(){
        if(!invulnerable){
            heartNumber--;  // perde una vita del player stesso (non perde il numero di player)
            invulnerable = true;  // diventa invulnerabile
            if(heartNumber == 0){  // se perde tutte le vite del player
                if(!died){  // se non è morto
                    gp.playSfx(6);  // sound morte player
                    startDeathY = imageP.y;  // inizia lo sprite della morte
                    died = true;  // imposta la morte a true
                }
            }
        }
        if(extinguished){  // quando è completamente morto resetta i valori
            lifeNumber -= 1;  // diminuisce di 1 la vita
            resetPlayerGameValue();
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
                    BufferedImage ogImg = ImageIO.read(getClass().getResourceAsStream("../res/player/Player Walking/walking "+avatarColor+"/"+directionImage+String.valueOf(sprite)+".png"));
                    imageList[dir].add(ogImg);
                    ogImage[dir].add(ogImg);
                    whiteImage[dir].add(ImageIO.read(getClass().getResourceAsStream("../res/player/walking Invincible/"+directionImage+String.valueOf(sprite)+".png")));
                }
            }
            deathImage = new ArrayList<>();
            for(int sprite=1; sprite<=7; sprite++){
                deathImage.add(ImageIO.read(getClass().getResourceAsStream("../res/player/Player Death/"+avatarColor+"/death"+String.valueOf(sprite)+".png")));
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        notifyObservers();
    }

    public void updateMousePosition(int mouseX, int mouseY){
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    public void createBomb(){
        gp.bombH.createBomb(getTileX(), getTileY(), getTileNumRow(), getTileNumCol(), firePower);
    }

    public void update(){  // update viene chiamato 60 volte al secondo
        if(extinguished)
            kill();
        if(!died){ // se non è morto allora puo eseguire l'update
            if(lifeNumber == 0){
                gp.playerDeath();
            }

            movementBehaviour.updateMovement(this);

            if(keyH.resetPressed){  // tasto R
                gp.resetLevel();
                keyH.resetPressed = false;
            }
        }
        notifyObservers();
    }

    public void resetInvincibility(){
        invulnerableTimer = 0;
        invulnerable = true;
    }

    public void powerUpHandler(Point index){
        if(index.x != 999 && gp.obj[index.y][index.x].name != "block" && gp.obj[index.y][index.x] != null && !(gp.obj[index.y][index.x] instanceof Bomb)){  // se non è il valore default o un blocco
            String objName = gp.obj[index.y][index.x].name;
            
            if(gp.obj[index.y][index.x].name != "nothing" && gp.obj[index.y][index.x].name != "exit")  // se non è un oggetto nullo o l'uscita
                gp.playSfx(7);  // suono powerUp ottenuto
            if(gp.obj[index.y][index.x].name != "exit")  // se non è l'exit
                gp.obj[index.y][index.x] = null;  // allora lo elimina
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
                break;
                case "life":
                    score += 50;
                    if(lifeNumber < 9) // il massimo di vite che puo avere è nove
                        lifeNumber += 1;
                break;
                case "death":
                    lifeNumber -= 1;
                break;
                case "heart":
                    score += 800;
                    heartNumber += 1;
                break;
                case "armor":
                    score += 800;
                    resetInvincibility();
                break;
                case "time":
                    score += 800;
                    gp.hud.resetTimer();
                break;
                case "blockCross":
                    score += 50;
                    blockCross = true;
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
                    if(getTileNumCol() == gp.obj[index.y][index.x].tileCol && getTileNumRow() == gp.obj[index.y][index.x].tileRow)  // se entra a meta corpo nel tile dell'uscita
                        gp.nextLevel();  // va al prossimo livello
                break;
                case "nothing":
                break;
            }
            notifyObservers();
        }
    }
}