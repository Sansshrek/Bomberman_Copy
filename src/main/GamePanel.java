package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.awt.Point;
import java.util.concurrent.*;

import javax.swing.JPanel;

import entity.Enemy;
import entity.EnemyType;
import entity.Entity;
import entity.EntityManager;
import entity.EntityObserver;
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
    public final int screenWidth = tileSize * maxScreenCol-tileSize;  // 720 pixels
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
    // KeyHandler keyH = KeyHandler.getInstance();
    public KeyHandler keyH = KeyHandler.getInstance();
    Thread gameThread;
    public BombHandler bombH = new BombHandler(this, tileSize);
    public Player player = new Player(this);
    public AssetSetter aSetter = new AssetSetter(this, tileM);
    // public ArrayList<SuperObject> obj = new ArrayList<>();
    public SuperObject obj[][];
    public ArrayList<Enemy> enemy = new ArrayList<>();
    public int entityCounter;
    public LevelType[] listaLivelli= {LevelType.LEVEL1, LevelType.LEVEL2, LevelType.LEVEL3};
    int levelIndex = 0;
    public int enemyNum;
    // Stato di Gioco
    public boolean pauseGame = false;
    public Panel currentPanel = new StartMenu(this);
    boolean startTransition = false, closeTransition = false;
    int alphaVal = 255;

    // test da eliminare
    boolean checkSetup, checkGameOn;
    
    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);  // tutti i disegni di questo componente vengono fatti in un buffer di disegno (per migliorare le performance)
        this.addKeyListener(keyH);
        this.setFocusable(true);  // cosi il GamePanel è "concentrato" a ricevere input di tastiera
        this.obj = new SuperObject[maxGameRow][maxGameCol]; 
        checkSetup = false;
        checkGameOn = false;
        
        // setupGame();
    }

    public void setupGame(){  // imposto il gioco da capo
        if(!checkSetup){
            checkSetup = true;
            hud.resetTimer();
            player.registerObserver(cChecker);
            player.setPlayerDefaultValues();
            tileM.setupTile();

            for(int row=0; row<maxGameRow; row++){  // reset obj
                for(int col=0; col<maxGameCol; col++){
                    obj[row][col] = null;
                }
            }

            System.out.println("Caricando i blocchi distruttibili");  // da eliminare
            aSetter.setMatrixBlocks();
            enemy.clear();  // resetto la lista dei nemici
            LevelType livello = listaLivelli[levelIndex];
            ArrayList<EnemyType> listaNemici = livello.enemyList;
            enemyNum = listaNemici.size();
            entityCounter = 1;
            int counter = 0;
            for(EnemyType enemyType: listaNemici){
                enemy.add(new Enemy(this, entityCounter, enemyType));  // aggiungo 3 nemici
                entityCounter++;
                enemy.get(counter).registerObserver(cChecker);
                counter++;
            }
            // startTransition = true;
            // senza il delay di 1 secondo parte direttamente il gioco senza far vedere la transizione
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);  // crea una nuova pool di thread di una grandezza
            Runnable task = () -> startingTransition();  // crea una nuova funzione eseguibile che setta checkGameOn a true
        
            executor.schedule(task, 1, TimeUnit.SECONDS);  // esegue la task dopo 2 secondi in parallelo col programma
            executor.shutdown();  // chiude la pool di thread 
            // checkGameOn = true;
        }
    }
    public void Test(){
        System.out.println("---DIOCANE__--");
    }/*
    import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        Runnable task = () -> System.out.println("Esecuzione ritardata!");

        int delay = 5;
        executor.schedule(task, delay, TimeUnit.SECONDS);
        executor.shutdown();
    }
}*/

    public void startingTransition(){
        alphaVal = 255;  // reimposta l'alphaVal
        startTransition = true;
        checkGameOn = false;
    }
    public void closingTransition(){
        alphaVal = 0;  // reimposta l'alphaVal
        closeTransition = true;
        // checkSetup = false;
        checkGameOn = false;
    }

    public void drawTransition(Graphics2D g2){
        System.out.println("Drawing "+alphaVal+" transition "+startTransition+ " "+closeTransition);
        g2.setColor(new Color(0, 0, 0, alphaVal));  // setta il colore con alpha
        g2.fillRect(0, 0, screenWidth, screenHeight);  // disegna il background
    }

    public void resetLevel(){
        closingTransition();
    }

    public void nextLevel(){
        if(enemyNum == 0){  // se sono finiti i nemici sulla mappa
            checkSetup = false;  // reimposta i valori di setup
            checkGameOn = false;
            levelIndex++;  // vai al prossimo livello
            if(levelIndex == listaLivelli.length){  // per ora quando finisce i livelli resetta il gioco
                levelIndex = 0;
                resetLevel();
            }
            setupGame();  // per ora lascio che resetta il livello
        }
    }

    public void updateKey(){
        if(keyH.pausePressed){ //se viene premuto Enter
            if(pauseGame){  // se il gioco è gia in pausa fa ricominciare il gioco
                pauseGame = false;
            }else{  // se il gioco non è in pausa allora lo ferma
                pauseGame = true;
            }
            keyH.pausePressed = false;
        }
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
        hud.drawTime(g2);
        g2.drawImage(hud.image, 0, 0, 256*scale, hudHeight, null);  // poi l'HUD
        hud.drawClock(g2);

        // Stampa il punteggio
        hud.drawScore(g2, player.score);
        hud.drawLife(g2, player.lifeNumber);
        if(hud.clockLeft == 1){  // se finisce il tempo
            System.out.println("---Finito il tempo---\n\n");
            resetLevel();  // resetta il gioco
        }
    }

    public void update(){
        if(!checkGameOn && currentPanel != null){
            currentPanel.chooseOptions(this);
        }else{
            updateKey();
        }
        if(startTransition && alphaVal>0){  // se è partita la transizione diminuisce l'alphaVal per la trasparenza
            alphaVal -= 5;
        }else if(startTransition && alphaVal==0){  // se è finita la transizione
            startTransition = false;  // reimposta la transizione
            checkGameOn = true;  // fa partire il gioco
        }
        if(closeTransition && alphaVal<255){  // se è partita la transizione di chiusura
            alphaVal += 5;  // aumenta l'alphaVal per la trasparenza
        }else if(closeTransition && alphaVal==255){  // se è finita la transizione di chiusura
            closeTransition = false;
            checkSetup = false;  // ripristina le variabili del setup
            setupGame();  // per ora lascio che resetta il livello
        }
        
        if(checkGameOn && !pauseGame){  // se il gioco puo partire e il player non ha fermato il gioco
            hud.updateTime();
            player.update();
            for(Enemy entity: enemy){
                if(!entity.extinguished)
                    entity.update();
            }
            cChecker.checkEntity();
            
            for(int row=0; row < maxGameRow; row++){
                for(int col=0; col < maxGameCol; col++){
                    if(obj[row][col] != null && !(obj[row][col] instanceof Bomb)){
                        obj[row][col].update();
                    }
                }
            }
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);  // utilizza il metodo della classe parente di GamePanel quindi JPanel (GamePanel extends JPanel)
        Graphics2D g2 = (Graphics2D) g;  // estende la classe Graphics per aggiungere piu controlli sulla geometria, trasformazione delle cordinate, gestione colori e layout di testo
        if(checkSetup){  // se ha inizializzato il gioco
            paintGame(g2);
            if(!checkGameOn) // se non è ancora partito il gioco
                drawTransition(g2);  // disegna la transizione
        }
        if(!checkGameOn && closeTransition){
            drawTransition(g2);
        }
        // if(startTransition || closeTransition) // se non è ancora partito il gioco
            // drawTransition(g2);  // disegna la transizione
        if (!checkGameOn && currentPanel != null){
            currentPanel.drawPanel(g2, this);
        }
        g2.dispose();  // rimuove il contesto grafico e rilascia ogni risorsa di sistema che sta usando
    }

    public void paintGame(Graphics2D g2){
        player.g2 = g2;

        for(Enemy entity: enemy){  // itero i nemici
            if(!entity.extinguished){
                entity.g2 = g2;
            }
        }
        bombH.g2 = g2;
        
        g2.setColor(Color.black);
        g2.fillRect(0, 0, this.screenWidth, this.screenHeight);  // disegna il background

        tileM.drawMap(g2, 24*this.scale, this.hudHeight+(8*this.scale), "ground");  // prima il pavimento
        tileM.drawMap(g2, 24*this.scale, this.hudHeight+(8*this.scale), "house");  // poi i palazzi
        tileM.drawMap(g2, -8*this.scale, this.hudHeight-(8*this.scale), "walls");  // poi le mura

        
        g2.setColor(Color.RED);  // da eliminare
        for(int row=0; row<maxGameRow; row++){
            for(int col=0; col<maxGameCol; col++){
                if(tileM.houseHitbox[row][col] != null)
                    g2.draw(tileM.houseHitbox[row][col]);
            }
        }

        for(int row=0; row < maxGameRow; row++){
            for(int col=0; col < maxGameCol; col++){
                if(obj[row][col] != null){
                    obj[row][col].draw(g2, this);
                    g2.setColor(Color.YELLOW);
                    g2.draw(obj[row][col].hitbox);
                }
            }
        }

        bombH.updateBomb();
        // player.draw();  // poi il player
        player.drawBehaviour.draw(player);
        ArrayList<Enemy> enemyDelete = new ArrayList<>();  // lista di nemici temporanea da eliminare nel caso dalla lista enemy originale
        for(Enemy entity: enemy){
            if(!entity.extinguished){  // se ancora non è esploso del tutto
                entity.draw();  // disegna l'enemy
                // entity.drawBehaviour.draw(entity);
            }else{
                enemyDelete.add(entity);  // aggiunge il nemico morto alla lista temporanea dei nemici da eliminare
            }
        }
        for(Enemy entity: enemyDelete){
            enemy.remove(entity);  // rimuove dalla lista originale l'enemy morto
        }
        drawHUD(g2);
    }

}
