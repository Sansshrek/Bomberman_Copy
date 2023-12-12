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
            if(bomb.exploded){  // se la bomba esplode
                bomb.generateFires(g2);  // creiamo le fiamme
            }else if(!bomb.extinguished){  // se ancora non si è spenta
                bomb.draw(g2);   // disegnamo la bomba in se che sta per esplodere
            }else{  // se si è spenta
                bombs.remove(bomb);  // rimuoviamo la bomba dalla lista delle bombe presenti sulla mappa
            }
        }
    }
}