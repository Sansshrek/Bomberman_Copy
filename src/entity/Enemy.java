
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
import java.util.List;

import javax.imageio.ImageIO;

import main.GamePanel;

public class Enemy extends Entity{
    int spriteDeathNum = 1, checkTimer=0;
    boolean playerCollision = false;
    //largezza e altezza dell' immagine del player
    public final int width;
    public final int height; 

    public Enemy(GamePanel gp, int uniCode, int maxSpriteNum){
        super(gp);
        this.uniCode = uniCode;
        this.maxSpriteNum = maxSpriteNum;

        this.speed = 1;
        this.direction = "down";
        this.tileSize = gp.getTileSize();
        //coordinate nel mondo
        // this.x = x;
        // this.y = y;

        this.image=null;

        //largezza e altezza dell' immagine dell' enemy
        this.width = 16*gp.scale; // larghezza dell' enemy
        this.height = 29*gp.scale; // altezza dell' enemy 

        //codinate top left dell' hitbox
        this.hitboxX = 0*gp.scale;// dove parte hitbox dell' enemy (0 pixel a destra rispetto a dove parte l'immagine)
        this.hitboxY = 8*gp.scale; // dove parte hitbox dell' enemy (12 pixel sotto rispetto a dove parte l'immagine)
        
        //larghezza e altezza dell' hitbox
        // this.hitboxWidth = 15*gp.scale;// larghezza dell'hitbox dell' enemy
        // this.hitboxHeight = 15*gp.scale;// altezza dell'hitbox dell' enemy
        this.hitboxWidth = gp.tileSize;
        this.hitboxHeight = gp.tileSize;
        this.behavior = new StupidEntity();  // inizializza behaviour

        setEnemyDefaultValues();
        getEnemyImage();
    }


    @Override
    public void notifyObservers(){
        for (int i = 0; i < observers.size(); i++) {
            EntityObserver observer = (EntityObserver)observers.get(i);
            observer.updateEntity(this);
            if(extinguished){  // se muore rimuoviamo l'entita
                observer.removeEntity(uniCode);
            }
        }
    }

	public void setEnemyDefaultValues(){
        Point avPos = findAvStartPos();  // trova una posizione disponibile sulla mappa
        int x = (avPos.x*tileSize) + (tileSize+tileSize/2);   // posizione x dell'enemy IN ALTO A SINISTRA
        int y = (avPos.y*tileSize) + 2*tileSize;   // posizione y dell'enemy IN ALTO A SINISTRA
        this.imageP = new Point(x, y);
        hitbox = new Rectangle(hitboxX + imageP.x, hitboxY + imageP.y, hitboxWidth, hitboxHeight);
        this.speed = 1;
        this.direction = "down";
        notifyObservers();
    }

    public Point findAvStartPos(){
        ArrayList<Point> avPos = new ArrayList<>();  // array di posizioni disponibili
        for(int row=0; row<gp.maxGameRow; row++){ 
            for(int col=0; col<gp.maxGameCol; col++){
                if(gp.obj[row][col] == null && gp.tileM.houseTileNum[row][col] != 3 && row+col != 0 && row+col != 1 && row+col != 2 && row+col != 3){  // se non c'è una casa e non c'è un blocco o sono le pos del player
                    avPos.add(new Point(col, row));  // ritorno x:col e y:row
                }
            }
        }
        Collections.shuffle(avPos);  // randomizzo array delle posizioni disponibili
        return avPos.get(0);  // ritorno la prima pos disponibile
    }

    public void getEnemyImage(){
        try{  // prova a caricare le immagini nelle variabili
            for(int dir=0; dir<4; dir++){
                imageList[dir] = new ArrayList<>();
                String directionImage = "";
                if(dir == 0) {directionImage = "up";}
                else if(dir == 1) {directionImage = "down";}
                else if(dir == 2) {directionImage = "left";}
                else {directionImage = "right";}
                for(int sprite=1; sprite<=maxSpriteNum; sprite++){  // per quante sprite ci stanno in una direzione
                    imageList[dir].add(ImageIO.read(getClass().getResourceAsStream("../res/enemies/walking Enemy/"+directionImage+String.valueOf(sprite)+".png")));
                }
            }
            
            death1 = ImageIO.read(getClass().getResourceAsStream("../res/enemies/Enemy fire/explosion1.png"));
            death2 = ImageIO.read(getClass().getResourceAsStream("../res/enemies/Enemy fire/explosion2.png"));
            death3 = ImageIO.read(getClass().getResourceAsStream("../res/enemies/Enemy fire/explosion3.png"));
            death4 = ImageIO.read(getClass().getResourceAsStream("../res/enemies/Enemy fire/explosion4.png"));
            death5 = ImageIO.read(getClass().getResourceAsStream("../res/enemies/Enemy fire/explosion5.png"));
            death6 = ImageIO.read(getClass().getResourceAsStream("../res/enemies/Enemy fire/explosion6.png"));
            death7 = ImageIO.read(getClass().getResourceAsStream("../res/enemies/Enemy fire/explosion7.png"));
            //explosion8 = ImageIO.read(getClass().getResourceAsStream("../res/player/Enemy/Enemy fire/explosion_08.png"));
            notifyObservers();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void kill(){
        System.out.println("Enemy morto");
        extinguished = true;
        notifyObservers();
    }

    public void update(){  // update viene chiamato 60 volte al secondo
            // Check for collisions

        if(!died){  // se non è morto
            // controlliamo cosa fare con l'oggetto
            // If a collision occurs, check for collisions in all directions and choose a new direction
            if (collisionOn) {  // se colpisce qualcosa
                behavior.update(this);
            }
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
            // controlla se colpiamo qualche blocco
                // CONTROLLA COLLISIONE OBJECT

            spriteCounter++;
            if(spriteCounter > 15){  // ogni 15/60 volte al secondo 
                spriteNum++;
                if(spriteNum == maxSpriteNum)
                    spriteNum = 0;
                spriteCounter = 0;  // e resetta  il counter
                // System.out.println(x+" "+y);  // da eliminare
                // System.out.println("Enemy tile:"+getEnemyTileX()+ " "+getEnemyTileY());
            }
            notifyObservers();
        }
    }

    public void draw(){
        image = null;
        if(!died){  // se ancora non è stato colpito dalla bomba allora disegna l'enemy normale
            switch(direction){  // in base alla direzione, la variabile image prende il valore dell'immagine inserita
                case "up":
                    image = imageList[0].get(spriteNum);
                break;
                case "down":
                    image = imageList[1].get(spriteNum);
                break;
                case "left":
                    image = imageList[2].get(spriteNum);
                break;
                case "right":
                    image = imageList[3].get(spriteNum);
                break;
            }
            notifyObservers();
        }else{  // altrimenti se l'enemy è stato colpito dalla bomba allora disegna l'esplosione
            spriteCounter++;
            if(spriteCounter > 10){
                spriteDeathNum++;
                spriteCounter = 0;
            }
            switch(spriteDeathNum){
                case 1:
                    image = death1;
                break;
                case 2:
                    image = death2;
                break;
                case 3:
                    image = death3;
                break;
                case 4:
                    image = death4;
                break;
                case 5:
                    image = death5;
                break;
                case 6:
                    image = death6;
                break;
                case 7:
                    image = death6;
                break;
                case 8:
                    image = death7;
                break;
                default:{
                    kill();  // altrimenti uccidi l'enemy
                }
            }
            notifyObservers();
        }
        if(!extinguished){  // se l'enemy non è esploso totalmente disegna l'immagine
            g2.drawImage(image, imageP.x, imageP.y, width, height, null);  // disegna lo sprite del personaggio (image) nella posizione x,y di grandezza tileSize
            //da eliminare
            g2.setColor(Color.BLUE);
            g2.draw(hitbox);  

            g2.setColor(Color.GREEN);
            g2.drawRect(getTileX(), getTileY(), tileSize, tileSize);
        }
    }
}
