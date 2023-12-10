package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.GamePanel;

public class Bomb extends SuperObject{
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
    

    public Bomb(GamePanel gp, int x, int y, int firePower, int tileSize, Graphics2D g2){
        super(gp);
        this.x = x;
        this.y = y;
        this.firePower = firePower;
        this.bombWidth = tileSize;
        this.bombHeight = tileSize;
        // this.g2 = g2;
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
            spriteCounter = 0;
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
            

            //da eliminare
            g2.setColor(Color.GREEN);
            g2.drawRect(x - i*bombWidth, y, gp.tileSize, gp.tileSize);

            if((x-i*bombWidth) > (gp.gameBorderLeftX)){

                int positionSx = (((x - i*bombWidth- (gp.tileSize+gp.tileSize/2)))/gp.tileSize)  * 10 + ((y - (2*gp.tileSize + (gp.tileSize/2)))/gp.tileSize );
                int positionDx = (((x + i*bombWidth- (gp.tileSize+gp.tileSize/2)))/gp.tileSize)  * 10 + ((y - (2*gp.tileSize + (gp.tileSize/2)))/gp.tileSize );
                
                if(gp.obj.get(positionSx) != null){
                    System.out.println(gp.obj.get(positionSx).name);
                    gp.obj.set(positionSx, gp.obj.get(positionSx).power);
                }
                if(gp.obj.get(positionDx) != null){
                    System.out.println(gp.obj.get(positionDx).name);
                    gp.obj.set(positionDx, gp.obj.get(positionDx).power);
                }
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
        }

        // Draw the vertical line of the explosion
        for (int i = 1; i <= firePower; i++) {
            int positionUp = ((x-(gp.tileSize+gp.tileSize/2))/gp.tileSize ) * 10 + ((y- i*bombWidth- (2*gp.tileSize + (gp.tileSize/2)))/gp.tileSize );
            int positionDw = ((x-(gp.tileSize+gp.tileSize/2))/gp.tileSize ) * 10 + ((y+ i*bombWidth- (2*gp.tileSize + (gp.tileSize/2)))/gp.tileSize );
            if(gp.obj.get(positionUp) != null){
                System.out.println(gp.obj.get(positionUp).name);
                gp.obj.set(positionUp, gp.obj.get(positionUp).power);
            }
            if(gp.obj.get(positionDw) != null){
                System.out.println(gp.obj.get(positionDw).name);
                gp.obj.set(positionDw, gp.obj.get(positionDw).power);
            }
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