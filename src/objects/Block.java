package objects;

import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import main.GamePanel;

public class Block extends SuperObject {
    int spriteCounter = 0;
    int spriteNum = 1;
    int tileX, tileY;
    BufferedImage img1, img2, img3, img4, destroyimg1, destroyimg2, destroyimg3, destroyimg4, destroyimg5, destroyimg6;
    boolean eploded = false, extinguished = false;
    public Block(GamePanel gp, int x, int y, int tileX, int tileY, String powerUp){
        super(gp);
        this.x = x;
        this.y = y;
        this.blockP = new Point(x, y);
        this.tileX = tileX;
        this.tileY = tileY;
        this.hitbox.x = x;
        this.hitbox.y = y;
        power = new PowerUp(gp, x, y, tileX, tileY, powerUp);
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

    public void destroy(){  // se il blocco è esploso
        spriteNum = 1;  // imposta a 1 lo spriteNum per prendere la prima animazione dell'esplosione
        exploded = true;  // imposta la variabile bool dell'esplosione a true
    }

    public void update(){
        spriteCounter++;
        if(!extinguished){  // se ancora non ha finito di esplodere
            if(exploded){  // se il blocco è esploso inizia l'animazione dell'esplosione
                if(spriteCounter > 10){
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
                            extinguished = true;  // il blocco ha finito di esplodere
                    }
                    spriteCounter = 0;
                }
            }
            else{  // altrimenti se il blocco ancora non è esploso continua l'animazione normale
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
        }else{  // altrimenti se ha finito di esplodere
            gp.tileM.blockTileNum[tileX][tileY] = 0;  // resetta il tile sulla mappa
            if(power.name != "nothing")  // se il blocco ha un powerUp
                gp.obj[tileY][tileX] = power;  // allora il blocco viene cambiato in powerUp nella lista degli oggetti
            else
                gp.obj[tileY][tileX] = power;  // altrimenti viene rimosso dalla lista se non ha powerUp
        }
    }
}
