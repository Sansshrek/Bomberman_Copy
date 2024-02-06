package entity.projectiles;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ClownProjectile extends Projectile{
    BufferedImage projectileImage[] = new BufferedImage[4];
    Rectangle gameHitbox;
    int spriteCounter = 0, spriteNum = 0;
    boolean switchDir = false, diagonal;

    public ClownProjectile(int width, int height, int screenWidth, int screenHeight, int startX, int startY, int moveX, int moveY, int speed, boolean diagonal){
        this.width = width;
        this.height = height;
        this.moveX = moveX*speed;
        this.moveY = moveY*speed;
        this.diagonal = diagonal;
        loadImage();
        this.gameHitbox = new Rectangle(0, 0, screenWidth, screenHeight);
        this.hitbox = new Rectangle(startX, startY, width, height);
    }

    public void loadImage(){
        try{
            for(int sprite=1; sprite<=4; sprite++){
                projectileImage[sprite-1] = ImageIO.read(getClass().getResourceAsStream("../../res/enemies/projectiles/projectile"+String.valueOf(sprite)+".png"));
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        image = projectileImage[0];
    }

    public void update(){
        if(!hitbox.intersects(gameHitbox)){  // se esce dall'hitbox del gioco
            extinguished = true;  // allora lo elimina
        }
        if(!diagonal){  // se non è un proiettile diagonale
            hitbox.x += moveX;  // sposta il proiettile in base al movimento dato
            hitbox.y += moveY;
        }else{
            // senza questo controllo i proiettili si muovono disegnando un quadrato che si espande
            // usando questo switch i proiettili diagonali sono più lenti e i proiettili disegnano un rombo che si espande
            if(switchDir){  // se è attiva sposta l'hitbox in orizzontale
                hitbox.x += moveX;  // sposta il proiettile in base al movimento dato
                switchDir = false;  // e disattiva lo switch
            }else{  // altrimenti sposta l'hitbox in verticale
                hitbox.y += moveY;
                switchDir = true;  // e attiva lo switch
            }
        }
        spriteCounter++;
        if(spriteCounter == 15){  // ogni 15 update
            spriteCounter = 0;  // resetta il contatore 
            spriteNum++;  // e cambia lo sprite del proiettile
            if(spriteNum == 4){  // se finisce l'animazione
                spriteNum = 0;  // resetta lo sprite
            }
            image = projectileImage[spriteNum];  // prende l'immagine in quella posizione degli sprite
        }
    }
}
