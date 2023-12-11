package objects;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import main.GamePanel;

public class Block extends SuperObject {
    int spriteCounter = 0;
    int spriteNum = 1;
    BufferedImage img1, img2, img3, img4, destroyimg1, destroyimg2, destroyimg3, destroyimg4, destroyimg5, destroyimg6;
    boolean exploded = false, extinguished = false;
    public Block(GamePanel gp, int x, int y, String powerUp, int position){
        super(gp);
        this.x = x;
        this.y = y;
        this.indexObj = position;
        power = new PowerUp(gp, x, y, powerUp);
        name = "block";
        collision = true;
        try {  // apro le 4 sprites del blocco
            
           
            img1 = ImageIO.read(getClass().getResourceAsStream("../res/tiles/destructible/alarm01.png"));
            img2 = ImageIO.read(getClass().getResourceAsStream("../res/tiles/destructible/alarm02.png"));
            img3 = ImageIO.read(getClass().getResourceAsStream("../res/tiles/destructible/alarm03.png"));
            img4 = ImageIO.read(getClass().getResourceAsStream("../res/tiles/destructible/alarm04.png"));
            
            destroyimg1 = ImageIO.read(getClass().getResourceAsStream("../res/tiles/destructible/alarm_destroy01.png"));
            destroyimg2 = ImageIO.read(getClass().getResourceAsStream("../res/tiles/destructible/alarm_destroy02.png"));
            destroyimg3 = ImageIO.read(getClass().getResourceAsStream("../res/tiles/destructible/alarm_destroy03.png"));
            destroyimg4 = ImageIO.read(getClass().getResourceAsStream("../res/tiles/destructible/alarm_destroy04.png"));
            destroyimg5 = ImageIO.read(getClass().getResourceAsStream("../res/tiles/destructible/alarm_destroy05.png"));
            destroyimg6 = ImageIO.read(getClass().getResourceAsStream("../res/tiles/destructible/alarm_destroy06.png"));
             /*
            img1 = ImageIO.read(getClass().getResourceAsStream("../res/tiles/destructible/totti.png"));
            img2 = ImageIO.read(getClass().getResourceAsStream("../res/tiles/destructible/totti.png"));
            img3 = ImageIO.read(getClass().getResourceAsStream("../res/tiles/destructible/totti.png"));
            img4 = ImageIO.read(getClass().getResourceAsStream("../res/tiles/destructible/totti.png"));*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroy(){
        spriteNum = 1;
        exploded = true;
    }

    public void update(){
        spriteCounter++;
        if(!extinguished){
            if(exploded){
                if(spriteCounter > 10){
                    System.out.println("EXPLODE");
                    switch(spriteNum){
                        case 1:
                            image = destroyimg1;
                            spriteNum++;
                        break;
                        case 2:
                            image = destroyimg2;
                            spriteNum++;
                        break;
                        case 3:
                            image = destroyimg3;
                            spriteNum++;
                        break;
                        case 4:
                            image = destroyimg4;
                            spriteNum++;
                        break;
                        case 5:
                            image = destroyimg5;
                            spriteNum++;
                        break;
                        case 6:
                            image = destroyimg6;
                            spriteNum++;
                        break;
                        default:
                            extinguished = true;
                    }
                    spriteCounter = 0;
                }
            }
            else{
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
        }else{
            extinguished = true;
            if(power.name != "nothing")
                gp.obj.set(indexObj, power);
            else
                gp.obj.set(indexObj, null);
        }
    }
}
