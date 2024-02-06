package entity.projectiles;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import entity.Node;
import main.GamePanel;
import objects.Bomb;

public class ProjectileHandler{
    int tileSize, projectileNumber;
    int[][] projectileDirections = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}}; // NW, N, NE, W, E, SW, S, SE
    public ArrayList<Projectile> projectiles = new ArrayList<>();
    Random rand = new Random();
    String bossType;
    public GamePanel gp;
    

    public ProjectileHandler(GamePanel gp, String bossType){
        this.gp = gp;  
        this.tileSize = gp.tileSize;
        this.bossType = bossType;
    }
    
    
    public void createProjectiles(int startX, int startY){
        if(bossType == "knight"){
            int projectileNum = (int)(Math.random()*(6 - 3)+3);  // un range da 3 a 6
            
            for(int i=0; i<projectileNum; i++){  // crea un numero di proiettili random
                int endX = rand.nextInt(tileSize*2);  // un range da 0.5 a 1.5 blocchi
                int endY = rand.nextInt(tileSize*3-tileSize)+tileSize;  // un range da 1 a 3 blocchi
                int moveX = rand.nextInt(3 - 2)+2;  // un range da 2 a 3
                int moveY = rand.nextInt(7 - 5)+5;  // un range da 5 a 7
                
                if(rand.nextBoolean()){  // 50% di probabilita che entra nell'if e inverte i valori per mandarli verso sinistra
                    endX = -endX;
                    moveX = -moveX;
                }
                
                System.out.println("StartX: "+startX+" StartY: "+startY+" endX: "+endX+" EndY: "+endY+" moveX: "+moveX);
                projectiles.add(new KnightProjectile(tileSize, tileSize, startX, startY, startX+endX, startY+endY, moveX, moveY));
            }
        }else if(bossType == "clown"){
            int speed = 2;
            for (int[] projectileMovement : projectileDirections) {  // per ogni direzione
                if(projectileMovement[0] == 0 || projectileMovement[1] == 0)  // se è una delle direzioni verticali/orizzontali
                    projectiles.add(new ClownProjectile(tileSize, tileSize, gp.screenWidth, gp.screenHeight, startX, startY, projectileMovement[0], projectileMovement[1], speed, false));  // crea un proiettile in quella direzione
                else
                    projectiles.add(new ClownProjectile(tileSize, tileSize, gp.screenWidth, gp.screenHeight, startX, startY, projectileMovement[0], projectileMovement[1], speed, true));  // crea un proiettile in quella direzione
            }
        }
    }

    public void updateProjectiles(){
        for (Projectile projectile : projectiles) {  // per ogni proiettile
            projectile.update();
            // System.out.println("Proiettile: "+projectile.hitbox.x+" "+projectile.hitbox.y+" "+projectile.moveX+" "+projectile.moveY);
            if(projectile.hitbox.intersects(gp.player.hitbox) && !gp.player.invulnerable){
                gp.player.kill();
            }
        }
        for (int i = 0; i < projectiles.size(); i++) {  // per ogni proiettile
            if(projectiles.get(i).extinguished){  // se il proiettile è estinto
                projectiles.remove(i);  // lo rimuove
                i--;  // decrementa i per non saltare un proiettile
            }
        }
    }

    public void drawProjectiles(Graphics2D g2){
        for (Projectile projectile : projectiles) {  // per ogni proiettile
            g2.drawImage(projectile.image, projectile.hitbox.x, projectile.hitbox.y, tileSize, tileSize, null);
        }
    }
    
}