package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.awt.Point;

import objects.Block;
import tile.TileManager;

public class AssetSetter{
    GamePanel gp;
    TileManager tileM;
    Random rand = new Random();
    ArrayList<String> powerUp = new ArrayList<>(Arrays.asList(
    "blockCross", "blockCross", "blockCross",
    "fire", "fire", "fire", "fire", "fire", // 5%
    "bomb", "bomb", "bomb", "bomb", "bomb",  // 5% bomb
    "skate", "skate", "skate", "skate", "skate",  // 5% skate
    "life", "life", "death", "death",  // 2% life, 2% death
    "heart", "heart", "armor", "armor", "time", "time",  // 2% heart, 2% armor, 2% time
    "onigiri", "onigiri", "onigiri", "onigiri", "onigiri",  // 5% onigiri
    "apple", "apple", "apple", "apple",   // 4% apple 
    "ice_cream", "ice_cream", "ice_cream",  // 3% ice cream
    "cake", "cake", "cake", // 3% cake
    // 60% nothing
    "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing", "nothing"
    ));
    public AssetSetter(GamePanel gp, TileManager tileM) {
        this.gp = gp;
        this.tileM = tileM;
    }
    
    public ArrayList<Point> availablePosMatrix(){
        ArrayList<Point> avPos = new ArrayList<>();
        for(int row=0; row<gp.maxGameRow; row++){  // 13 è il massimo dei tiles della mappa per la x
            for(int col=0; col<gp.maxGameCol; col++){  // 10 è il massimo dei tiles della mappa per la y
                if(tileM.houseTileNum[row][col] != 3 && tileM.houseTileNum[row][col] != -1 && row+col != 0 && row+col != 1){  // se non è un palazzo o non è gia preso
                    avPos.add(new Point(col, row));
                }
            }
        }
        return avPos;
    }
    public void setMatrixBlocks(int minBlock, int maxBlock){
        int numBlock = (int)(Math.random()*(maxBlock-minBlock))+minBlock;  // range tra minBlock-maxBlock blocchi a random
        ArrayList<Point> avPos = availablePosMatrix();  // prendiamo le posizioni disponibili
        ArrayList<String> avPowerUp = getPowerUp(numBlock);
          // prendiamo i powerUp disponibili
        Collections.shuffle(avPos);  // randomizziamo l'ordine delle pos disponibili
         // randomizziamo l'ordine dei powerUp

        Point exit = avPos.get(0);
        gp.obj[exit.y][exit.x] = new Block(gp, exit.x*gp.tileSize + (gp.tileSize+gp.tileSize/2), exit.y*gp.tileSize + (2*gp.tileSize + (gp.tileSize/2)), exit.y, exit.x, "exit");
        for(int i=1; i<numBlock; i++){  // inizializzamo l'array vuoto cosi che dopo mettiamo 
            Point position = avPos.get(i);
            int tileCol = position.x;
            int tileRow = position.y;

            gp.obj[tileRow][tileCol] = new Block(gp, tileCol*gp.tileSize + (gp.tileSize+gp.tileSize/2), tileRow*gp.tileSize + (2*gp.tileSize + (gp.tileSize/2)), tileRow, tileCol, avPowerUp.get(i));
            
        }
    }

    public ArrayList<String> getPowerUp(int max){
        ArrayList<String> powerUpTemp = new ArrayList<>();
        List<String> powerUpCopy = powerUp.stream()
        .collect(Collectors.toList());
        
        for(int i=0; i<max; i++){
            // prende un index a random nel range della lunghezza dell'array
            int randomIndex = rand.nextInt(powerUpCopy.size());
            // usa l'index  random per prendere un elemento casuale dall'array
            powerUpTemp.add(powerUpCopy.get(randomIndex));
            // rimuove  l'elemento dalla lista in modo tale da non poterlo selezionare di nuovo
            powerUpCopy.remove(randomIndex);
        }
        return powerUpTemp;
    }
}
