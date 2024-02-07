
package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.imageio.ImageIO;

import main.GamePanel;

public class Enemy extends Entity{
    boolean playerCollision = false;  // se c'è una collisione con il player
    boolean checkSoundBoss = false; // controllo del suono dello stage clear per il boss quando muore
    //largezza e altezza dell' immagine del player
    public int startX, startY; // Aggiungiamo delle variabili specifiche per la scelta della posizione di partenza dell'enemy 
    //e per la grandezza variabile dell'immagine e per il rilascio di punti al player

    public Enemy(GamePanel gp, int uniCode, EnemyType type){
        super(gp);  // chiama il costruttore della classe madre
        this.uniCode = uniCode;  // inizializza uniCode
        
        this.movementBehaviour = type.movementBehaviour;  // inizializza movementBehaviour
        this.drawBehaviour = type.drawBehaviour;  // inizializza drawBehaviour
        this.maxSpriteNum = type.maxSpriteNum;  // inizializza maxSpriteNum
        this.heartNumber = type.startingLives;  // inizializza heartNumber
        this.speed = type.speed;  // inizializza speed
        this.score = type.startingScore;
        this.type = type.type;
        this.hittableWidth = type.hittableWidth*gp.scale; // larghezza dell' enemy
        this.hittableHeight = type.hittableHeight*gp.scale; // altezza dell' enemy 
        this.imageWidth = type.imageWidth*gp.scale;
        this.imageHeight = type.imageHeight*gp.scale;
        this.offsetImageX = type.offsetImageX*gp.scale;// dove parte hitbox dell' enemy 
        this.offsetImageY = type.offsetImageY*gp.scale; // dove parte hitbox dell' enemy 
        this.offsetHittableX = type.offsetHittableX*gp.scale;// dove parte hitbox dell' enemy 
        this.offsetHittableY = type.offsetHittableY*gp.scale; // dove parte hitbox dell' enemy 
        this.startX = type.startCol;
        this.startY = type.startRow;
        this.reverseAnimation = type.reverseAnimation;
        this.blockCross = type.blockCross;

        this.invulnerableSec = 2;
        this.tileSize = gp.getTileSize();

        this.image=null;

        this.hitboxWidth = gp.tileSize; //DIMENSIONE DI UNA TILE
        this.hitboxHeight = gp.tileSize;  //DIMENSIONE DI UNA TILE
        // this.drawBehaviour = new EnemyDrawBehaviour();

        setEnemyDefaultValues();
        getEnemyImage();
    }


    //Metodo di notifica ed aggiornamento degli observer
    @Override
    public void notifyObservers(){
        for (int i = 0; i < observers.size(); i++) {
            EntityObserver observer = (EntityObserver)observers.get(i);
            observer.updateEntities(this);
            if(extinguished){  // se muore rimuoviamo l'entita
                observer.removeEntities(uniCode);
            }
        }
    }

    //Metodo di impostazioni delle variabili di default dell'enemy che utilizza o la posizione di partenza impostata (!-1) o una posizione disponibile
	public void setEnemyDefaultValues(){
        int hitboxXAv, hitboxYAv;
        if(startX == -1){  // se la pos di partenza è quella di default allora la cambia
            Point avPos = findAvStartPos();  // trova una posizione disponibile sulla mappa
            // Point avPos = new Point(1, 0);
            hitboxXAv = (avPos.x*tileSize) + (tileSize+tileSize/2);   // posizione x dell'enemy IN ALTO A SINISTRA
            hitboxYAv = (avPos.y*tileSize) + (2*tileSize+tileSize/2);   // posizione y dell'enemy IN ALTO A SINISTRA
            
        }else{
            hitboxXAv = (startX*tileSize) + (tileSize+tileSize/2);
            hitboxYAv = (startY*tileSize) + (2*tileSize+tileSize/2);
        }
        this.hitbox = new Rectangle(hitboxXAv, hitboxYAv, hitboxWidth, hitboxHeight);
        this.imageP = new Point(hitboxXAv-offsetImageX, hitboxYAv-offsetImageY);
        this.hittableHitbox = new Rectangle(imageP.x+offsetHittableX, imageP.y+offsetHittableY, hittableWidth, hittableHeight);
        this.collisionOn = true;
        this.direction = "down";
        // gp.cChecker.checkTile(this);
        notifyObservers();
    }

    //Metodo per trovare una posizione disponibile sulla mappa
    public Point findAvStartPos(){
        ArrayList<Point> avPos = new ArrayList<>();  // array di posizioni disponibili
        for(int row=0; row<gp.maxGameRow; row++){ 
            for(int col=0; col<gp.maxGameCol; col++){
                if(gp.obj[row][col] == null && gp.tileM.houseTileNum[row][col] != 3 && gp.tileM.houseTileNum[row][col] != -1 && row+col != 0 && row+col != 1 && row+col != 2 && row+col != 3){  // se non c'è una casa e non c'è un blocco o sono le pos del player
                    avPos.add(new Point(col, row));  // ritorno x:col e y:row
                }
            }
        }
        Collections.shuffle(avPos);  // randomizzo array delle posizioni disponibili
        return avPos.get(0);  // ritorno la prima pos disponibile
    }

    //Metodo per ottenere l'immagine dell'enemy
    public void getEnemyImage(){
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
                    BufferedImage ogImg = ImageIO.read(getClass().getResourceAsStream("../res/enemies/"+type+"/"+directionImage+String.valueOf(sprite)+".png"));
                    imageList[dir].add(ogImg);
                    ogImage[dir].add(ogImg);
                    whiteImage[dir].add(ImageIO.read(getClass().getResourceAsStream("../res/enemies/invulnerable/"+type+"/"+directionImage+String.valueOf(sprite)+".png")));
                }
            }
            for(int sprite=1; sprite<=7; sprite++){
                deathImage.add(ImageIO.read(getClass().getResourceAsStream("../res/enemies/enemy_fire/explosion"+String.valueOf(sprite)+".png")));
            }

            notifyObservers();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    //Metodo per quando un enemy viene colpita
    public void kill(){
        if(!invulnerable && !died){
            heartNumber--;
            System.out.println(heartNumber);
            invulnerable = true;
            invulnerableTimer = 0;
            if(heartNumber == 0){
                gp.playSfx(8); // sound enemy muore
                died = true;
                System.out.println("Enemy morto");
                gp.player.score += score;
                gp.enemyNum--;
                if(type != "knight" && type != "clown"){
                    hittableHeight = 32*gp.scale;
                    imageP.y -= 16*gp.scale;
                }
            }
            notifyObservers();
        }
    }

    //Metodo per aggiornare e spostare l'enemy
    public void update(){  // update viene chiamato 60 volte al secondo
        if(type == "clown" || type == "knight"){  // anche se il clown o knight è morto fa l'update dei proiettili
            projectileHandler.updateProjectiles();  // aggiorna i proiettili
        }
        /*
        if((died || gp.keyH.nextLevelPressed) && !checkSoundBoss && (type == "knight" || type == "clown")){  // se ancora non ha fatto partire la musichetta di stage clear
            gp.stopMusic();  // ferma la musica di gioco
            gp.playSfx(2);  // sound stage clear
            checkSoundBoss = true;  // imposta a true il controllo del suono cosi che viene chiamato una sola volta
        } */

        if(!died){  // se non è morto
            // controlliamo cosa fare con l'oggetto

            movementBehaviour.updateMovement(this);
            //System.out.println("Collision:" + collisionOn + "  Row:" + getTileNumRow() + "  Col:" + getTileNumCol() + "  Direction:" + direction);
           
            changeSpriteDirection();
            // controlla se colpiamo qualche blocco
                // CONTROLLA COLLISIONE OBJECT

            if(type != "knight" && type != "clown"){  // il knight non ha bisogno di modificare le sprite
                spriteCounter++;
                if(spriteCounter > 15){  // ogni 15/60 volte al secondo 
                    if(reverseAnimation){  // se il tipo di nemico ha gli sprite che tornano al contrario quando finisce l'animazione
                        if(endAnimation)  // se è finita l'animazione
                            spriteNum--;  // ristampa gli sprite in reverse
                        else
                            spriteNum++;  // altrimenti li stampa in modo normale

                        if(spriteNum == maxSpriteNum-1)
                            endAnimation = true;
                        else if(spriteNum == 0)
                            endAnimation = false;
                    }else{
                        spriteNum++;
                        if(spriteNum == maxSpriteNum)
                            spriteNum = 0;
                    }
                    spriteCounter = 0;  // e resetta il counter
                }
            }
            notifyObservers();
        }
    }
}
