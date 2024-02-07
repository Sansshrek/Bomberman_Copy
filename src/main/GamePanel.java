package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.awt.Point;
import java.util.concurrent.*;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import entity.Enemy;
import entity.EnemyType;
import entity.Entity;
import entity.EntityManager;
import entity.EntityObserver;
import entity.Player;
import entity.projectiles.ProjectileHandler;
import objects.Bomb;
import objects.BombHandler;
import objects.SuperObject;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable{
    // User Settings
    // public int userNumber = 0;
    // public UserHandler userH = new UserHandler();

    AudioManager audioM = new AudioManager();
    AudioManager musicM = new AudioManager();

    // Screen Settings
    final int originalTileSize = 16;  //16x16 tile
    public final int scale = 3;
    
    public final int tileSize = originalTileSize * scale;  // 48x48 tile (è public cosi la classe Player puo accedere al valore)
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 14;
    public final int screenWidth = tileSize * maxScreenCol;  // 768 pixels
    public final int screenHeight = tileSize * maxScreenRow;  // 672 pixels
    public final int hudHeight = 32*scale;
    public final int maxGameCol = 13;
    public final int maxGameRow = 11;
    public final int gameBorderLeftX = (tileSize/2)+tileSize;
    public final int gameBorderRightX = gameBorderLeftX + 13*tileSize;
    public final int gameBorderUpY = (tileSize/2)+hudHeight;
    public final int gameBorderDownY = gameBorderUpY + 11*tileSize;
    public final int maxUserNumber = 4;

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
    public LevelType[] levelListEasy = {LevelType.Level1Easy, LevelType.Level2Easy, LevelType.Level2Easy};
    public LevelType[] levelListNormal = {LevelType.Level1, LevelType.Level2, LevelType.Level3, LevelType.Level4, LevelType.Level5, LevelType.Level6, LevelType.Level7, LevelType.Level8};
    public LevelType[] levelListHard = {LevelType.Level1Hard, LevelType.Level2Hard, LevelType.Level3Hard};
    public BufferedImage[] levelNumberImages;
    int levelNumberX = -screenWidth;  // posizione iniziale del numero del livello 
    boolean canDrawLevelNumber = false;
    public int levelIndex = 0, themeIndex = 0;
    public int enemyNum, minBlock, maxBlock;
    // Stato di Gioco
    public boolean pauseGame = false;
    public Panel currentPanel = new StartMenu(this);
    boolean startTransition = false, closeTransition = false;
    int alphaVal = 255;
    public String gameDifficulty = "normal";
    public String levelType = "firstWorld";  // inizializza il tipo di livello al primo mondo

    EnemyType test = EnemyType.PUROPEN;

    // test da eliminare
    boolean checkSetup, checkGameOn;
    
    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);  // tutti i disegni di questo componente vengono fatti in un buffer di disegno (per migliorare le performance)
        this.addKeyListener(keyH);
        this.setFocusable(true);  // cosi il GamePanel è "concentrato" a ricevere input di tastiera
        this.obj = new SuperObject[maxGameRow][maxGameCol]; 
        getLevelImage();
        checkSetup = false;
        checkGameOn = false;
        
        // setupGame();
    }

    void resetGamePanel(){  // funzione che resetta i valori basici del gamePanel per esempio quando si chiama dallo startMenu
        player.gamesPlayed ++;  // aumenta il numero di partite giocate
        checkSetup = false;
        checkGameOn = false;
        alphaVal = 255;
        minBlock = 30;
        maxBlock = 35;
        levelIndex = 0;  // resetta al primo livello del gioco
        themeIndex = 0;
        levelType = "firstWorld";
        currentPanel = null;  // resevtta il pannello corrente
        player.registerObserver(cChecker);
        player.setPlayerDefaultValues();
        Map<String, String> scoreMap = readScore();
        if (scoreMap != null) {
            player.nickname = scoreMap.get("Nk");
            player.avatarColor = scoreMap.get("Av");
        }
        player.getPlayerImage();
        setupGame();  // esegue il setup del gioco
    }

    public void setupGame(){  // imposto il gioco da capo
        if(!checkSetup){
            playSfx(1);  // sound stage intro
            canDrawLevelNumber = true;  // fa partire l'animazione del numero del livello
            checkSetup = true;
            pauseGame = false;
            hud.resetTimer();
            player.resetPlayerGameValue();
            tileM.setupTile();

            for(int row=0; row<maxGameRow; row++){  // reset obj
                for(int col=0; col<maxGameCol; col++){
                    obj[row][col] = null;
                }
            }

            if(levelType != "firstBoss" && levelType != "secondBoss"){  // se non è un boss carica i blocchi distruttibili
                System.out.println("Caricando i blocchi distruttibili");  // da eliminare
                aSetter.setMatrixBlocks(minBlock, maxBlock);
            }
            enemy.clear();  // resetto la lista dei nemici
            cChecker.resetEntities();  // resetta le entita nell'observ
            ArrayList<EnemyType> listaNemici = new ArrayList<>();
            if(gameDifficulty == "easy")
                listaNemici = levelListEasy[levelIndex].getEnemyList();
            else if(gameDifficulty == "normal")
                listaNemici = levelListNormal[levelIndex].getEnemyList();
            else if(gameDifficulty == "hard")
                listaNemici = levelListHard[levelIndex].getEnemyList();
            // ArrayList<EnemyType> listaNemici = livello.getEnemyList(gameDifficulty);
            enemyNum = listaNemici.size();
            entityCounter = 1;
            int counter = 0;
            for(EnemyType enemyType: listaNemici){
                Enemy newEnemy = new Enemy(this, entityCounter, enemyType);
                // imposta i projectileHandler per i boss
                if(newEnemy.type == "knight")
                    newEnemy.projectileHandler = new ProjectileHandler(this, "knight");
                else if(newEnemy.type == "clown")
                    newEnemy.projectileHandler = new ProjectileHandler(this, "clown");
                enemy.add(newEnemy);  // aggiungo i nemici
                entityCounter++;
                enemy.get(counter).registerObserver(cChecker);
                counter++;
            }
            // startTransition = true;
            // senza il delay parte direttamente il gioco senza far vedere la transizione
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);  // crea una nuova pool di thread di una grandezza
            Runnable task = () -> {startingTransition(); playMusic(themeIndex);};  // crea una nuova funzione eseguibile che setta checkGameOn a true e fa partire il suono dello stage intro
        
            executor.schedule(task, 3, TimeUnit.SECONDS);  // esegue la task dopo 2 secondi in parallelo col programma
            executor.shutdown();  // chiude la pool di thread 
            // checkGameOn = true;
        }
    }

    public void getLevelImage(){
        levelNumberImages = new BufferedImage[8];
        try {
            for(int levelI=1; levelI<8; levelI++){  // per quante sprite ci stanno in una direzione
                levelNumberImages[levelI-1] = ImageIO.read(getClass().getResourceAsStream("../res/menu/level "+String.valueOf(levelI)+".png"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
        // System.out.println("Drawing "+alphaVal+" transition "+startTransition+ " "+closeTransition);
        g2.setColor(new Color(0, 0, 0, alphaVal));  // setta il colore con alpha
        g2.fillRect(0, 0, screenWidth, screenHeight);  // disegna il background
    }

    public void resetLevel(){
        if(checkGameOn){  // se il gioco è avviato puoi resettare il livello
            closingTransition();
        }
    }
    public void playerDeath(){  // viene chiamato quando il player perde tutte le vite
        pauseGame = true;
        checkGameOn = false;
        currentPanel = new DeathPanel(this);
    }

    public void nextLevel(){
        if(enemyNum == 0){  // se sono finiti i nemici sulla mappa
            stopMusic();
            playSfx(2); // sound stage clear
            levelIndex++;  // vai al prossimo livello
            if(levelIndex < levelListNormal.length){  // se non è finito il gioco
                minBlock += 2;  // aumenta il numero di blocchi che possono essere sulla mappa
                maxBlock += 2;
                
                if(levelIndex < 3){
                    levelType = "firstWorld";
                    themeIndex = 0;  // theme del primo mondo
                }else if(levelIndex == 3){  // se arriva al livello del primo boss
                    levelType = "firstBoss";
                    themeIndex = 15;  // theme del boss
                }else if(levelIndex > 3 && levelIndex < 7){  // se sono i primi 3 livelli del secondo mondo
                    levelType = "secondWorld";
                    themeIndex = 14;  // theme del secondo mondo
                }else if(levelIndex == 7){  // se arriva al livello del secondo boss
                    levelType = "secondBoss";
                    themeIndex = 15;  // theme del boss
                }
                closingTransition();
            }else{  // quando finisce il gioco
                pauseGame = true;
                checkSetup = false;
                checkGameOn = false;
                currentPanel = new EndPanel(this);
            }
            
        }
    }

    public void updateKey(){
        if(keyH.pausePressed){ //se viene premuto Enter
            playSfx(3);  // sound pausa
            if(pauseGame){  // se il gioco è gia in pausa fa ricominciare il gioco
                pauseGame = false;
            }else{  // se il gioco non è in pausa allora lo ferma
                pauseGame = true;
            }
            keyH.pausePressed = false;
        }
        if(keyH.nextLevelPressed){  // se viene premuto N
            if(checkGameOn){  // s il goico è avviato puoi andare al prossimo livello
                enemyNum = 0;  // imposta il numero di nemici sulla mappa a 0 cosi puo andare al prossimo livello
                nextLevel();  // va al prossimo livello
            }
            keyH.nextLevelPressed = false;
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
        if(!pauseGame){
            hud.drawTime(g2);  // disegna la barra bianca del timer
            g2.drawImage(hud.image, 0, 0, 256*scale, hudHeight, null);  // poi l'HUD
            hud.drawClock(g2);

            // Stampa il punteggio
            hud.drawScore(g2, player.score);
            hud.drawLife(g2, player.lifeNumber);
            if(hud.clockLeft == 1){  // se finisce il tempo
                System.out.println("---Finito il tempo---\n\n");
                // checkGameOn = false;  // ferma il gioco
                player.kill();  // uccide il player
                // resetLevel();  // resetta il gioco
                ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);  // crea una nuova pool di thread di una grandezza
                Runnable task = () -> resetLevel();  // crea una nuova funzione eseguibile che setta checkGameOn a true
            
                executor.schedule(task, 2, TimeUnit.SECONDS);  // esegue la task dopo 2 secondi in parallelo col programma
                executor.shutdown();  // chiude la pool di thread 
            }
        }else{
            g2.drawImage(hud.pauseImage, 0, 0, 256*scale, hudHeight, null);  // disegna l'immagine di pausa
        }
    }

    public void saveScore(){
        int level, world;
        
        if(levelType == "firstWorld" || levelType == "firstBoss"){
            world = 1;
            level = levelIndex+1;  // l'index del primo mondo va da 0 a 3 quindi aggiungiamo 1 per avere un index da 1 a 4
        }else{
            world = 2;
            level = levelIndex-3;  // l'index del secondo mondo va da 4 a 7 quindi rimuoviamo 3 per avere un index da 1 a 4
        }
        //legge i valori nel file txt per salvarli temporaneamente
        Map<String, String> scoreMap = readScore();

        // aggiorna i valori nel file in base a quelli di gioco attuali
        scoreMap = updateScore(scoreMap, world, level);

        // riscrive su file i dati aggiornati
        writeScore(scoreMap);
    }

    public Map<String, String> readScore(){
        Map<String, String> scoreMap = new HashMap<>();
        String line = "";
        try (Scanner scan = new Scanner(new File("./src/res/score.txt"))){
                line = scan.nextLine();
        } catch (IOException e){
            System.out.println("Errore nella lettura dello score");
            e.printStackTrace();
        }
        System.out.println(line);
        String[] score = line.split(" ");
        for(int i=0; i < score.length; i++){
            // System.out.println(score[i]);
            String key = score[i].substring(0, 2);
            String value = score[i].substring(2);
            scoreMap.put(key, value);
        }
        return scoreMap;
    }
    public void writeScore(Map<String, String> scoreMap){
        String finalScore = "";
        for(Map.Entry<String, String> entry: scoreMap.entrySet()){
            finalScore += entry.getKey() + entry.getValue() + " ";  // salva ogni valore come chiave (2char) + valore + spazio (es: "Bs120 ")
        }
        finalScore += "\n";  // ritorna a capo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("./src/res/score.txt"))){
            writer.write(finalScore);
        } catch (IOException e){
            System.out.println("Errore nella scrittura dello score");
            e.printStackTrace();
        }
    }
    public Map<String, String> updateScore(Map<String, String> scoreMap, int world, int level){
        scoreMap.put("Nk", player.nickname);
        scoreMap.put("Av", String.valueOf(player.avatarColor));
        if(player.score > Integer.parseInt(scoreMap.get("Bs"))){  // se lo score del player è migliore di quello salvato
            scoreMap.put("Bs", String.valueOf(player.score));  // cambia il Best Score
        }
        scoreMap.put("Gw", Integer.toString(Integer.parseInt(scoreMap.get("Gw")) + player.gamesWon));  // aumenta il numero di partite vinte
        scoreMap.put("Gl", Integer.toString(Integer.parseInt(scoreMap.get("Gl")) + player.gamesLost));  // aumenta il numero di partite perse
        scoreMap.put("Gp", Integer.toString(Integer.parseInt(scoreMap.get("Gp")) + player.gamesPlayed));  // aumenta il numero di partite giocate
        scoreMap.put("Ls", Integer.toString(player.score));  // salva l'ultimo score
        scoreMap.put("Lw", Integer.toString(world));  // salva l'ultimo mondo
        scoreMap.put("Ll", Integer.toString(level));  // salva l'ultimo livello
        return scoreMap;
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
            // finisce la musichetta e parte il livello dopo
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);  // crea una nuova pool di thread di una grandezza
            Runnable task = () -> {setupGame();};  // crea una nuova funzione eseguibile che setta checkGameOn a true e fa partire il suono dello stage intro
        
            executor.schedule(task, 2, TimeUnit.SECONDS);  // esegue la task dopo 2 secondi in parallelo col programma
            executor.shutdown();  // chiude la pool di thread 
            // setupGame();  // fa partire il setup del gioco
        }
        
        if(checkGameOn && !pauseGame){  // se il gioco puo partire e il player non ha fermato il gioco
            hud.updateTime();
            player.update();
            for(Enemy entity: enemy){
                if(!entity.extinguished)
                    entity.update();
            }
            cChecker.checkEntities();
            
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

            if(canDrawLevelNumber){  // se può disegnare il numero del livello
                g2.drawImage(levelNumberImages[levelIndex], levelNumberX, 0, screenWidth, screenHeight, null);  // disegna il numero del livello corrente
                if(levelNumberX != 0){  // finche non è precisamente al centro dello schermo
                    levelNumberX += 12;  // sposta a destra di 10 pixel il numero del livello per l'animazione
                }else{  // altrimenti ferma il disegno del numero del livello
                    // e lo fa ripartire dopo 1 secondi spostando nuovamente di 1 pixel lo sprite del livello sbloccando quindi il ciclo normale che sposta lo sprite
                    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);  // crea una nuova pool di thread di una grandezza
                    Runnable task = () -> levelNumberX += 12;  // crea una nuova funzione eseguibile che setta checkGameOn a true
                
                    executor.schedule(task, 1, TimeUnit.SECONDS);  // esegue la task dopo 2 secondi in parallelo col programma
                    executor.shutdown();  // chiude la pool di thread 
                }
                if(levelNumberX > screenWidth){  // se il numero del livello è fuori dallo schermo
                    levelNumberX = -screenWidth;  // resetta la posizione di partenza
                    canDrawLevelNumber = false;  // e non può piu disegnare il numero del livello
                }
            }
        }
        if (!checkGameOn && currentPanel != null){  // se il gioco non è ancora partito e c'è un pannello corrente
            currentPanel.drawPanel(g2, this);  // disegna il pannello corrente
        }
        g2.dispose();  // rimuove il contesto grafico e rilascia ogni risorsa di sistema che sta usando
    }

    public void paintGame(Graphics2D g2){
        if(g2 == null)
            System.out.println(g2);
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
                entity.drawBehaviour.draw(entity);  // disegna l'enemy
                // entity.drawBehaviour.draw(entity);
            }else{
                enemyDelete.add(entity);  // aggiunge il nemico morto alla lista temporanea dei nemici da eliminare
                if(entity.type == "knight" || entity.type == "clown"){  // se è uno dei boss allora vai direttamente al prossimo livello
                    nextLevel();
                }
            }
        }
        for(Enemy entity: enemyDelete){
            enemy.remove(entity);  // rimuove dalla lista originale l'enemy morto
        }
        drawHUD(g2);
    }

    public void playMusic(int index){  
        musicM.setAudio(index);  // carica la clip musicale selezionata in base all'index
        musicM.play();  // fa partire la clip musicale
        musicM.loop();  // la mette in loop
    }

    public void stopMusic(){
        musicM.stop();  // 
    }
    public void playSfx(int index){
        audioM.setAudio(index);
        audioM.play();
    }
}
