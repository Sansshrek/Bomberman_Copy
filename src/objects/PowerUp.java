package objects;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import main.GamePanel;

public class PowerUp extends SuperObject{
    int spriteCounter = 0, spriteNum = 1;
    BufferedImage img, destroyimg1, destroyimg2, destroyimg3, destroyimg4, destroyimg5, destroyimg6, destroyimg7;
    boolean exploded = false, extinguished = false;
    public PowerUp(GamePanel gp, int x, int y, int tileX, int tileY, String powerUp){
        super(gp);
        name = powerUp;
        this.x = x;
        this.y = y;
        this.tileX = tileX;
        this.tileY = tileY;
        this.hitbox.x = x;
        this.hitbox.y = y;
        this.collision = false;
        try {
            img = ImageIO.read(getClass().getResourceAsStream("../res/powerup/"+powerUp+".png"));
            
            destroyimg1 = ImageIO.read(getClass().getResourceAsStream("../res/powerup/powerup_destroy/powerup_destroy01.png"));
            destroyimg2 = ImageIO.read(getClass().getResourceAsStream("../res/powerup/powerup_destroy/powerup_destroy02.png"));
            destroyimg3 = ImageIO.read(getClass().getResourceAsStream("../res/powerup/powerup_destroy/powerup_destroy03.png"));
            destroyimg4 = ImageIO.read(getClass().getResourceAsStream("../res/powerup/powerup_destroy/powerup_destroy04.png"));
            destroyimg5 = ImageIO.read(getClass().getResourceAsStream("../res/powerup/powerup_destroy/powerup_destroy05.png"));
            destroyimg6 = ImageIO.read(getClass().getResourceAsStream("../res/powerup/powerup_destroy/powerup_destroy06.png"));
            destroyimg7 = ImageIO.read(getClass().getResourceAsStream("../res/powerup/powerup_destroy/powerup_destroy07.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroy(){
        exploded = true;  // imposta la variabile bool dell'esplosione a true
    }

    public void update(){
        spriteCounter++;
        if(!extinguished){  // se ancora non ha finito di esplodere
            if(exploded){  // se il blocco è esploso inizia l'animazione dell'esplosione
            System.out.println("BOOM");
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
                        case 7:
                            image = destroyimg7;
                            spriteNum++;
                        break;
                        case 8:  // rimuove lo sprite dell'esplosione
                            image = null;
                            spriteNum++;
                        break;
                        default:
                            extinguished = true;  // il powerup ha finito di esplodere
                    }
                    spriteCounter = 0;
                }
            }
            else{  // altrimenti se il powerup ancora non è esploso continua l'animazione normale
                image = img;
            }
        }else{  // altrimenti se ha finito di esplodere
            gp.obj[tileY][tileX] = null;  // viene rimosso dalla lista degli oggetti
        }
    }
}
