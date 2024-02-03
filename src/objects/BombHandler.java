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
    public ArrayList<Bomb> bombs = new ArrayList<>();
    private ArrayList<Fire> fires = new ArrayList<>();
    public Graphics2D g2;
    public GamePanel gp;
    

    public BombHandler(GamePanel gp, int tileSize){
        this.tileSize = tileSize;
        this.bombWidth = tileSize;
        this.bombHeight = tileSize;
        this.gp = gp;
    }

    public void addBombNumber(){
        bombNumber+=1; 
    }

    public void createBomb(int x, int y, int tileRow, int tileCol, int firePower){
        if(bombNumber > bombs.size()){
            if(gp.obj[tileRow][tileCol] == null){  // se non c'è niente in quella posizione allora puo mettere la bomba 
                Bomb newBomb = new Bomb(this.gp, x, y, tileRow, tileCol, firePower, tileSize, g2);
                bombs.add(newBomb);
                gp.obj[tileRow][tileCol] = newBomb;  // aggiunge la bomba alla matrice di oggetti sulla mappa
            }
        }
    }
    
    public void createEnemyBomb(int x, int y, int tileRow, int tileCol, int firePower){
        Bomb newBomb = new Bomb(this.gp, x, y, tileRow, tileCol, 3, tileSize, g2);
        bombs.add(newBomb);
    }

    public void updateBomb(){
        int index = -1;
        for (Bomb bomb: bombs) {
            if(bomb.exploded && !bomb.extinguished){
                bomb.generateFires(g2);
            }else if(!bomb.extinguished)
                bomb.draw(g2); 
            else{  // se la bomba ha finito di esplodere prendo il suo index sulla lista delle bombe
                index = bombs.indexOf(bomb);
            }
        }
        if(index != -1){  // se è diverso dal valore di default
            gp.obj[bombs.get(index).tileRow][bombs.get(index).tileCol] = null;  // elimina la bomba dalla mappa
            bombs.remove(index);  // e dalla lista delle bombe
        }
    }

    public void removeBomb(Bomb bomb){
        int index = bombs.indexOf(bomb);
        gp.obj[bombs.get(index).tileRow][bombs.get(index).tileCol] = null;  // elimina la bomba dalla mappa
        bombs.remove(index);  // e dalla lista delle bombe
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