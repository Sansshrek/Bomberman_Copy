package entity.projectiles;

import java.awt.Rectangle;
import java.io.IOException;

import javax.imageio.ImageIO;

public class KnightProjectile extends Projectile{
    int endX, endY, middleX, highY;
    boolean rise = true;

    public KnightProjectile(int width, int height, int startX, int startY, int endX, int endY, int moveX, int moveY){
        this.width = width;
        this.height = height;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.middleX = (startX+endX)/2;
        this.moveX = moveX;
        this.moveY = moveY;
        this.hitbox = new Rectangle(startX, startY, width, height);
        
        try{
            this.image = ImageIO.read(getClass().getResourceAsStream("../../res/enemies/projectiles/pebble.png"));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void update(){
        hitbox.x += moveX;  // sposta il proiettile in base al movimento dato
        if(hitbox.y > endY){  // se supera alla posizione dove deve andare
            extinguished = true;  // si estingue il proiettile
        }
        if(moveX > 0 && hitbox.x > middleX){  // se il movimento è verso destra e supera la posizione centrale
            rise = false;  // inizia a scendere
        }else if(moveX < 0 && hitbox.x < middleX){  // se il movimento è verso sinistra e supera la posizione centrale
            rise = false;  // inizia a scendere
        }
        if(rise){  // se sta salendo
            hitbox.y -= moveY;  // sposta il proiettile in alto in base al movimento dato
        }else{  // se sta scendendo
            hitbox.y += moveY;  // sposta il proiettile in basso in base al movimento dato
        }
    }
    
}
