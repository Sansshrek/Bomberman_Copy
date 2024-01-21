package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;
import objects.Bomb;
import objects.BombHandler;

public class Player extends Entity{
    KeyHandler keyH;
    // public ArrayList<SuperObject> bombObj = new ArrayList<>();

    //largezza e altezza dell' immagine del player
    public final int width;
    public final int height; 
    
    //larghezza e altezza dell' hitbox
    public int firePower, bombNumber, lifeNumber, score, invulnerableTimer = 0, startDeathY;
    public boolean pauseGame;
    boolean checkDeathJump = false, checkDeathFall = false;
    int spriteDeathCounter = 0;

    public Player(GamePanel gp, KeyHandler keyH){
        super(gp);
        this.keyH = keyH;
        int scale = gp.getScale();
        this.tileSize = gp.getTileSize();

        this.image=null;

        //largezza e altezza dell' immagine del player
        this.width = 16*scale; // larghezza del player
        this.height = 29*scale; // altezza del player 

        //codinate top left dell' hitbox
        this.hitboxX = 2*scale;// dove parte hitbox del player (2 pixel a destra rispetto a dove parte l'immagine)
        this.hitboxY = 12*scale; // dove parte hitbox del player (12 pixel sotto rispetto a dove parte l'immagine)
        
        //larghezza e altezza dell' hitbox
        this.hitboxWidth = 12*scale;// larghezza dell'hitbox del player
        this.hitboxHeight = 12*scale;// altezza dell'hitbox del player
        

        System.out.println("Caricando il player");  // da eliminare
        gp.bombH.addBombNumber();  // aggiunge la prima bomba al player
        gp.bombH.addBombNumber();  // aggiunge la prima bomba al player
        setPlayerDefaultValues();
        getPlayerImage();
        notifyObservers();

    }
    
    @Override
    public void notifyObservers(){
        for (int i = 0; i < observers.size(); i++) {
            EntityObserver observer = (EntityObserver)observers.get(i);
            observer.updateEntity(this);
        }
    }
/* 
    public void setEntityDefaultValues(){
        setPlayerDefaultValues();
    }
    */
	public void setPlayerDefaultValues(){
        int x=0;
        int y=0;
        x = (x*tileSize) + (tileSize+tileSize/2);   // posizione x del player IN ALTO A SINISTRA
        y = (y*tileSize) + 2*tileSize;    // posizione y del player IN ALTO A SINISTRA
        this.imageP = new Point(x, y);
        speed = 2;
        direction = "down";
        firePower = 1;
        lifeNumber = 5;
        score = 0;
        invulnerable = true;
        died = false;
        extinguished = false;
        checkDeathJump = false;
        checkDeathFall = false;
        spriteDeathCounter = 0;
        spriteNum = 1;
        this.hitbox = new Rectangle(hitboxX+imageP.x, hitboxY+imageP.y, hitboxWidth, hitboxHeight);
        //setEntityVar(imageP, hitbox, invulnerable, died, extinguished, speed);
        notifyObservers();
    }

    public void kill(){
        if(!died){
            startDeathY = imageP.y;
            died = true;
        }
        // playerDeathJump();
        if(extinguished){  // quando è completamente morto resetta i valori
            int x=0;
            int y=0;
            x = (0*tileSize) + (tileSize+tileSize/2);   // posizione x del player IN ALTO A SINISTRA
            y = (y*tileSize) + 2*tileSize;    // posizione y del player IN ALTO A SINISTRA
            this.imageP = new Point(x, y);
            direction = "down";
            invulnerable = true;
            died = false;
            extinguished = false;
            checkDeathJump = false;
            checkDeathFall = false;
            spriteDeathCounter = 0;
            spriteNum = 1;
            lifeNumber -= 1;  // diminuisce di 1 la vita
            this.hitbox = new Rectangle(hitboxX+imageP.x, hitboxY+imageP.y, hitboxWidth, hitboxHeight);
            gp.hud.resetTimer();
            //setEntityVar(imageP, hitbox, invulnerable, died, extinguished, speed);
            notifyObservers();
        }
    }

    public void getPlayerImage(){
        try{  // prova a caricare le immagini nelle variabili
            
            ogUp1 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Original/up01.png"));
            ogUp2 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Original/up02.png"));
            ogUp3 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Original/up03.png"));
            ogDown1 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Original/down01.png"));
            ogDown2 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Original/down02.png"));
            ogDown3 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Original/down03.png"));
            ogLeft1 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Original/left01.png"));
            ogLeft2 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Original/left02.png"));
            ogLeft3 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Original/left03.png"));
            ogRight1 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Original/right01.png"));
            ogRight2 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Original/right02.png"));
            ogRight3 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Original/right03.png"));

            up1=ogUp1;
            up2=ogUp2;
            up3=ogUp3;
            down1=ogDown1;
            down2=ogDown2;
            down3=ogDown3;
            left1=ogLeft1;
            left2=ogLeft2;
            left3=ogLeft3;
            right1=ogRight1;
            right2=ogRight2;
            right3=ogRight3;


            whiteUp1 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Invincible/up01.png"));
            whiteUp2 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Invincible/up02.png"));
            whiteUp3 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Invincible/up03.png"));
            whiteDown1 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Invincible/down01.png"));
            whiteDown2 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Invincible/down02.png"));
            whiteDown3 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Invincible/down03.png"));
            whiteLeft1 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Invincible/left01.png"));
            whiteLeft2 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Invincible/left02.png"));
            whiteLeft3 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Invincible/left03.png"));
            whiteRight1 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Invincible/right01.png"));
            whiteRight2 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Invincible/right02.png"));
            whiteRight3 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Invincible/right03.png"));
            
            death1 = ImageIO.read(getClass().getResourceAsStream("../res/player/Player Death/death1.png"));
            death2 = ImageIO.read(getClass().getResourceAsStream("../res/player/Player Death/death2.png"));
            death3 = ImageIO.read(getClass().getResourceAsStream("../res/player/Player Death/death3.png"));
            death4 = ImageIO.read(getClass().getResourceAsStream("../res/player/Player Death/death4.png"));
            death5 = ImageIO.read(getClass().getResourceAsStream("../res/player/Player Death/death5.png"));
            death6 = ImageIO.read(getClass().getResourceAsStream("../res/player/Player Death/death6.png"));
            death7 = ImageIO.read(getClass().getResourceAsStream("../res/player/Player Death/death7.png"));
            
            //up1=up2=up3=down1=down2=down3=left1=left2=left3=right1=right2=right3 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Totti/up01.png")); 
            
        }catch(IOException e){
            e.printStackTrace();
        }
        notifyObservers();
    }

    public void update(){  // update viene chiamato 60 volte al secondo
        if(extinguished)
            kill();
        if(!died){ // se non è morto allora puo eseguire l'update
            if(lifeNumber == 0){
                gp.resetLevel();
            }

            if(keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed){

                if(keyH.upPressed){ // se clicchi sopra
                    direction = "up";
                }
                if(keyH.downPressed){
                    direction = "down";
                }
                if(keyH.leftPressed){
                    direction = "left";
                }
                if(keyH.rightPressed){
                    direction = "right";
                }

                // hitbox.x = x + hitboxX;
                // hitbox.y = y + hitboxY;
            // CONTROLLA COLLISIONE TILES
            collisionOn = false;
            // gp.cChecker.checkBomb(this);
            Point objPoint = gp.cChecker.checkObj(this, true, g2);
            powerUpHandler(objPoint);
            gp.cChecker.checkTile(this);  // controlla se colpiamo qualche blocco
                // CONTROLLA COLLISIONE OBJECT
                // controlliamo cosa fare con l'oggetto

                if(!collisionOn){ // si puo muovere
                    switch(direction){
                        case "up": 
                            imageP.y -= speed;
                            hitbox.y -= speed;
                            break;  // la posizione Y diminuisce della velocita del player
                        case "down": 
                            imageP.y += speed;
                            hitbox.y += speed;
                            break;
                        case "left": 
                            imageP.x -= speed; 
                            hitbox.x -= speed;
                            break;
                        case "right": 
                            imageP.x += speed; 
                            hitbox.x += speed;
                            break;
                    }
                    //setLocation(objPoint, hitbox);
                    
                }

                spriteCounter++;
                if(spriteCounter > 15){  // ogni 15/60 volte al secondo 
                    switch(spriteNum){
                        case 1:  // dalla 1 alla 2
                            spriteNum = 2; 
                            break;
                        case 2:  // dalla 2 alla 3(1)
                            spriteNum = 3;
                            break;
                        case 3:  // dalla 3(1) alla 4
                            spriteNum = 4;
                            break;
                        case 4: // dalla 4 alla 1
                            spriteNum = 1;
                            break;
                        }
                    spriteCounter = 0;  // e resetta il counter
                    // System.out.println(x+" "+y);  // da eliminare
                    //System.out.println(getPlayerTileX()+ " "+getPlayerTileY());
                }
            }
            if(keyH.bombPressed){ // se preme il tasto P (bomba)
                // BombHandler bomb = new BombHandler(getPlayerCenterCol()*gp.tileSize+(gp.tileSize/2), getPlayerCenterRow()*gp.tileSize+(24 * gp.scale), firePower, g2, gp.tileSize);
                gp.bombH.createBomb(getTileX(), getTileY(), getTileNumRow(), getTileNumCol(), firePower);
                // gp.cChecker.checkBomb(this);
                // BombHandler bomb = new BombHandler(x, y, firePower, g2, gp.tileSize);
                // gp.bombs.add(bomb);
                // gp.obj.add(bomb);
            }
            if(keyH.statsPressed){ // da eliminare
                System.out.println("\nFire "+firePower);
                System.out.println("Speed "+speed);
                System.out.println("BombNumber "+bombNumber);
                System.out.println("Life "+lifeNumber);
                keyH.statsPressed = false;
            }
            if(keyH.firePressed){
                firePower++;
                System.out.println("Range fuoco aumentato");
                keyH.firePressed = false;
            }
            if(keyH.resetPressed){
                gp.resetLevel();
                keyH.resetPressed = false;
            }
        }
        notifyObservers();
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
/* 
    public void playerDeathJump(){
        if(died){
            for(int i=0; i<gp.tileSize*2; i++){
                imageP.y -= 1;
                hitbox.y -= 1;
                
            }
            for(int i=0; i<gp.tileSize*2; i++){
                imageP.y += 1;
                hitbox.y += 1;
                g2.drawImage(death1, imageP.x, imageP.y, gp.player.width, gp.player.height, null);
            }
            extinguished = true;
        }
    }
*/
    public void powerUpHandler(Point index){
        if(index.x != 999 && gp.obj[index.y][index.x].name != "block" && gp.obj[index.y][index.x] != null && !(gp.obj[index.y][index.x] instanceof Bomb)){  // se non è il valore default o un blocco
            String objName = gp.obj[index.y][index.x].name;
            gp.obj[index.y][index.x] = null;
            switch(objName){
                case "fire":
                    score += 10;
                    firePower += 1;
                break;
                case "bomb":
                    score += 10;
                    gp.bombH.addBombNumber();
                break;
                case "skate":
                    score += 10;
                    speed += 1;
                    //setStatus(invulnerable, died, extinguished, speed);
                break;
                case "life":
                    score += 50;
                    lifeNumber += 1;
                break;
                case "death":
                    lifeNumber -= 1;
                break;
                case "onigiri":
                    score += 150;
                break;
                case "apple":
                    score += 300;
                break;
                case "ice_cream":
                    score += 500;
                break;
                case "cake":
                    score += 1000;
                break;
                case "exit":
                    gp.nextLevel();
                break;
                case "nothing":
                break;
            }
            notifyObservers();
        }
    }
    public void invincibleCheck(){
        int d, r;
        if(invulnerable){  // se il player è invulnerabile 
            invulnerableTimer++;  // aumenta il timer per l'invulnerabilità
            if(invulnerableTimer>=600){
                System.out.println("Mancano 2 sec");
                d=6;
                r=3;
            }else if (invulnerableTimer>=480){
                System.out.println("Mancano 4 sec");
                d=15;
                r=9;
            }else{  
                d=30;
                r=15;
            }
            if (invulnerableTimer%d==r){  // divide per 30 (quindi la metà di 60 FPS del gioco, cioe ogni mezzo secondo) e per ogni quarto di secondo cambia lo sprite 
                up1 = whiteUp1;
                up2 = whiteUp2;
                up3 = whiteUp3;
                down1 = whiteDown1;
                down2 = whiteDown2;
                down3 = whiteDown3;
                left1 = whiteLeft1;
                left2 = whiteLeft2;
                left3 = whiteLeft3;
                right1 = whiteRight1;
                right2 = whiteRight2;
                right3 = whiteRight3;
            }
            if (invulnerableTimer%d==0){
                up1 = ogUp1;
                up2 = ogUp2;
                up3 = ogUp3;
                down1 = ogDown1;
                down2 = ogDown2;
                down3 = ogDown3;
                left1 = ogLeft1;
                left2 = ogLeft2;
                left3 = ogLeft3;
                right1 = ogRight1;
                right2 = ogRight2;
                right3 = ogRight3;
            }
            // System.out.println(invulnerableTimer);
            if(invulnerableTimer == 720){  // quando finisce il timer
                System.out.println("Finita invulnerabilita");
                invulnerable = false;  // finisce l'invulnerabilità
                // resetta le immagini originali per sicurezza
                up1 = ogUp1;
                up2 = ogUp2;
                up3 = ogUp3;
                down1 = ogDown1;
                down2 = ogDown2;
                down3 = ogDown3;
                left1 = ogLeft1;
                left2 = ogLeft2;
                left3 = ogLeft3;
                right1 = ogRight1;
                right2 = ogRight2;
                right3 = ogRight3;
                invulnerableTimer = 0;  // resetta il timer
                //setStatus(invulnerable, died, extinguished, speed);
            }
            notifyObservers();
        }
        
    }

    public void draw(){
        image = null;
        if(!died){
            invincibleCheck();
            switch(direction){  // in base alla direzione, la variabile image prende il valore dell'immagine inserita
                case "up":
                    if(spriteNum == 1 || spriteNum == 3){
                        image = up1;
                    }
                    if(spriteNum == 2){
                        image = up2;
                    }
                    if(spriteNum == 4){
                        image = up3;
                    }
                    break;
                case "down":
                    if(spriteNum == 1 || spriteNum == 3){
                        image = down1;
                    }
                    if(spriteNum == 2){
                        image = down2;
                    }
                    if(spriteNum == 4){
                        image = down3;
                    }
                    break;
                case "left":
                    if(spriteNum == 1 || spriteNum == 3){
                        image = left1;
                    }
                    if(spriteNum == 2){
                        image = left2;
                    }
                    if(spriteNum == 4){
                        image = left3;
                    }
                    break;
                case "right":
                    if(spriteNum == 1 || spriteNum == 3){
                        image = right1;
                    }
                    if(spriteNum == 2){
                        image = right2;
                    }
                    if(spriteNum == 4){
                        image = right3;
                    }
                    break;
            }   
        }else{  // se il player è morto
            if(!checkDeathFall){  // se ancora non ha toccato terra allora salta
                image = death1;
                if(checkDeathJump){  // dopo aver toccato 2 blocchi di altezza
                    imageP.y += 8;  // scende
                }
                else{
                    imageP.y -= 8;  // altrimenti sale
                }
                if(imageP.y == startDeathY-gp.tileSize-gp.tileSize/2){  // se supera i 2 blocchi
                    checkDeathJump = true;
                }
                if(imageP.y == startDeathY){
                    checkDeathFall = true;
                    spriteNum = 1;
                }
            }else{  // altrimenti se tocca terra er poro chicco sta a stira
                if(spriteDeathCounter == 10){
                    extinguished = true;
                }
        
    
                spriteCounter++;
                if(spriteCounter > 10){  // ogni 15/60 volte al secondo 
                    switch(spriteNum){
                        case 1:
                            spriteNum++; 
                            break;
                        case 2: 
                            spriteNum++;
                            break;
                        case 3: 
                            spriteNum++;
                            break;
                        case 4:
                            spriteNum++;
                            break;
                        case 5:
                            spriteNum = 6;
                            spriteDeathCounter++;
                            break;
                        case 6:
                            spriteNum = 5;  // fa un loop nelle ultime due animazioni
                            spriteDeathCounter++;
                            break;
                        }
                    spriteCounter = 0;  // e resetta il counter
                }
                if(spriteNum == 1)
                    image = death2;
                else if(spriteNum == 2)
                    image = death3;
                else if(spriteNum == 3)
                    image = death4;
                else if(spriteNum == 4)
                    image = death5;
                else if(spriteNum == 5)
                    image = death6;
                else if(spriteNum == 6)
                    image = death7;
            }
        }
        notifyObservers();
        g2.drawImage(image, imageP.x, imageP.y, gp.player.width, gp.player.height, null);  // disegna lo sprite del personaggio (image) nella posizione x,y di grandezza tileSize
        //da eliminare
        g2.setColor(Color.BLUE);
        // g2.drawRect(this.hitboxX+x, this.hitboxY+y, this.hitboxWidth, this.hitboxHeight);  
        g2.drawRect(hitbox.x, hitbox.y, hitboxWidth, hitboxHeight);
        // System.out.println("x: "+hitbox.x+ " y: "+hitbox.y);

        g2.setColor(Color.GREEN);
        // System.out.println("Tilex: "+getPlayerTileX()+ " TileY: "+getPlayerTileY());
        // System.out.println("NTilex: "+getTileNumCol()+ " NTileY: "+getTileNumRow());
        // System.out.println("x: "+hitbox.x+ " y: "+hitbox.y);
        // System.out.println("CentX: "+(getCenterX()/gp.tileSize)+" CentY: "+(getCenterY()/gp.tileSize)+" x: "+hitbox.x+" y: "+hitbox.y);
        g2.drawRect(getTileX(), getTileY(), tileSize, tileSize);


        //da eliminare
        g2.setColor(Color.YELLOW);
        g2.drawRect(gp.gameBorderLeftX, gp.gameBorderUpY, 13*gp.tileSize, 11*gp.tileSize);
        // g2.drawRect(gp.gameBorderLeftX, gp.gameBorderUpY, Math.abs(gp.gameBorderRightX - gp.gameBorderLeftX), Math.abs(gp.gameBorderDownY - gp.gameBorderUpY));

        g2.setColor(Color.RED);
        g2.drawRect(getCenterX(), getCenterY(), hitboxWidth/2, hitboxHeight/2);

        g2.setColor(Color.RED);
        // g2.drawRect(x, y, width, height);

    }
}