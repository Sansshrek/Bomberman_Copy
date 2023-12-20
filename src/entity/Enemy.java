
package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import main.GamePanel;

public class Enemy extends Entity{
    // public ArrayList<SuperObject> bombObj = new ArrayList<>();

    public BufferedImage up1, up2, up3, down1, down2, down3, left1, left2, left3, right1, right2, right3;
    //largezza e altezza dell' immagine del player
    public final int width;
    public final int height; 
    
    //codinate top left dell' hitbox
    public int hitboxX; 
    public int hitboxY;
    
    //larghezza e altezza dell' hitbox
    public int hitboxWidth;
    public int hitboxHeight;

    public Enemy(GamePanel gp, int x, int y){
        super(gp);

        this.speed = 1;
        this.direction = "down";
        this.ogTileSize = gp.getOgTileSize();
        this.scale = gp.getScale();
        this.tileSize = gp.getTileSize();
        //coordinate nel mondo
        this.x = x;
        this.y = y;

        //largezza e altezza dell' immagine dell' enemy
        this.width = 16*gp.scale; // larghezza dell' enemy
        this.height = 29*gp.scale; // altezza dell' enemy 

        //codinate top left dell' hitbox
        this.hitboxX = 0*gp.scale;// dove parte hitbox dell' enemy (0 pixel a destra rispetto a dove parte l'immagine)
        this.hitboxY = 8*gp.scale; // dove parte hitbox dell' enemy (12 pixel sotto rispetto a dove parte l'immagine)
        
        //larghezza e altezza dell' hitbox
        this.hitboxWidth = 15*gp.scale;// larghezza dell'hitbox dell' enemy
        this.hitboxHeight = 15*gp.scale;// altezza dell'hitbox dell' enemy
        
        hitbox = new Rectangle(hitboxX, hitboxY, hitboxWidth, hitboxHeight);
        hitboxDefaultX = hitboxX;
        hitboxDefaultY = hitboxY;

        System.out.println("Caricando l'enemy");  // da eliminare
        setEnemyDefaultValues();
        getEnemyImage();
        
    }

	public void setEnemyDefaultValues(){
        x = (x*ogTileSize*scale) + (ogTileSize+ogTileSize/2)*gp.scale;   // posizione x dell'Enemy IN ALTO A SINISTRA
        y = (y*ogTileSize*scale) + (2*ogTileSize)*gp.scale;    // posizione y dell'Enemy IN ALTO A SINISTRA
        this.speed = 1;
        this.direction = "down";
        
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
            
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void update(){  // update viene chiamato 60 volte al secondo
            // Check for collisions
            
        hitbox.x = x + hitboxX;
        hitbox.y = y + hitboxY;
        collisionOn = false;
        gp.cChecker.checkTile(this);
        int objIndex = gp.cChecker.checkObj(this, false, g2);
        powerUpHandler(objIndex); // controlliamo cosa fare con l'oggetto
        // If a collision occurs, check for collisions in all directions and choose a new direction
        if (collisionOn) {
            List<String> directions = Arrays.asList("up", "down", "left", "right");
            Collections.shuffle(directions);
            for (String dir : directions) {
                direction = dir;
                gp.cChecker.checkTile(this);
                if (!collisionOn) {
                    break;
                }
            }
        }
         // controlla se colpiamo qualche blocco
            // CONTROLLA COLLISIONE OBJECT
            

        if(!collisionOn){ // si puo muovere
            switch(direction){
                case "up": 
                    y -= speed; 
                    hitbox.y -= speed;
                    break;  // la posizione Y diminuisce della velocita del player
                case "down": 
                    y += speed; 
                    hitbox.y += speed;
                    break;
                case "left": 
                    x -= speed; 
                    hitbox.x -= speed;
                    break;
                case "right": 
                    x += speed; 
                    hitbox.x += speed;
                    break;
            }
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

            spriteCounter = 0;  // e resetta il counter
            // System.out.println(x+" "+y);  // da eliminare
            // System.out.println("Enemy tile:"+getEnemyTileX()+ " "+getEnemyTileY());
        }
    }

    public int getEnemyTileX(){
        int entityLeftWorldX = hitbox.x - (24 * gp.scale);  // Coordinata x dove parte la hitbox
        int entityRightWorldX = hitbox.x + hitbox.width - (24 * gp.scale);  // cordinata x dove arriva la hitbox
        int entityCenterX = ((entityLeftWorldX + entityRightWorldX) / 2 ) / gp.tileSize+1;
        return entityCenterX*gp.tileSize + gp.tileSize/2;
    }
    public int getEnemyTileY(){
        int entityTopWorldY = hitbox.y - (gp.hudHeight + (8 * gp.scale));  // coordinata y dove parte la hitbox
        int entityBottomWorldY = hitbox.y + hitbox.height - (gp.hudHeight + (8 * gp.scale));
        int entityCenterY = ((entityTopWorldY + entityBottomWorldY) / 2 ) / gp.tileSize+2;
        return entityCenterY*gp.tileSize + gp.tileSize/2;
    }
    public void powerUpHandler(int index){
        if(index != 999){  // se non è il valore default
            String objName = gp.obj.get(index).name;
            if(objName != "block") // da eliminare
                System.out.println(objName);
            gp.obj.set(index, gp.obj.get(index).power);
        }
    }
    public void draw(){
        BufferedImage image = null;
        
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
        g2.drawImage(image, x, y, gp.enemy.width, gp.enemy.height, null);  // disegna lo sprite del personaggio (image) nella posizione x,y di grandezza tileSize
        //da eliminare
        g2.setColor(Color.BLUE);
        g2.draw(this.hitbox);  

        g2.setColor(Color.GREEN);
        g2.drawRect(getEnemyTileX(), getEnemyTileY(), tileSize, tileSize);
    }
}
