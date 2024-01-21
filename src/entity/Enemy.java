
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
    // public ArrayList<SuperObject> bombObj = new ArrayList<>();

    public BufferedImage up1, up2, up3, down1, down2, down3, left1, left2, left3, right1, right2, right3;
    public BufferedImage explosion1, explosion2, explosion3, explosion4, explosion5, explosion6, explosion7, explosion8;
    int spriteDeathNum = 1, checkTimer=0;
    boolean playerCollision = false;
    //largezza e altezza dell' immagine del player
    public final int width;
    public final int height; 

    public Enemy(GamePanel gp){
        super(gp);

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
        setEnemyDefaultValues();
        getEnemyImage();
    }


    @Override
    public void notifyObservers(){
        for (int i = 0; i < observers.size(); i++) {
            EntityObserver observer = (EntityObserver)observers.get(i);
            observer.updateEntity(this);
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
            up1 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Enemy/up01.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Enemy/up02.png"));
            up3 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Enemy/up03.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Enemy/down01.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Enemy/down02.png"));
            down3 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Enemy/down03.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Enemy/left01.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Enemy/left02.png"));
            left3 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Enemy/left03.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Enemy/right01.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Enemy/right02.png"));
            right3 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Enemy/right03.png"));
            
            death1 = ImageIO.read(getClass().getResourceAsStream("../res/player/Enemy/Enemy fire/explosion_01.png"));
            death2 = ImageIO.read(getClass().getResourceAsStream("../res/player/Enemy/Enemy fire/explosion_02.png"));
            death3 = ImageIO.read(getClass().getResourceAsStream("../res/player/Enemy/Enemy fire/explosion_03.png"));
            death4 = ImageIO.read(getClass().getResourceAsStream("../res/player/Enemy/Enemy fire/explosion_04.png"));
            death5 = ImageIO.read(getClass().getResourceAsStream("../res/player/Enemy/Enemy fire/explosion_05.png"));
            death6 = ImageIO.read(getClass().getResourceAsStream("../res/player/Enemy/Enemy fire/explosion_06.png"));
            death7 = ImageIO.read(getClass().getResourceAsStream("../res/player/Enemy/Enemy fire/explosion_07.png"));
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
            collisionOn = false;
            gp.cChecker.checkBomb(this);
            gp.cChecker.checkTile(this);
            gp.cChecker.checkPlayerCollision(this, gp.player);
            Point objPoint = gp.cChecker.checkObj(this, false, g2);
            // controlliamo cosa fare con l'oggetto
            // If a collision occurs, check for collisions in all directions and choose a new direction
            if (collisionOn) {  // se colpisce qualcosa
                List<String> directions = Arrays.asList("up", "down", "left", "right");
                Collections.shuffle(directions);
                for (String dir : directions) {  // itera le posizioni disponibili in cui puo andare
                    direction = dir;
                    gp.cChecker.checkTile(this);  // controlla se puo andare in quella posizione
                    if(!collisionOn) {
                        break;
                    }
                }
            }
            // controlla se colpiamo qualche blocco
                // CONTROLLA COLLISIONE OBJECT

            if(!collisionOn){ // si puo muovere
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
                notifyObservers();
            }
            spriteCounter++;
            if(spriteCounter > 15){  // ogni 15/60 volte al secondo 
                switch(spriteNum){
                    case 1:  // dalla 1 alla 2
                        spriteNum = 2; 
                        break;
                    case 2:  // dalla 2 alla 3(1)
                        spriteNum = 3;
                        break;
                    case 3:  // dalla 3(1) alla 4
                        spriteNum = 4;
                        break;
                    case 4: // dalla 4 alla 1
                        spriteNum = 1;
                        break;
                    }

                spriteCounter = 0;  // e resetta  il counter
                // System.out.println(x+" "+y);  // da eliminare
                // System.out.println("Enemy tile:"+getEnemyTileX()+ " "+getEnemyTileY());
            }
        }
    }

    public void draw(){
        image = null;
        if(!died){  // se ancora non è stato colpito dalla bomba allora disegna l'enemy normale
            switch(direction){  // in base alla direzione, la variabile image prende il valore dell'immagine inserita
                case "up":
                    if(spriteNum == 1 || spriteNum == 3){
                        image = up1;
                    }
                    if(spriteNum == 2){
                        image = up2;
                    }
                    if(spriteNum == 4){
                        image = up3;
                    }
                    break;
                case "down":
                    if(spriteNum == 1 || spriteNum == 3){
                        image = down1;
                    }
                    if(spriteNum == 2){
                        image = down2;
                    }
                    if(spriteNum == 4){
                        image = down3;
                    }
                    break;
                case "left":
                    if(spriteNum == 1 || spriteNum == 3){
                        image = left1;
                    }
                    if(spriteNum == 2){
                        image = left2;
                    }
                    if(spriteNum == 4){
                        image = left3;
                    }
                    break;
                case "right":
                    if(spriteNum == 1 || spriteNum == 3){
                        image = right1;
                    }
                    if(spriteNum == 2){
                        image = right2;
                    }
                    if(spriteNum == 4){
                        image = right3;
                    }
                    break;
            }
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
        }
        if(!extinguished){  // se l'enemy non è esploso totalmente disegna l'immagine
            g2.drawImage(image, imageP.x, imageP.y, width, height, null);  // disegna lo sprite del personaggio (image) nella posizione x,y di grandezza tileSize
            //da eliminare
            g2.setColor(Color.BLUE);
            g2.draw(this.hitbox);  

            g2.setColor(Color.GREEN);
            g2.drawRect(getTileX(), getTileY(), tileSize, tileSize);

            g2.setColor(Color.RED);
            g2.draw(hitbox);
        }
    }
}
