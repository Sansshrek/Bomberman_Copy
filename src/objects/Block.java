package objects;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import main.GamePanel;

public class Block extends SuperObject {
    int spriteCounter = 0;
    int spriteNum = 1;
    BufferedImage img1, img2, img3, img4;
    boolean exploded, extinguished;
    public Block(GamePanel gp, int x, int y, String powerUp){
        super(gp);
        this.x = x;
        this.y = y;
        power = new PowerUp(gp, x, y, powerUp);
        name = "block";
        collision = true;
        try {  // apro le 4 sprites del blocco
            
           
            img1 = ImageIO.read(getClass().getResourceAsStream("../res/tiles/destructible/alarm01.png"));
            img2 = ImageIO.read(getClass().getResourceAsStream("../res/tiles/destructible/alarm02.png"));
            img3 = ImageIO.read(getClass().getResourceAsStream("../res/tiles/destructible/alarm03.png"));
            img4 = ImageIO.read(getClass().getResourceAsStream("../res/tiles/destructible/alarm04.png"));
             /*
            img1 = ImageIO.read(getClass().getResourceAsStream("../res/tiles/destructible/totti.png"));
            img2 = ImageIO.read(getClass().getResourceAsStream("../res/tiles/destructible/totti.png"));
            img3 = ImageIO.read(getClass().getResourceAsStream("../res/tiles/destructible/totti.png"));
            img4 = ImageIO.read(getClass().getResourceAsStream("../res/tiles/destructible/totti.png"));*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(){
        spriteCounter++;
        if(spriteCounter > 15){  // ogni 15/60 volte al secondo 
            switch(spriteNum){
                case 1:  // dalla 1 alla 2
                    spriteNum = 2;
                    image = img1;
                    break;
                case 2:  // dalla 2 alla 3
                    spriteNum = 3;
                    image = img2;
                    break;
                case 3:  // dalla 3 alla 4
                    spriteNum = 4;
                    image = img3;
                    break;
                case 4: // dalla 4 alla 1
                    spriteNum = 1;
                    image = img4;
                    break;
            }
            spriteCounter = 0;
        }
    }
}
