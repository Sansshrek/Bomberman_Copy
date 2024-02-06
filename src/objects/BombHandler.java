package objects;

import java.awt.Graphics2D;
import java.util.ArrayList;

import main.GamePanel;

public class BombHandler{
    int bombWidth, bombHeight, bombNumber, tileSize;
    public ArrayList<Bomb> bombs = new ArrayList<>();
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
    public void setBombNumber(int bombNumber){
        this.bombNumber = bombNumber;
    }

    public void createBomb(int x, int y, int tileRow, int tileCol, int firePower){
        if(bombNumber > 0){
            if(gp.obj[tileRow][tileCol] == null){  // se non c'è niente in quella posizione allora puo mettere la bomba 
                gp.playSfx(5); // sound piazza bomba
                Bomb newBomb = new Bomb(this.gp, x, y, tileRow, tileCol, firePower, tileSize, g2);
                bombs.add(newBomb);
                gp.obj[tileRow][tileCol] = newBomb;  // aggiunge la bomba alla matrice di oggetti sulla mappa
                bombNumber--;
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
}