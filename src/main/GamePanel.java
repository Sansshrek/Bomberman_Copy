package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JPanel;

import entity.Enemy;
import entity.Entity;
import entity.Player;
import objects.Bomb;
import objects.BombHandler;
import objects.SuperObject;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable{
    // Screen Settings
    final int originalTileSize = 16;  //16x16 tile
    public final int scale = 3;
    

    public final int tileSize = originalTileSize * scale;  // 48x48 tile (è public cosi la classe Player puo accedere al valore)
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 14;
    public final int screenWidth = tileSize * maxScreenCol-16*this.scale;  // 768 pixels
    public final int screenHeight = tileSize * maxScreenRow;  // 672 pixels
    public final int hudHeight = 32*scale;
    public final int maxGameCol = 13;
    public final int maxGameRow = 11;
    public final int gameBorderLeftX = (tileSize/2)+tileSize;
    public final int gameBorderRightX = gameBorderLeftX + 13*tileSize;
    public final int gameBorderUpY = (tileSize/2)+hudHeight;
    public final int gameBorderDownY = gameBorderUpY + 11*tileSize;

    // World Settings

    // FPS 
    int FPS = 60;

    public HUD hud = new HUD(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    public TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    public BombHandler bombH = new BombHandler(tileSize);
    public Player player = new Player(this, keyH);
    public Enemy enemy = new Enemy(this, 10,0);
    public Enemy enemy2 = new Enemy(this, 10,3);
    public AssetSetter aSetter = new AssetSetter(this, tileM);
    // public ArrayList<SuperObject> obj = new ArrayList<>();
    public SuperObject obj[][];
    public ArrayList<Entity> entity = new ArrayList<>();

    // Stato di Gioco
    public final int menuState = 5;
    
    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);  // tutti i disegni di questo componente vengono fatti in un buffer di disegno (per migliorare le performance)
        this.addKeyListener(keyH);
        this.setFocusable(true);  // cosi il GamePanel è "concentrato" a ricevere input di tastiera
        this.obj = new SuperObject[maxGameRow][maxGameCol]; 
    }

    public void setupGame(){
        System.out.println("Caricando i blocchi distruttibili");  // da eliminare
        aSetter.setMatrixBlocks();
    }
    public int getTileSize(){
        return tileSize;
    }
    public int getScale(){
        return scale;
    }
    public int getOgTileSize(){
        return originalTileSize;
    }

    public void startGameThread(){
        gameThread = new Thread(this);  //this = la classe GamePanel al costruttore di Thread
        gameThread.start();  // chiama automaticamente il metodo run()
    }

    @Override
    // METODO FPS: "Delta/Accumulator" Method  (piu accurato)
    public void run(){

        double drawInterval = 1000000000/FPS; // 1 milione di nanosecondi / FPS | 0.01666 secondi
        double delta = 0;
        double lastTime = System.nanoTime();
        long currentTime;

        while(gameThread != null){

            currentTime = System.nanoTime();  // controlliamo il tempo corrente

            delta += (currentTime - lastTime) / drawInterval;  // sottraiamo il tempo corrente dal tempo precedente (quanto tempo è passato) dividendo per l'intervallo stabilito
            lastTime = currentTime;  // il tempo precedente diventa il tempo corrente
            // a ogni ciclo aggiungiamo il tempo precedente diviso al drawInterval al delta e quando il delta raggiunge questo drawInterval allora aggiornamo lo schermo e resettiamo il delta
            if(delta >= 1){
                update();
                repaint();
                delta--;
            }
        }
    }

    public void drawHUD(Graphics2D g2){
        
        g2.drawImage(hud.image, 0, 0, 256*scale, hudHeight, null);  // poi l'HUD

        // Stampa il punteggio
        hud.drawScore(g2, player.score);
        hud.drawLife(g2, player.lifeNumber);

    }

    public void update(){
        player.update();
        enemy.update();
        enemy2.update();
        
        
        for(int row=0; row < maxGameRow; row++){
            for(int col=0; col < maxGameCol; col++){
                if(obj[row][col] != null){
                    obj[row][col].update();
                }
            }
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);  // utilizza il metodo della classe parente di GamePanel quindi JPanel (GamePanel extends JPanel)
        Graphics2D g2 = (Graphics2D) g;  // estende la classe Graphics per aggiungere piu controlli sulla geometria, trasformazione delle cordinate, gestione colori e layout di testo
        player.g2 = g2;
        enemy.g2 = g2;
        enemy2.g2 = g2;
        bombH.g2 = g2;

        g2.setColor(Color.black);
        g2.fillRect(0, 0, this.maxScreenRow, this.maxScreenCol);  // disegna il background

        tileM.drawMap(g2, 24*this.scale, this.hudHeight+(8*this.scale), "ground");  // prima il pavimento
        tileM.drawMap(g2, -8*this.scale, this.hudHeight-(8*this.scale), "walls");  // poi le mura

        g2.setColor(Color.RED);
        for(Rectangle hitbox: tileM.houseHitbox){
            g2.draw(hitbox);
        }

        for(int row=0; row < maxGameRow; row++){
            for(int col=0; col < maxGameCol; col++){
                if(obj[row][col] != null){
                    if(obj[row][col] instanceof Bomb){
                        Bomb bomb = (Bomb) obj[row][col];
                        obj[row][col].draw(g2, this);
                    }
                    obj[row][col].draw(g2, this);
                    g2.setColor(Color.YELLOW);
                    g2.draw(obj[row][col].hitbox);
                }
            }
        }

        bombH.updateBomb();
        player.draw();  // poi il player
        enemy.draw();
        enemy2.draw();
        drawHUD(g2);

        g2.dispose();  // rimuove il contesto grafico e rilascia ogni risorsa di sistema che sta usando
    }

}
