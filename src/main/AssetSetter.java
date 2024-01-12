package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.awt.Point;

import objects.Block;
import tile.TileManager;

public class AssetSetter{
    GamePanel gp;
    TileManager tileM;

    public AssetSetter(GamePanel gp, TileManager tileM) {
        this.gp = gp;
        this.tileM = tileM;
    }

    public ArrayList<Integer> availablePos(){
        ArrayList<Integer> avPos = new ArrayList<>();
        for(int x=0; x<10; x++){  // 13 è il massimo dei tiles della mappa per la x
            for(int y=0; y<13; y++){  // 10 è il massimo dei tiles della mappa per la y
                if(tileM.houseTileNum[x][y] != 3 && tileM.houseTileNum[x][y] != -1 && x+y != 0 && x+y != 1 && x-y !=0){  // se non è un palazzo o non è gia preso
                    if(true)  // pos vicino alla partenza del player 
                        avPos.add( x * 10 + y); // calcola l'indice unico per la posizione
                }
            }
        }
        return avPos;
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
    public void setMatrixBlocks(){
        int numBlock = (int)(Math.random()*(45-33))+33;  // 33-45 blocchi a random
        ArrayList<Point> avPos = availablePosMatrix();  // prendiamo le posizioni disponibili
        ArrayList<String> avPowerUp = getPowerUp(numBlock);  // prendiamo i powerUp disponibili
        Collections.shuffle(avPos);  // randomizziamo l'ordine delle pos disponibili
        Collections.shuffle(avPowerUp); // randomizziamo l'ordine dei powerUp

        Point exit = avPos.get(0);
        gp.obj[exit.y][exit.x] = new Block(gp, exit.x*gp.tileSize + (gp.tileSize+gp.tileSize/2), exit.y*gp.tileSize + (2*gp.tileSize + (gp.tileSize/2)), exit.y, exit.x, "exit");
        for(int i=1; i<numBlock; i++){  // inizializzamo l'array vuoto cosi che dopo mettiamo 
            Point position = avPos.get(i);
            int tileCol = position.x;
            int tileRow = position.y;

            gp.obj[tileRow][tileCol] = new Block(gp, tileCol*gp.tileSize + (gp.tileSize+gp.tileSize/2), tileRow*gp.tileSize + (2*gp.tileSize + (gp.tileSize/2)), tileRow, tileCol, avPowerUp.get(i));
            
        }
    }

    //avaliable position with distinguished x and y saved ina 
    /*
    public void setBlocks(){
        int numBlock = (int)(Math.random()*(40-33))+33;  // 33-40 blocchi a random
        System.out.println(numBlock);  // da eliminare
        numBlock = 45;
        System.out.println("Prendendo posizioni disponibili");
        ArrayList<Integer> avPos = availablePos();
        ArrayList<String> avPowerUp = getPowerUp(numBlock);
        
        for(int i=0; i<avPos.size(); i++){
            System.out.println(avPos.get(i));
        } 
        System.out.println("Inizializzando Array oggetti");
        for(int row=0; row < gp.maxGameRow; row++){
            for(int col=0; col < gp.maxGameCol; col++){
                gp.obj[row][col] = null;
            }
        }
        Collections.shuffle(avPos);  // randomizza le posizioni disponibili
        Collections.shuffle(avPowerUp);
        
        // caricando l'uscita nel primo posto della lista
        int position = avPos.get(0);
        int tileCol = position / 10;
        int tileRow = position % 10;
        gp.obj[]
        gp.obj.set(1, new Block(gp, tileCol*gp.tileSize + (gp.tileSize+gp.tileSize/2), tileRow*gp.tileSize + (2*gp.tileSize + (gp.tileSize/2)), tileCol, tileRow, "exit", position));
        for(int i = 1; i< numBlock; i++){
            //System.out.println("Caricando blocchi nell'array oggetti");
            position = avPos.get(i);
            tileCol = position / 10;  // ci permette di prendere da un numero soltanto sia la X che la Y
            tileRow = position % 10;  // ad esempio se x=3, y=6 -> 3*10+6 = 36 -> 36/10 = 3 | 36%10 = 6
            
            while(true){  // controllo della posizione se non è gia stata presa
                tileCol = (int)(Math.random()*(13)); // 0-12
                tileRow = (int)(Math.random()*(10)); // 0-9
                if(tileM.houseTileNum[tileCol][tileRow] != 3 && tileM.houseTileNum[tileCol][tileRow] != -1 && tileCol+tileRow != 0){  // se non è un palazzo o non è gia preso
                    if(tileCol == 0 && tileRow == 0)  // pos vicino alla partenza del player 
                        break; // esci dal loop
                }
            }
            tileM.houseTileNum[tileCol][tileRow] = 16;  // lo imposta come blocco oggetto dei tile
            gp.obj.set(position, new Block(gp, tileCol*gp.tileSize + (gp.tileSize+gp.tileSize/2), tileRow*gp.tileSize + (2*gp.tileSize + (gp.tileSize/2)), tileCol, tileRow, avPowerUp.get(position), position));
        }
        for(int i=0; i<130; i++){  // inizializzamo l'array vuoto cosi che dopo mettiamo 
            if(gp.obj.get(i) != null)
                System.err.println(gp.obj.get(i).x+" "+gp.obj.get(i).y);
        } 
    } */

    public ArrayList<String> getPowerUp(int max){
        ArrayList<String> powerUpList = new ArrayList<>();
        System.out.println("Generando Powerup");
        for(int i=0; i<max; i++){
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
            for (int j = 0; j < powerUps.length; j++) {
                cumulativeProbability += probabilities[j];
                if (randomNumber <= cumulativeProbability) {
                    powerUpList.add(powerUps[j]); // ritorna il powerUp all'indice i
                }
            }
        }
        return powerUpList;
    }
}
