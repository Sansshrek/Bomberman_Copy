package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;
import objects.BombHandler;

public class Player extends Entity{
    KeyHandler keyH;
    // public ArrayList<SuperObject> bombObj = new ArrayList<>();

    public BufferedImage up1, up2, up3, down1, down2, down3, left1, left2, left3, right1, right2, right3;
    //largezza e altezza dell' immagine del player
    public final int width;
    public final int height; 
    
    //codinate top left dell' hitbox
    public int hitboxX; 
    public int hitboxY;
    
    //larghezza e altezza dell' hitbox
    public int firePower, bombNumber, lifeNumber, score;

    public Player(GamePanel gp, KeyHandler keyH){
        super(gp);
        this.keyH = keyH;
        this.ogTileSize = gp.getOgTileSize();
        this.scale = gp.getScale();
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

        this.hitbox = new Rectangle(hitboxX+x, hitboxY+y, hitboxWidth, hitboxHeight);
    }

	public void setPlayerDefaultValues(){
        x=0;
        y=0;
        x = (x*tileSize) + (tileSize+tileSize/2);   // posizione x del player IN ALTO A SINISTRA
        y = (y*tileSize) + 2*tileSize;    // posizione y del player IN ALTO A SINISTRA
        speed = 2;
        direction = "down";
        firePower = 1;
        lifeNumber = 5;
        score = 0;
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

            // CONTROLLA COLLISIONE TILES
            collisionOn = false;
            // hitbox.x = x + hitboxX;
            // hitbox.y = y + hitboxY;
            gp.cChecker.checkTile(this);  // controlla se colpiamo qualche blocco
            // CONTROLLA COLLISIONE OBJECT
            int objIndex = gp.cChecker.checkObj(this, true, g2);
            powerUpHandler(objIndex); // controlliamo cosa fare con l'oggetto

            if(!collisionOn){ // si puo muovere
                switch(direction){
                    case "up": 
                        y -= speed; 
                        hitbox.y -= speed;
                        break;  // la posizione Y diminuisce della velocita del player
                    case "down": 
                        y += speed; 
                        hitbox.y += speed;
                        break;
                    case "left": 
                        x -= speed; 
                        hitbox.x -= speed;
                        break;
                    case "right": 
                        x += speed; 
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
            gp.bombH.createBomb(gp ,getPlayerTileX(), getPlayerTileY(), firePower);
            gp.cChecker.checkBomb(this);
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
    }

    public int getPlayerTileX(){
        int entityLeftWorldX = hitbox.x - (gp.tileSize+gp.tileSize/2);  // Coordinata x dove parte la hitbox
        int entityRightWorldX = hitbox.x + hitbox.width - (gp.tileSize+gp.tileSize/2);  // cordinata x dove arriva la hitbox
        int entityCenterX = ((entityLeftWorldX + entityRightWorldX) / 2 ) / gp.tileSize;
        return entityCenterX*gp.tileSize + (gp.tileSize + gp.tileSize/2);
    }
    public int getPlayerCenterX(){
        int entityLeftWorldX = hitbox.x - (gp.tileSize+gp.tileSize/2);  // Coordinata x dove parte la hitbox
        int entityRightWorldX = hitbox.x + hitbox.width - (gp.tileSize+gp.tileSize/2);  // cordinata x dove arriva la hitbox
        int entityCenterX = ((entityLeftWorldX + entityRightWorldX) / 2 );
        return entityCenterX + (gp.tileSize + gp.tileSize/2);
    }
    public int getPlayerTileY(){
        int entityTopWorldY = hitbox.y - (gp.tileSize*2+gp.tileSize/2);  // coordinata y dove parte la hitbox
        int entityBottomWorldY = hitbox.y + hitbox.height - (gp.tileSize*2+gp.tileSize/2);
        int entityCenterY = ((entityTopWorldY + entityBottomWorldY) / 2 ) / gp.tileSize;
        return entityCenterY*gp.tileSize + (gp.tileSize*2+gp.tileSize/2);
    }
    public int getPlayerCenterY(){
        int entityTopWorldY = hitbox.y - (gp.tileSize*2+gp.tileSize/2);  // coordinata y dove parte la hitbox
        int entityBottomWorldY = hitbox.y + hitbox.height - (gp.tileSize*2+gp.tileSize/2);
        int entityCenterY = ((entityTopWorldY + entityBottomWorldY) / 2 );
        return entityCenterY + (gp.tileSize*2+gp.tileSize/2);
    }

    public void powerUpHandler(int index){
        if(index != 999){  // se non è il valore default
            String objName = gp.obj.get(index).name;
            if(objName != "block") // da eliminare
                gp.obj.set(index, gp.obj.get(index).power);
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
                    speed += 0.25;
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
                case "nothing":
                break;
            }
        }
    }

    public void draw(){
        BufferedImage image = null;
        
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
        g2.drawImage(image, x, y, gp.player.width, gp.player.height, null);  // disegna lo sprite del personaggio (image) nella posizione x,y di grandezza tileSize
        //da eliminare
        g2.setColor(Color.BLUE);
        // g2.drawRect(this.hitboxX+x, this.hitboxY+y, this.hitboxWidth, this.hitboxHeight);  
        g2.drawRect(hitbox.x, hitbox.y, hitboxWidth, hitboxHeight);
        // System.out.println("x: "+hitbox.x+ " y: "+hitbox.y);

        g2.setColor(Color.GREEN);
        g2.drawRect(getPlayerTileX(), getPlayerTileY(), tileSize, tileSize);

         //da eliminare
        g2.setColor(Color.YELLOW);
        g2.drawRect(gp.gameBorderLeftX, gp.gameBorderUpY, 13*gp.tileSize, 11*gp.tileSize);

        g2.setColor(Color.RED);
        g2.drawRect(getPlayerCenterX(), getPlayerCenterY(), 6*scale, 6*scale);

        g2.setColor(Color.RED);
        // g2.drawRect(x, y, width, height);

    }
}