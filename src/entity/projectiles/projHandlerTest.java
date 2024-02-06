package entity.projectiles;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import main.GamePanel;
import objects.Bomb;

public class projHandlerTest{
    int projectileWidth, projectileHeight, tileSize, projectileNumber;
    public Map<String, Rectangle> projectiles = new HashMap<>();
    BufferedImage projImage[] = new BufferedImage[4];
    String bossType;
    public Graphics2D g2;
    public GamePanel gp; //LASCIAMO STI COMMENTI PER IL PROF
    

    public projHandlerTest(GamePanel gp, int tileSize, String bossType){
        this.tileSize = tileSize;
        this.projectileWidth = tileSize;
        this.projectileHeight = tileSize;       //PALLE DI PALLE
        this.gp = gp;  // capito
        this.bossType = bossType;
        loadImage();
    }
    
    public void loadImage(){
        for(int sprite=1; sprite<=4; sprite++){
            try{
                if(bossType == "knight")  // se è il primo boss lancia le pietre quando colpisce col martello
                    projImage[sprite-1] = ImageIO.read(getClass().getResourceAsStream("../res/enemies/projectiles/pebble"+String.valueOf(sprite)+".png"));
                else if(bossType == "clown")  // se è il secondo boss spara i proiettili
                    projImage[sprite-1] = ImageIO.read(getClass().getResourceAsStream("../res/enemies/projectiles/projectile"+String.valueOf(sprite)+".png"));
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    
    public void createProjectiles(int startX, int startY){
        if(bossType == "knight"){
            projectiles.put("right", new Rectangle(startX, startY, projectileWidth, projectileHeight));
        }else if(bossType == "clown"){
            
        }
    }

    public void updateProjectiles(){
        System.out.println("TEST PROIETTILE");
        for (Map.Entry<String, Rectangle> entry : projectiles.entrySet()) {
            if(bossType == "knight"){
                String key = entry.getKey();
                Rectangle value = entry.getValue();
                if(key == "right"){
                    value.x += 1;
                }
            }else{
                Rectangle value = entry.getValue();
            }
        }
    }

    public void drawProjectiles(Graphics2D g2){
        for (Map.Entry<String, Rectangle> entry : projectiles.entrySet()) {
            if(bossType == "knight"){
                Rectangle projectile = entry.getValue();
                g2.drawImage(projImage[0], projectile.x, projectile.y, projectileWidth, projectileHeight, null);
            }else{
                Rectangle value = entry.getValue();
            }
        }
    }

    public boolean checkHitPlayer(Rectangle playerHitbox){
        for(Map.Entry<String, Rectangle> entry : projectiles.entrySet()){  // controlliamo ogni proiettile
            Rectangle projectile = entry.getValue();  // controlliamo il proiettile corrente
            if(projectile.intersects(playerHitbox)){  // se il proiettile colpisce il player
                return true;  // allora ritorna vero
            }
        }
        return false;  // altrimenti ritorna falso
    }
    
}