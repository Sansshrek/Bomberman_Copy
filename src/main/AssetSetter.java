package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import objects.Block;
import tile.TileManager;

public class AssetSetter {
    GamePanel gp;
    TileManager tileM;

    public AssetSetter(GamePanel gp, TileManager tileM) {
        this.gp = gp;
        this.tileM = tileM;
    }

    public ArrayList<Integer> availablePos(){
        ArrayList<Integer> avPos = new ArrayList<>();
        for(int x=0; x<13; x++){  // 13 è il massimo dei tiles della mappa per la x
            for(int y=0; y<10 ;y++){  // 10 è il massimo dei tiles della mappa per la y
                if(tileM.blockTileNum[x][y] != 3 && tileM.blockTileNum[x][y] != -1 && x+y != 0){  // se non è un palazzo o non è gia preso
                    if(x != 0 && y != 0)  // pos vicino alla partenza del player 
                        avPos.add( x * 10 + y); // calcola l'indice unico per la posizione
                }
            }
        }
        return avPos;
    }
    public void setBlocks(){
        boolean checkExit = false;
        int numBlock = (int)(Math.random()*(40-33))+33;  // 33-40 blocchi a random
        System.out.println(numBlock);
        ArrayList<Integer> avPos = availablePos();
        for(int i=0; i<avPos.size(); i++){
            System.out.println(avPos.get(i));
        }
        for(int i=0; i<140; i++){  // inizializzamo l'array vuoto cosi che dopo mettiamo 
            gp.obj.add(null);
        }
        Collections.shuffle(avPos);  // randomizza le posizioni disponibili
        for(int i = 0; i< numBlock; i++){
            int position = avPos.get(i);
            int blockX = position / 10;  // ci permette di prendere da un numero soltato sia la X che la Y
            int blockY = position % 10;  // ad esempio se x=3, y=6 -> 3*10+6 = 36 -> 36/10 = 3 | 36%10 = 6
            /*
            while(true){  // controllo della posizione se non è gia stata presa
                blockX = (int)(Math.random()*(13)); // 0-12
                blockY = (int)(Math.random()*(10)); // 0-9
                if(tileM.blockTileNum[blockX][blockY] != 3 && tileM.blockTileNum[blockX][blockY] != -1 && blockX+blockY != 0){  // se non è un palazzo o non è gia preso
                    if(blockX == 0 && blockY == 0)  // pos vicino alla partenza del player 
                        break; // esci dal loop
                }
            }*/
            String powerUp;
            if(!checkExit){  // se non è stata gia impostata l'uscita
                powerUp = "exit";
                checkExit = true;
            }else
                powerUp = getPowerUp();
            tileM.blockTileNum[blockX][blockY] = -1;
            gp.obj.set(position, new Block(gp, blockX*gp.tileSize + (gp.tileSize+gp.tileSize/2), blockY*gp.tileSize + (2*gp.tileSize + (gp.tileSize/2)), powerUp, position));
        }
        for(int i=0; i<130; i++){  // inizializzamo l'array vuoto cosi che dopo mettiamo 
            if(gp.obj.get(i) != null)
                System.err.println(gp.obj.get(i).x+" "+gp.obj.get(i).y);
        }
    }

    public String getPowerUp(){
        // Probabilita         10%     15%     15%      5%       5%       25%       20%        15%        10%     25%
        String[] powerUps = {"fire", "bomb", "skate", "life", "death", "onigiri", "apple", "ice_cream", "cake", "nothing"};
        int[] probabilities = {10, 15, 15, 5, 5, 25, 20, 15, 10, 60};
        Random random = new Random();
        int total = 0;
        for (int probability : probabilities) {
            total += probability;
        }
        int randomNumber = random.nextInt(total) + 1; // genera un numero da 1 al totale delle probabilità

        int cumulativeProbability = 0;
        for (int i = 0; i < powerUps.length; i++) {
            cumulativeProbability += probabilities[i];
            if (randomNumber <= cumulativeProbability) {
                return powerUps[i]; // ritorna il powerUp all'indice i
            }
        }
        return null;
    }
}
