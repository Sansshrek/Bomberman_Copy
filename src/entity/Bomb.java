 
package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.GamePanel;

public class BombHandler extends Entity{
    int bombTimer = 0;
    int fireTimer = 0;
    int spriteCounter = 0;
    int spriteNum = 1;
    int firePower;
    public boolean exploded = false, extinguished = false;
    public boolean endedAnimation = false;
    BufferedImage bombImage, largeBomb, mediumBomb, smallBomb;
    int bombWidth, bombHeight;
    BufferedImage[] expCenter = new BufferedImage[5];
    BufferedImage[] expEndUp = new BufferedImage[5];
    BufferedImage[] expEndBtm = new BufferedImage[5];
    BufferedImage[] expEndSx = new BufferedImage[5];
    BufferedImage[] expEndDx = new BufferedImage[5];
    BufferedImage[] expMidUp = new BufferedImage[5];
    BufferedImage[] expMidBtm = new BufferedImage[5];
    BufferedImage[] expMidSx = new BufferedImage[5];
    BufferedImage[] expMidDx = new BufferedImage[5];
    public Graphics2D g2;
    //largezza e altezza dell' immagine del player
    public final int width;
    public final int height; 
    
    //codinate top left dell' hitbox
    public int hitboxX; 
    public int hitboxY;
    
    //larghezza e altezza dell' hitbox
    public int hitboxWidth;
    public int hitboxHeight;

    public Bomb(GamePanel gp, int x, int y, int firePower, Graphics2D g2){
        this.gp = gp;

        this.speed = 0;
        this.direction = "down";
        this.ogTileSize = gp.getOgTileSize();
        this.scale = gp.getScale();
        this.tileSize = gp.getTileSize();
        //coordinate nel mondo
        this.x = x;
        this.y = y;

        //largezza e altezza dell' immagine dell' enemy
        this.width = tileSize; // larghezza dell' enemy
        this.height = tileSize; // altezza dell' enemy 
        this.firePower = firePower;
        this.bombWidth = tileSize;
        this.bombHeight = tileSize;

        //codinate top left dell' hitbox
        this.hitboxX = 0;// dove parte hitbox dell' enemy (2 pixel a destra rispetto a dove parte l'immagine)
        this.hitboxY = 0; // dove parte hitbox dell' enemy (12 pixel sotto rispetto a dove parte l'immagine)
        
        //larghezza e altezza dell' hitbox
        this.hitboxWidth = tileSize;// larghezza dell'hitbox dell' enemy
        this.hitboxHeight = tileSize;// altezza dell'hitbox dell' enemy
        
        hitbox = new Rectangle(hitboxX, hitboxY, hitboxWidth, hitboxHeight);
        hitboxDefaultX = hitboxX;
        hitboxDefaultY = hitboxY;

        setBombDefaultValues();
        getBombImage();
        
    }

	public void setBombDefaultValues(){
        x = (x*ogTileSize*scale) + (ogTileSize+ogTileSize/2)*gp.scale;   // posizione x dell'Enemy IN ALTO A SINISTRA
        y = (y*ogTileSize*scale) + (2*ogTileSize)*gp.scale;    // posizione y dell'Enemy IN ALTO A SINISTRA
        this.speed = 0;
        this.direction = "down";''
        
    }

    public void getBombImage(){
        try {
            //sprite delle bombe
            largeBomb = bombImage = ImageIO.read(getClass().getResourceAsStream("../res/bomb/largeBomb.png"));
            mediumBomb = ImageIO.read(getClass().getResourceAsStream("../res/bomb/mediumBomb.png"));
            smallBomb = ImageIO.read(getClass().getResourceAsStream("../res/bomb/smallBomb.png"));
            // sprite delle esplosioni
            for(int i=1; i<=5; i++){
                expCenter[i-1] = ImageIO.read(getClass().getResourceAsStream("../res/bomb/exp"+i+"/expCenter.png"));
                expEndUp[i-1] = ImageIO.read(getClass().getResourceAsStream("../res/bomb/exp"+i+"/expEndUp.png"));
                expEndBtm[i-1] = ImageIO.read(getClass().getResourceAsStream("../res/bomb/exp"+i+"/expEndBtm.png"));
                expEndSx[i-1] = ImageIO.read(getClass().getResourceAsStream("../res/bomb/exp"+i+"/expEndSx.png"));
                expEndDx[i-1] = ImageIO.read(getClass().getResourceAsStream("../res/bomb/exp"+i+"/expEndDx.png"));
                expMidUp[i-1] = ImageIO.read(getClass().getResourceAsStream("../res/bomb/exp"+i+"/expMidUp.png"));
                expMidBtm[i-1] = ImageIO.read(getClass().getResourceAsStream("../res/bomb/exp"+i+"/expMidBtm.png"));
                expMidSx[i-1] = ImageIO.read(getClass().getResourceAsStream("../res/bomb/exp"+i+"/expMidSx.png"));
                expMidDx[i-1] = ImageIO.read(getClass().getResourceAsStream("../res/bomb/exp"+i+"/expMidDx.png"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("bomb "+x+" "+y);

    }

    
    public void powerUpHandler(int index){
        if(index != 999){  // se non è il valore default
            String objName = gp.obj.get(index).name;
            if(objName != "block") // da eliminare
                System.out.println(objName);
            gp.obj.set(index, gp.obj.get(index).power);
            switch(objName){
                case "fire":
                
                break;
                case "bomb":
                
                break;
                case "skate":
                
                break;
                case "life":
                
                break;
                case "death":
                    
                break;
                case "onigiri":
                    
                break;
                case "apple":
                    
                break;
                case "ice_cream":
                    
                break;
                case "cake":
                    ;
                break;
                case "nothing":
                break;
            }
        }
    }
    public void draw(Graphics2D g2){
        spriteCounter++;
        
        if(bombTimer == 9){  // dopo 9 animazioni / 1.5 secondi
            exploded = true;
            spriteNum = 1;  // resetta lo spriteNum prima che esplode la bomba
        }else if(spriteCounter > 10){  // ogni 10/60 volte al secondo 
            System.out.println(bombTimer);
            bombTimer++;  // aumenta il contatore della bomba
            switch(spriteNum){
                case 1:  // dalla 1(small) alla 2(medium)
                    spriteNum = 2;
                    bombImage = smallBomb;
                    break;
                case 2:  // dalla 2(medium) alla 3(big)
                    spriteNum = 3;
                    bombImage = mediumBomb;
                    break;
                case 3:  // dalla 3(big) alla 4(medium)
                    spriteNum = 4;
                    bombImage = largeBomb;
                    break;
                case 4: // dalla 4(medium) alla 1(small)
                    spriteNum = 1;
                    bombImage = mediumBomb;
                    break;
            }
            spriteCounter = 0;  // e resetta il counter
        }
        g2.drawImage(bombImage, x, y, bombWidth, bombHeight, null); 
    }
    
    public void generateFires(Graphics2D g2){
        if(fireTimer == 4){  // dopo 9 animazioni / 1.5 secondi
            extinguished = true;
        }else if(spriteCounter > 10){  // ogni 10/60 volte al secondo 
            fireTimer++;  // aumenta il contatore della bomba
        }
        spriteCounter++;
    
        // If the explosion has extinguished, do not draw the fire
        if (extinguished) {
            return;
        }
    
        // Draw the center of the explosion
        g2.drawImage(expCenter[fireTimer], x, y, bombWidth, bombHeight, null); 
    
        // Draw the horizontal line of the explosion
        for (int i = 1; i <= firePower; i++) {
            // Draw the middle part of the explosion
            if (i < firePower) {
                g2.drawImage(expMidSx[fireTimer], x - i * bombWidth, y, bombWidth, bombHeight, null);
                g2.drawImage(expMidDx[fireTimer], x + i * bombWidth, y, bombWidth, bombHeight, null);
            }
    
            // Draw the end part of the explosion
            if (i == firePower) {
                g2.drawImage(expEndSx[fireTimer], x - i * bombWidth, y, bombWidth, bombHeight, null);
                g2.drawImage(expEndDx[fireTimer], x + i * bombWidth, y, bombWidth, bombHeight, null);
            }
        }
    
        // Draw the vertical line of the explosion
        for (int i = 1; i <= firePower; i++) {
            // Draw the middle part of the explosion
            if (i < firePower) {
                g2.drawImage(expMidUp[fireTimer], x, y - i * bombHeight, bombWidth, bombHeight, null);
                g2.drawImage(expMidBtm[fireTimer], x, y + i * bombHeight, bombWidth, bombHeight, null);
            }
    
            // Draw the end part of the explosion
            if (i == firePower) {
                g2.drawImage(expEndUp[fireTimer], x, y - i * bombHeight, bombWidth, bombHeight, null);
                g2.drawImage(expEndBtm[fireTimer], x, y + i * bombHeight, bombWidth, bombHeight, null);
            }
        }
    }
}

