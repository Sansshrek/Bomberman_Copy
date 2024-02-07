package tile;

import java.awt.Graphics2D;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.Rectangle;

import javax.imageio.ImageIO;

import main.GamePanel;

public class TileManager {
    GamePanel gp;
    public ArrayList<Tile> currentTiles, firstWorld, firstBoss, secondWorld;  // array di tiles che indica all'immagine 
    public Rectangle houseHitbox[][];
    public int houseTileNum[][];
    public int groundTileNum[][];  // dove viene salvata la mappa in stile numerico per indicare a quale tiles si riferisce
    public int wallsTileNum[][];  // dove viene salvata la mappa in stile numerico per indicare a quale tiles si riferisce
    int drawScreenCol;

    public TileManager(GamePanel gp){ 
        this.gp = gp;
        firstWorld = getTileImage("firstWorld");
        secondWorld = getTileImage("secondWorld");
        firstBoss = getTileImage("firstBoss");
        // poiche la stampa delle mura avviene mezzo blocco fuori dal jpanel sia a destra che a sinistra allora hanno un blocco in piu per il disegno
        drawScreenCol = gp.maxScreenCol+1;
        currentTiles = new ArrayList<>(firstWorld);  // inizializza l'array con la prima mappa inizialmente
        wallsTileNum = new int[gp.maxScreenRow][drawScreenCol];  // mapTileNum salva la matrice di numeri della txt della mappa 
        groundTileNum = new int[gp.maxScreenRow][drawScreenCol];  // mapTileNum salva la matrice di numeri della txt della mappa 
        houseTileNum = new int[gp.maxScreenRow][drawScreenCol];
        houseHitbox = new Rectangle[gp.maxGameRow][gp.maxGameCol];
    }

    public void setupTile(){
        // resetta le hitbox delle case e i numeri delle case
        for(int row=0; row<gp.maxGameRow; row++){  // 13 è il massimo dei tiles della mappa per la x
            for(int col=0; col<gp.maxGameCol; col++){  // 10 è il massimo dei tiles della mappa per la y
                houseHitbox[row][col] = null;
                houseTileNum[row][col] = 0;  // tile trasparente
            }
        }
        if(gp.levelType == "firstWorld"){
            currentTiles = firstWorld;  // inizializza l'array con la prima mappa
        }else if(gp.levelType == "firstBoss"){
            currentTiles = firstBoss;  // inizializza l'array con la mappa del primo boss
        }else if(gp.levelType == "secondWorld"  || gp.levelType == "secondBoss"){
            currentTiles = secondWorld;  // inizializza l'array con la seconda mappa
        }
    
        loadMap("../res/map/walls01.txt", "walls");
        loadMap("../res/map/ground01.txt", "ground");
        generateHouse();  // genera le case random 
    }

    public ArrayList<Tile> getTileImage(String resourceDir){
        ArrayList<Tile> tileGetter = new ArrayList<>();
        try {
            //  transparent tile (0)
            tileGetter.add(new Tile(ImageIO.read(getClass().getResourceAsStream("../res/tiles/"+resourceDir+"/wall/empty.png")), gp.tileSize, false));
            // pavimento (1)
            tileGetter.add(new Tile(ImageIO.read(getClass().getResourceAsStream("../res/tiles/"+resourceDir+"/floor01.png")), gp.tileSize, false));
            // pavimento con ombra (2)
            tileGetter.add(new Tile(ImageIO.read(getClass().getResourceAsStream("../res/tiles/"+resourceDir+"/floor02.png")), gp.tileSize, false));
            // palazzo indistruttibile (3)  si potrebbe eliminare
            tileGetter.add(new Tile(ImageIO.read(getClass().getResourceAsStream("../res/tiles/"+resourceDir+"/building.png")), gp.tileSize, true));

            // mura sopra (4, 5, 6)
            tileGetter.add(new Tile(ImageIO.read(getClass().getResourceAsStream("../res/tiles/"+resourceDir+"/wall/wall_up01.png")), gp.tileSize, true));
            tileGetter.add(new Tile(ImageIO.read(getClass().getResourceAsStream("../res/tiles/"+resourceDir+"/wall/wall_up02.png")), gp.tileSize, true));
            tileGetter.add(new Tile(ImageIO.read(getClass().getResourceAsStream("../res/tiles/"+resourceDir+"/wall/wall_up03.png")), gp.tileSize, true));

            // mura sotto (7, 8, 9)
            tileGetter.add(new Tile(ImageIO.read(getClass().getResourceAsStream("../res/tiles/"+resourceDir+"/wall/wall_dw01.png")), gp.tileSize, true));
            tileGetter.add(new Tile(ImageIO.read(getClass().getResourceAsStream("../res/tiles/"+resourceDir+"/wall/wall_dw02.png")), gp.tileSize, true));
            tileGetter.add(new Tile(ImageIO.read(getClass().getResourceAsStream("../res/tiles/"+resourceDir+"/wall/wall_dw03.png")), gp.tileSize, true));

            // mura sinistra (10, 11, 12)
            tileGetter.add(new Tile(ImageIO.read(getClass().getResourceAsStream("../res/tiles/"+resourceDir+"/wall/wall_sx01.png")), gp.tileSize, true));
            tileGetter.add(new Tile(ImageIO.read(getClass().getResourceAsStream("../res/tiles/"+resourceDir+"/wall/wall_sx02.png")), gp.tileSize, true));
            tileGetter.add(new Tile(ImageIO.read(getClass().getResourceAsStream("../res/tiles/"+resourceDir+"/wall/wall_sx03.png")), gp.tileSize, true));

            // mura destra (13, 14, 15)
            tileGetter.add(new Tile(ImageIO.read(getClass().getResourceAsStream("../res/tiles/"+resourceDir+"/wall/wall_dx01.png")), gp.tileSize, true));
            tileGetter.add(new Tile(ImageIO.read(getClass().getResourceAsStream("../res/tiles/"+resourceDir+"/wall/wall_dx02.png")), gp.tileSize, true));
            tileGetter.add(new Tile(ImageIO.read(getClass().getResourceAsStream("../res/tiles/"+resourceDir+"/wall/wall_dx03.png")), gp.tileSize, true));

            // blocco oggetto (16)
            tileGetter.add(new Tile(null, gp.tileSize, true));
            // test mura dx (17, 18)
            tileGetter.add(new Tile(ImageIO.read(getClass().getResourceAsStream("../res/tiles/"+resourceDir+"/wall/wall_dx04.png")), gp.tileSize, true));
            tileGetter.add(new Tile(ImageIO.read(getClass().getResourceAsStream("../res/tiles/"+resourceDir+"/wall/wall_dx05.png")), gp.tileSize, true));
            // test mura sx (19, 20)
            tileGetter.add(new Tile(ImageIO.read(getClass().getResourceAsStream("../res/tiles/"+resourceDir+"/wall/wall_sx04.png")), gp.tileSize, true));
            tileGetter.add(new Tile(ImageIO.read(getClass().getResourceAsStream("../res/tiles/"+resourceDir+"/wall/wall_sx05.png")), gp.tileSize, true));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tileGetter;
    }

    public void loadMap(String filePath, String type){
        // ALCUNE CASE SONO CASUALI

        try {
            InputStream is = getClass().getResourceAsStream(filePath) ;  // importiamo il file txt
            BufferedReader br = new BufferedReader(new InputStreamReader(is));  // e lo leggiamo

            int col = 0;
            int row = 0;

            while(col < drawScreenCol && row < gp.maxScreenRow){
                String line = br.readLine();  // leggiamo una singola riga

                while(col < drawScreenCol){
                    if(line != null){  // è uno spazio disponibile (no casa/nemico/spazio player)
                        String numbers[] = line.split(" "); // dividiamo la stringa della riga in un'array di numeri
                        int num = Integer.parseInt(numbers[col]);
                        if(type == "ground"){
                            groundTileNum[row][col] = num;  // salviamo il numero nella matrice della pavimento
                        }
                        if(type == "walls")
                            wallsTileNum[row][col] = num;  // salviamo il numero nella matrice del muro
                    }
                    col++;
                }
                if(col == drawScreenCol){
                    col = 0;
                    row++;
                }
            }
            br.close(); // chiudiamo il lettore 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateHouse(){
        ArrayList <Point> randomHouse = new ArrayList<>();
        
        for(int row=0; row<gp.maxGameRow; row++){
            for(int col=0; col<gp.maxGameCol; col++){
                if((row%2) != 0 && (col%2) != 0){  // se row e col sono dispari crea una casa (grid base di bomberman)
                    houseTileNum[row][col] = 3;  // creo la casa
                    houseHitbox[row][col] = new Rectangle(col* gp.tileSize + 72, row* gp.tileSize + 120, gp.tileSize, gp.tileSize);  // aggiungo la sua hitbox
                }else if(row+col != 0 && row+col != 1){  // se non è vicino alla posizione default del player
                    randomHouse.add(new Point(col, row));  // aggiungo la casa alle posizioni disponibili per quelle random
                }
            }
        }
        
        if(gp.levelType == "firstWorld" || gp.levelType == "secondWorld"){  // se sono il prmo o il secondo mondo settiamo le case random
            Collections.shuffle(randomHouse); // randomizziamo dove possono stare i palazzi
            for(int i=0; i<8; i++){  // numero di case fuori dal grid normale
                Point housePoint = randomHouse.get(i);  // prendo la posizione disponibile della casa
                houseTileNum[housePoint.y][housePoint.x] = 3;  // e la creo
                houseHitbox[housePoint.y][housePoint.x] = new Rectangle(housePoint.x* gp.tileSize + 72, housePoint.y* gp.tileSize + 120, gp.tileSize, gp.tileSize);  // aggiungo la sua hitbox
                if(housePoint.y+1 < gp.maxGameRow){ // se la posizione sotto la casa non esce dalla mappa giocabile
                    groundTileNum[housePoint.y+1][housePoint.x] = 2;  // allora rende il blocco sulla mappa ground sotto la casa come un tile "pavimento con ombra"
                }
            }
        }
    }

    public boolean isHouse(int x, int y) {
        // Assicurati che la posizione x e y sia all'interno dei limiti della mappa
        int xBlock = (((x - (gp.tileSize+gp.tileSize/2)))/gp.tileSize);
        int yBlock = ((y - (2*gp.tileSize + (gp.tileSize/2)))/gp.tileSize );
        if (xBlock >= 0 && xBlock < drawScreenCol && yBlock >= 0 && yBlock < gp.maxScreenRow) {
            if(houseTileNum[yBlock][xBlock] == 3){
                return true; // Restituisci true se il tile corrispondente alla posizione (x, y) è un palazzo
            }
        } 
        return false;
    }

    public void drawMap(Graphics2D g2, int startWidth, int startHeight, String type){
        int col = 0;
        int row = 0;
        int x = startWidth;
        int y = startHeight;


        while(col < drawScreenCol && row < gp.maxScreenRow){
            int tileNum = 0;

            if(type == "ground")
                tileNum = groundTileNum[row][col];  // salviamo il numero della matrice del pavimento
            if(type == "house")
                tileNum = houseTileNum[row][col];
            if(type == "walls")
                tileNum = wallsTileNum[row][col];  // salviamo il numero della matrice del muro

            g2.drawImage(currentTiles.get(tileNum).image, x, y, gp.tileSize, gp.tileSize, null);
    
            x += gp.tileSize; // aumenta di 16 la posizione di partenza di dove disegna il blocco
            col++;
            if(col == drawScreenCol){  // appena raggiunge la fine della riga da disegnare
                col = 0;
                x = startWidth;
                row++;
                y += gp.tileSize;
            }
        }
    }
}