
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
    boolean playerCollision = false;
    EntityMovementBehaviour stupidBehaviour = new StupidEntity();
    EntityMovementBehaviour searchBehaviour = new SearchEntity();
    //largezza e altezza dell' immagine del player
    public int score, startX, startY;

    public Enemy(GamePanel gp, int uniCode, EnemyType type){
        super(gp);
        this.uniCode = uniCode;
        
        this.movementBehaviour = type.movementBehaviour;  // inizializza movementBehaviour
        this.maxSpriteNum = type.maxSpriteNum;
        this.heartNumber = type.startingLives;
        this.score = type.startingScore;
        this.type = type.type;
        this.width = type.width*gp.scale; // larghezza dell' enemy
        this.height = type.height*gp.scale; // altezza dell' enemy 
        this.offsetX = type.offsetX*gp.scale;// dove parte hitbox dell' enemy (0 pixel a destra rispetto a dove parte l'immagine)
        this.offsetY = type.offsetY*gp.scale; // dove parte hitbox dell' enemy (12 pixel sotto rispetto a dove parte l'immagine)
        this.startX = type.startX;
        this.startY = type.startY;
        this.reverseAnimation = type.reverseAnimation;

        this.invulnerableSec = 2;
        this.tileSize = gp.getTileSize();

        this.image=null;

        this.hitboxWidth = gp.tileSize; //DIMENSIONE DI UNA TILE
        this.hitboxHeight = gp.tileSize;  //DIMENSIONE DI UNA TILE
        this.drawBehaviour = new EnemyDrawBehaviour();

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
        if(startX == -1){  // se la pos di partenza è quella di default allora la cambia
            Point avPos = findAvStartPos();  // trova una posizione disponibile sulla mappa
            // Point avPos = new Point(1, 0);
            int hitboxXAv = (avPos.x*tileSize) + (tileSize+tileSize/2);   // posizione x dell'enemy IN ALTO A SINISTRA
            int hitboxYAv = (avPos.y*tileSize) + (2*tileSize+tileSize/2);   // posizione y dell'enemy IN ALTO A SINISTRA
            hitbox = new Rectangle(hitboxXAv, hitboxYAv, hitboxWidth, hitboxHeight);
            this.imageP = new Point(hitboxXAv-offsetX, hitboxYAv-offsetY);
        }else{
            startX = startX*tileSize;
            startY = startY*tileSize;
        }
        this.collisionOn = true;
        this.speed = 1;
        this.direction = "down";
        // gp.cChecker.checkTile(this);
        notifyObservers();
    }

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

    public void kill(){
        heartNumber--;
        System.out.println(heartNumber);
        invulnerable = true;
        invulnerableTimer = 0;
        if(heartNumber == 0){
            died = true;
            System.out.println("Enemy morto");
            gp.enemyNum--;
            
            height = 32*gp.scale;
            imageP.y -= 16*gp.scale;
        }
        notifyObservers();
    }


    public void update(){  // update viene chiamato 60 volte al secondo

        if(!died){  // se non è morto
            // controlliamo cosa fare con l'oggetto

            movementBehaviour.updateMovement(this);
            //System.out.println("Collision:" + collisionOn + "  Row:" + getTileNumRow() + "  Col:" + getTileNumCol() + "  Direction:" + direction);
            changeSpriteDirection();
            // controlla se colpiamo qualche blocco
                // CONTROLLA COLLISIONE OBJECT

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
            notifyObservers();
        }
    }

    public void draw(){

        drawBehaviour.draw(this);
        
        if(!extinguished){  // se l'enemy non è esploso totalmente disegna l'immagine
            g2.drawImage(image, imageP.x, imageP.y, width, height, null);  // disegna lo sprite del personaggio (image) nella posizione x,y di grandezza tileSize
            //da eliminare
            /*
            g2.setColor(Color.BLUE);
            g2.draw(hitbox);

            g2.setColor(Color.GREEN);
            g2.drawRect(getTileX(), getTileY(), tileSize, tileSize);  */
        }
    }
}
