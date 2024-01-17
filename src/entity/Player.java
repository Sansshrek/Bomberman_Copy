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

    public BufferedImage up1, up2, up3, down1, down2, down3, left1, left2, left3, right1, right2, right3;
    //largezza e altezza dell' immagine del player
    public final int width;
    public final int height; 
    
    //codinate top left dell' hitbox
    public Point hitboxP;
    
    //larghezza e altezza dell' hitbox
    public int firePower, bombNumber, lifeNumber, score, invulnerableTimer = 0;
    public boolean invulnerable, pauseGame;

    public Player(GamePanel gp, KeyHandler keyH){
        super(gp);
        this.keyH = keyH;
        int scale = gp.getScale();
        this.tileSize = gp.getTileSize();

        //largezza e altezza dell' immagine del player
        this.width = 16*scale; // larghezza del player
        this.height = 29*scale; // altezza del player 

        //codinate top left dell' hitbox
        this.hitboxX = 2*scale;// dove parte hitbox del player (2 pixel a destra rispetto a dove parte l'immagine)
        this.hitboxY = 12*scale; // dove parte hitbox del player (12 pixel sotto rispetto a dove parte l'immagine)
        
        //larghezza e altezza dell' hitbox
        this.hitboxWidth = 12*scale;// larghezza dell'hitbox del player
        this.hitboxHeight = 12*scale;// altezza dell'hitbox del player
        
        hitboxDefaultX = hitboxX;
        hitboxDefaultY = hitboxY;

        System.out.println("Caricando il player");  // da eliminare
        gp.bombH.addBombNumber();
        setPlayerDefaultValues();
        getPlayerImage();

    }

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
        this.hitbox = new Rectangle(hitboxX+imageP.x, hitboxY+imageP.y, hitboxWidth, hitboxHeight);
    }

    public void kill(){
        int x=0;
        int y=0;
        x = (0*tileSize) + (tileSize+tileSize/2);   // posizione x del player IN ALTO A SINISTRA
        y = (y*tileSize) + 2*tileSize;    // posizione y del player IN ALTO A SINISTRA
        this.imageP = new Point(x, y);
        direction = "down";
        invulnerable = true;
        died = false;
        this.hitbox = new Rectangle(hitboxX+imageP.x, hitboxY+imageP.y, hitboxWidth, hitboxHeight);
    }

    public void getPlayerImage(){
        try{  // prova a caricare le immagini nelle variabili
            
            up1 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Original/up01.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Original/up02.png"));
            up3 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Original/up03.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Original/down01.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Original/down02.png"));
            down3 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Original/down03.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Original/left01.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Original/left02.png"));
            left3 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Original/left03.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Original/right01.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Original/right02.png"));
            right3 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Original/right03.png"));
            
            //up1=up2=up3=down1=down2=down3=left1=left2=left3=right1=right2=right3 = ImageIO.read(getClass().getResourceAsStream("../res/player/walking Totti/up01.png")); 
            
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void update(){  // update viene chiamato 60 volte al secondo

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
        }
    }

    public void draw(){
        BufferedImage image = null;

        if(invulnerable){  // se il player è invulnerabile 
            invulnerableTimer++;  // aumenta il timer per l'invulnerabilità
            // System.out.println(invulnerableTimer);
            if(invulnerableTimer == 1200){  // quando finisce il timer
                System.out.println("Finita invulnerabilita");
                invulnerable = false;  // finisce l'invulnerabilità
                invulnerableTimer = 0;  // resetta il timer
            }
        }
        
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