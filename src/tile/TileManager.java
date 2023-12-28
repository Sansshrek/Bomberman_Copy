package tile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.Rectangle;

import javax.imageio.ImageIO;

import main.GamePanel;
import objects.Bomb;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;  // array di tiles che indica all'immagine 
    int wallsNum = 43;
    int contHouse = 0;
    public Rectangle houseHitbox[][];
    public int houseTileNum[][];
    public int groundTileNum[][];  // dove viene salvata la mappa in stile numerico per indicare a quale tiles si riferisce
    public int wallsTileNum[][];  // dove viene salvata la mappa in stile numerico per indicare a quale tiles si riferisce

    public TileManager(GamePanel gp){ 
        this.gp = gp;
        tile = new Tile[20]; // array di 10 tile 
        wallsTileNum = new int[gp.maxScreenRow][gp.maxScreenCol];  // mapTileNum salva la matrice di numeri della txt della mappa 
        groundTileNum = new int[gp.maxScreenRow][gp.maxScreenCol];  // mapTileNum salva la matrice di numeri della txt della mappa 
        houseTileNum = new int[gp.maxScreenRow][gp.maxScreenCol];
        houseHitbox = new Rectangle[gp.maxGameRow][gp.maxGameCol];

        System.out.println("Prendendo l'immagine dei Tiles");  // da eliminare
        getTileImage();

    }

    public void setupTile(){
        for(int row=0; row<gp.maxGameRow; row++){  // 13 è il massimo dei tiles della mappa per la x
            for(int col=0; col<gp.maxGameCol; col++){  // 10 è il massimo dei tiles della mappa per la y
                houseHitbox[row][col] = null;
                houseTileNum[row][col] = 0;  // tile trasparente
            }
        }
        contHouse = 0;  // resetto il contatore delle case
        
        System.out.println("Caricando la mappa delle mura"); // da eliminare
        loadMap("../res/map/walls01.txt", "walls");
        System.out.println("Caricando la mappa del pavimento"); // da eliminare
        loadMap("../res/map/ground01.txt", "ground");
        System.out.println("Generando palazzi random");
        generateHouse();  // genera le case random 

    }

    public void getTileImage(){
        try {
            tile[0] = new Tile();  //  transparent tile
            tile[0].setImage(ImageIO.read(getClass().getResourceAsStream("../res/tiles/wall/empty.png")), gp.scale);

            tile[1] = new Tile();
            tile[1].setImage(ImageIO.read(getClass().getResourceAsStream("../res/tiles/floor01.png")), gp.scale);  // pavimento

            tile[2] = new Tile();
            tile[2].setImage(ImageIO.read(getClass().getResourceAsStream("../res/tiles/floor02.png")), gp.scale);  // pavimento con ombra

            tile[3] = new Tile();
            tile[3].setImage(ImageIO.read(getClass().getResourceAsStream("../res/tiles/building.png")), gp.scale);  // palazzo indistruttibile
            // tile[3].setImage(ImageIO.read(getClass().getResourceAsStream("../res/tiles/test.png")), gp.scale);  // blocco indistruttibile
            tile[3].collision = true;

            tile[4] = new Tile();
            tile[4].setImage(ImageIO.read(getClass().getResourceAsStream("../res/tiles/wall/wall_up01.png")), gp.scale);  // mura sopra
            tile[4].collision = true;

            tile[5] = new Tile();
            tile[5].setImage(ImageIO.read(getClass().getResourceAsStream("../res/tiles/wall/wall_up02.png")), gp.scale);
            tile[5].collision = true;

            tile[6] = new Tile();
            tile[6].setImage(ImageIO.read(getClass().getResourceAsStream("../res/tiles/wall/wall_up03.png")), gp.scale);
            tile[6].collision = true;

            tile[7] = new Tile();
            tile[7].setImage(ImageIO.read(getClass().getResourceAsStream("../res/tiles/wall/wall_dw01.png")), gp.scale);  // mura sotto
            tile[7].collision = true;

            tile[8] = new Tile();
            tile[8].setImage(ImageIO.read(getClass().getResourceAsStream("../res/tiles/wall/wall_dw02.png")), gp.scale);
            tile[8].collision = true;

            tile[9] = new Tile();
            tile[9].setImage(ImageIO.read(getClass().getResourceAsStream("../res/tiles/wall/wall_dw03.png")), gp.scale);
            tile[9].collision = true;

            tile[10] = new Tile();
            tile[10].setImage(ImageIO.read(getClass().getResourceAsStream("../res/tiles/wall/wall_sx01.png")), gp.scale);  // mura sinistra
            tile[10].collision = true;

            tile[11] = new Tile();
            tile[11].setImage(ImageIO.read(getClass().getResourceAsStream("../res/tiles/wall/wall_sx02.png")), gp.scale);
            tile[11].collision = true;

            tile[12] = new Tile();
            tile[12].setImage(ImageIO.read(getClass().getResourceAsStream("../res/tiles/wall/wall_sx03.png")), gp.scale);
            tile[12].collision = true;

            tile[13] = new Tile();
            tile[13].setImage(ImageIO.read(getClass().getResourceAsStream("../res/tiles/wall/wall_dx01.png")), gp.scale);  // mura destra
            tile[13].collision = true;

            tile[14] = new Tile();
            tile[14].setImage(ImageIO.read(getClass().getResourceAsStream("../res/tiles/wall/wall_dx02.png")), gp.scale);
            tile[14].collision = true;

            tile[15] = new Tile();
            tile[15].setImage(ImageIO.read(getClass().getResourceAsStream("../res/tiles/wall/wall_dx03.png")), gp.scale);
            tile[15].collision = true;

            tile[16] = new Tile();  // blocco oggetto
            tile[16].collision = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath, String type){
        // ALCUNE CASE SONO CASUALI

        try {
            InputStream is = getClass().getResourceAsStream(filePath) ;  // importiamo il file txt
            BufferedReader br = new BufferedReader(new InputStreamReader(is));  // e lo leggiamo

            int col = 0;
            int row = 0;

            while(col < gp.maxScreenCol && row < gp.maxScreenRow){
                String line = br.readLine();  // leggiamo una singola riga

                while(col < gp.maxScreenCol){
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
                if(col == gp.maxScreenCol){
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
                }else if(row+col != 0 && row+col != 1){  // se non è vicino alla posizione default del player
                    randomHouse.add(new Point(col, row));  // aggiungo la casa alle posizioni disponibili per quelle random
                }
            }
        }

        Collections.shuffle(randomHouse); // randomizziamo dove possono stare i palazzi
        for(int i=0; i<8; i++){  // numero di case fuori dal grid normale
            Point housePoint = randomHouse.get(i);  // prendo la posizione disponibile della casa
            houseTileNum[housePoint.y][housePoint.x] = 3;  // e la creo
            if(housePoint.y+1 < gp.maxGameRow){ // se la posizione sotto la casa non esce dalla mappa giocabile
                groundTileNum[housePoint.y+1][housePoint.x] = 2;  // allora rende il blocco sulla mappa ground sotto la casa come un tile "pavimento con ombra"
            }
        }
    }

    public boolean isHouse(int x, int y) {
        // Assicurati che la posizione x e y sia all'interno dei limiti della mappa
        int xBlock = (((x - (gp.tileSize+gp.tileSize/2)))/gp.tileSize);
        int yBlock = ((y - (2*gp.tileSize + (gp.tileSize/2)))/gp.tileSize );
        // System.out.print("x: "+xBlock+" y: "+yBlock); // da eliminare
        if (xBlock >= 0 && xBlock < gp.maxScreenCol && yBlock >= 0 && yBlock < gp.maxScreenRow) {
            // System.out.println(" block "+blockTileNum[xBlock][yBlock]);  // da eliminare
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


        while(col < gp.maxScreenCol && row < gp.maxScreenRow){
            int tileNum = 0;

            if(type == "ground")
                tileNum = groundTileNum[row][col];  // salviamo il numero della matrice del pavimento
            if(type == "house")
                tileNum = houseTileNum[row][col];
            if(type == "walls")
                tileNum = wallsTileNum[row][col];  // salviamo il numero della matrice del muro

            g2.drawImage(tile[tileNum].image, x, y, gp.tileSize, gp.tileSize, null);
            if(tileNum == 3 && contHouse <= wallsNum){  // se è un palazzo
                houseHitbox[row][col] = new Rectangle(x, y, gp.tileSize, gp.tileSize);  // aggiungo la sua hitbox
                // System.out.println("Cont "+cont);
                contHouse++;
            }
            x += gp.tileSize; // aumenta di 16 la posizione di partenza di dove disegna il blocco
            col++;
            if(col == gp.maxScreenCol){  // appena raggiunge la fine della riga da disegnare
                col = 0;
                x = startWidth;
                row++;
                y += gp.tileSize;
            }
        }
    }
}