package tile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;  // array di tiles che indica all'immagine 
    public int blockTileNum[][];
    public int groundTileNum[][];  // dove viene salvata la mappa in stile numerico per indicare a quale tiles si riferisce
    public int wallsTileNum[][];  // dove viene salvata la mappa in stile numerico per indicare a quale tiles si riferisce

    public TileManager(GamePanel gp){ 
        this.gp = gp;
        tile = new Tile[20]; // array di 10 tile 
        wallsTileNum = new int[gp.maxScreenCol][gp.maxScreenRow];  // mapTileNum salva la matrice di numeri della txt della mappa 
        groundTileNum = new int[gp.maxScreenCol][gp.maxScreenRow];  // mapTileNum salva la matrice di numeri della txt della mappa 
        blockTileNum = new int[gp.maxScreenCol][gp.maxScreenRow];

        getTileImage();
        loadMap("../res/map/walls01.txt", "walls");
        loadMap("../res/map/ground01.txt", "ground");

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
            tile[3].setImage(ImageIO.read(getClass().getResourceAsStream("../res/tiles/building.png")), gp.scale);  // blocco indistruttibile
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
                    if(line != null){
                        String numbers[] = line.split(" "); // dividiamo la stringa della riga in un'array di numeri
                        int num = Integer.parseInt(numbers[col]);
                        if(type == "ground"){
                            groundTileNum[col][row] = num;  // salviamo il numero nella matrice della pavimento
                            blockTileNum[col][row] = num;
                        }
                        if(type == "walls")
                            wallsTileNum[col][row] = num;  // salviamo il numero nella matrice del muro
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

    public boolean isHouse(int x, int y) {
        // Assicurati che la posizione x e y sia all'interno dei limiti della mappa
        if (x >= gp.gameBorderLeftX && x < 13*gp.tileSize && y >= gp.gameBorderUpY && y < 11*gp.tileSize) {
            if(groundTileNum[(((x - (gp.tileSize+gp.tileSize/2)))/gp.tileSize)][((y - (2*gp.tileSize + (gp.tileSize/2)))/gp.tileSize )] == 3){
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
                tileNum = groundTileNum[col][row];  // salviamo il numero della matrice del pavimento
            if(type == "walls")
                tileNum = wallsTileNum[col][row];  // salviamo il numero della matrice del muro

            g2.drawImage(tile[tileNum].image, x, y, gp.tileSize, gp.tileSize, null);

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