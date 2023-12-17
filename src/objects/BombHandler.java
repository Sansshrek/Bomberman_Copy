package objects;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.GamePanel;

public class BombHandler{
    int bombTimer = 0;
    int spriteCounter = 0;
    int spriteNum = 1;
    int bombWidth, bombHeight, bombNumber, tileSize;
    private ArrayList<Bomb> bombs = new ArrayList<>();
    private ArrayList<Fire> fires = new ArrayList<>();
    public Graphics2D g2;
    

    public BombHandler(int tileSize){
        this.tileSize = tileSize;
        this.bombWidth = tileSize;
        this.bombHeight = tileSize;
    }

    public void addBombNumber(){
        bombNumber+=1; 
    }

    public void createBomb(GamePanel gp, int x, int y, int firePower){
        if(bombNumber > 0){
            bombs.add(new Bomb(gp, x, y, firePower, tileSize, g2));
            bombNumber--;
        }
    }

    public void updateBomb(){
        for (Bomb bomb : bombs) {
            if(bomb.exploded){
                bomb.generateFires(g2);
            }else if(!bomb.extinguished){
                bomb.draw(g2); 
            }else{
                bombs.remove(bomb);
            }
        }
    }

    /*
    public void explode(){
        System.out.println("Cnaicao");
        if(spriteCounter > 10){  // ogni 10/60 volte al secondo 
            switch(spriteNum){
                case 0:  // dalla 0(small) alla 1(medium)
                    spriteNum = 1;
                    break;
                case 1:  // dalla 1(medium) alla 2(big)
                    spriteNum = 2;
                    break;
                case 2:  // dalla 2(big) alla 3(medium)
                    spriteNum = 3;
                    break;
                case 3: // dalla 3(medium) alla 0(small)
                    spriteNum = 0;
                    endedAnimation = true;
                    break;
            } 
        }
        if(spriteCounter > 10)
            spriteCounter = 0;  // e resetta il counter
        g2.drawImage(expCenter[spriteNum], x, y, null);  // centro
        int expSize = firePower*2;
        g2.drawImage(expEndUp[spriteNum], x, y - expSize*48, null);  // alto
        g2.drawImage(expEndBtm[spriteNum], x, y + expSize*48, null);  // basso
        g2.drawImage(expEndSx[spriteNum], x - expSize*48, y, null);  // sinistra
        g2.drawImage(expEndDx[spriteNum], x - expSize*48, y, null);  // destra
    } */

}