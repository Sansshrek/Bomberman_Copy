package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.GamePanel;

public class Bomb extends SuperObject{
    int bombTimer = 0;
    int fireTimer = 0;
    int spriteCounter = 0;
    int spriteNum = 1;
    int firePower;
    public boolean exploded = false, extinguished = false, newBombAv = false;
    public boolean endedAnimation = false;
    int stopIndexSx = 10, stopIndexDx = 10, stopIndexUp = 10, stopIndexDw = 10;
    BufferedImage bombImage, largeBomb, mediumBomb, smallBomb;
    int bombWidth, bombHeight;
    BufferedImage[] expCenter = new BufferedImage[5];
    BufferedImage[] expEndUp = new BufferedImage[5];
    BufferedImage[] expEndBtm = new BufferedImage[5];
    BufferedImage[] expEndSx = new BufferedImage[5];
    BufferedImage[] expEndDx = new BufferedImage[5];
    BufferedImage[] expMidUp = new BufferedImage[5];
    BufferedImage[] expMidBtm = new BufferedImage[5];
    BufferedImage[] expMidSx = new BufferedImage[5];
    BufferedImage[] expMidDx = new BufferedImage[5];
    public Graphics2D g2;
    

    public Bomb(GamePanel gp, int x, int y, int firePower, int tileSize, Graphics2D g2){
        super(gp);
        this.x = x;
        this.y = y;
        this.firePower = firePower;
        this.bombWidth = tileSize;
        this.bombHeight = tileSize;
        // this.g2 = g2;
        try {
            //sprite delle bombe
            largeBomb = bombImage = ImageIO.read(getClass().getResourceAsStream("../res/bomb/largeBomb.png"));
            mediumBomb = ImageIO.read(getClass().getResourceAsStream("../res/bomb/mediumBomb.png"));
            smallBomb = ImageIO.read(getClass().getResourceAsStream("../res/bomb/smallBomb.png"));
            // sprite delle esplosioni
            for(int i=1; i<=5; i++){
                expCenter[i-1] = ImageIO.read(getClass().getResourceAsStream("../res/bomb/exp"+i+"/expCenter.png"));
                expEndUp[i-1] = ImageIO.read(getClass().getResourceAsStream("../res/bomb/exp"+i+"/expEndUp.png"));
                expEndBtm[i-1] = ImageIO.read(getClass().getResourceAsStream("../res/bomb/exp"+i+"/expEndBtm.png"));
                expEndSx[i-1] = ImageIO.read(getClass().getResourceAsStream("../res/bomb/exp"+i+"/expEndSx.png"));
                expEndDx[i-1] = ImageIO.read(getClass().getResourceAsStream("../res/bomb/exp"+i+"/expEndDx.png"));
                expMidUp[i-1] = ImageIO.read(getClass().getResourceAsStream("../res/bomb/exp"+i+"/expMidUp.png"));
                expMidBtm[i-1] = ImageIO.read(getClass().getResourceAsStream("../res/bomb/exp"+i+"/expMidBtm.png"));
                expMidSx[i-1] = ImageIO.read(getClass().getResourceAsStream("../res/bomb/exp"+i+"/expMidSx.png"));
                expMidDx[i-1] = ImageIO.read(getClass().getResourceAsStream("../res/bomb/exp"+i+"/expMidDx.png"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("bomb "+x+" "+y);
    }

    public void draw(Graphics2D g2){
        spriteCounter++;
        
        if(bombTimer == 9){  // dopo 9 animazioni / 1.5 secondi
            exploded = true;
            spriteNum = 1;  // resetta lo spriteNum prima che esplode la bomba
        }else if(spriteCounter > 10){  // ogni 10/60 volte al secondo 
            System.out.println(bombTimer);
            bombTimer++;  // aumenta il contatore della bomba
            switch(spriteNum){
                case 1:  // dalla 1(small) alla 2(medium)
                    spriteNum = 2;
                    bombImage = smallBomb;
                    break;
                case 2:  // dalla 2(medium) alla 3(big)
                    spriteNum = 3;
                    bombImage = mediumBomb;
                    break;
                case 3:  // dalla 3(big) alla 4(medium)
                    spriteNum = 4;
                    bombImage = largeBomb;
                    break;
                case 4: // dalla 4(medium) alla 1(small)
                    spriteNum = 1;
                    bombImage = mediumBomb;
                    break;
            }
            spriteCounter = 0;  // e resetta il counter
        }
        g2.drawImage(bombImage, x, y, bombWidth, bombHeight, null); 
    }
    public void generateFires(Graphics2D g2){
        if(!newBombAv){
            newBombAv = true;
            gp.bombH.addBombNumber();
        }
        if(fireTimer == 4){  // dopo 9 animazioni / 1.5 secondi
            extinguished = true;
        }else if(spriteCounter > 10){  // ogni 10/60 volte al secondo 
            fireTimer++;  // aumenta il contatore della bomba
            spriteCounter = 0;
        }
        spriteCounter++;
    
        // If the explosion has extinguished, do not draw the fire
        if (extinguished) {
            return;
        }
    
        // Draw the center of the explosion
        g2.drawImage(expCenter[fireTimer], x, y, bombWidth, bombHeight, null); 
    
        g2.setColor(Color.RED);  // da eliminare
        // Draw the horizontal line of the explosion
 
        for (int i = 1; i <= firePower; i++) {
            int positionSx = (((x - i*bombWidth - (gp.tileSize+gp.tileSize/2)))/gp.tileSize)  * 10 + ((y - (2*gp.tileSize + (gp.tileSize/2)))/gp.tileSize );
            
            // System.out.println("PosSX "+positionSx+" PosDX "+positionDx+" PosUP "+positionUp+" PosDW "+positionDw);

            // g2.drawRect(x - i*bombWidth, y, gp.tileSize, gp.tileSize);
            g2.drawRect(x - i*bombWidth, y, gp.tileSize, gp.tileSize);

            if(i >= stopIndexSx){  // se il fuoco supera l'index del blocco distrutto allora ferma il fuoco
                break;
            }

            if(positionSx >= 0 && positionSx < 140) {  // check se la posizione è nell'array

                // System.out.print("SX ");  // da eliminare
                if(gp.tileM.isHouse(x-i*bombWidth, y)){  // se in quella pos. del fuoco c'è una casa 
                    // System.out.println("X "+(x-i*bombWidth)+" Y "+y+" Collision SX true");
                    break;  // ferma il loop
                }
                if(gp.obj.get(positionSx) != null && gp.obj.get(positionSx).name != "exit"){   // check se quella posizione è un blocco esistente
                    // System.out.println(gp.obj.get(positionSx).name);
                    gp.obj.get(positionSx).destroy();  // inizia l'animazione dell'esplosione del blocco
                    // gp.obj.set(positionSx, gp.obj.get(positionSx).power);
                    stopIndexSx = i;  // salva la posizione del fuoco cosi che non vada oltre al blocco distrutto
                }
                
                // Draw the middle part of the explosion
                if (i < firePower) { 
                    if((x-i*bombWidth) > (gp.gameBorderLeftX-1))
                        g2.drawImage(expMidSx[fireTimer], x - i * bombWidth, y, bombWidth, bombHeight, null);
                }

                // Draw the end part of the explosion
                if (i == firePower) {
                    if((x-i*bombWidth) > (gp.gameBorderLeftX-1))
                        g2.drawImage(expEndSx[fireTimer], x - i * bombWidth, y, bombWidth, bombHeight, null);
                }
            }
            
        }
        
        for (int i = 1; i <= firePower; i++) {
            g2.drawRect(x + i*bombWidth, y, gp.tileSize, gp.tileSize);

            if(i >= stopIndexDx){
                break;
            }

            int positionDx = (((x + i*bombWidth- (gp.tileSize+gp.tileSize/2)))/gp.tileSize)  * 10 + ((y - (2*gp.tileSize + (gp.tileSize/2)))/gp.tileSize);
            if(positionDx >= 0 && positionDx <140){

                
                // System.out.print("DX ");  // da eliminare
                if(gp.tileM.isHouse(x+i*bombWidth, y)){
                    // System.out.println("X "+(x+i*bombWidth)+" Y "+y+" Collision DX true");
                    break;
                    // System.out.println("collision");
                }

                if(gp.obj.get(positionDx) != null && gp.obj.get(positionDx).name != "exit"){
                    // System.out.println(gp.obj.get(positionDx).name);
                    gp.obj.get(positionDx).destroy();
                    stopIndexDx = i;
                }

                if (i < firePower){
                    if((x+i*bombWidth) < gp.gameBorderRightX)
                        g2.drawImage(expMidDx[fireTimer], x + i * bombWidth, y, bombWidth, bombHeight, null);
                }

                if (i == firePower){
                    if((x+i*bombWidth) < gp.gameBorderRightX)
                        g2.drawImage(expEndDx[fireTimer], x + i * bombWidth, y, bombWidth, bombHeight, null);
                }
            }
        }

        for (int i = 1; i <= firePower; i++) {
            g2.drawRect(x, y - i*bombWidth, gp.tileSize, gp.tileSize);

            if(i >= stopIndexUp){
                break;
            }

            int positionUp = ((x-(gp.tileSize+gp.tileSize/2))/gp.tileSize ) * 10 + ((y- i*bombWidth- (2*gp.tileSize + (gp.tileSize/2)))/gp.tileSize );
            if(positionUp >= 0 && positionUp <140){
                
                // System.out.print("UP ");  // da eliminare
                if(gp.tileM.isHouse(x, y-i*bombWidth)){
                    // System.out.println("X "+x+" Y "+(y-i*bombWidth)+" Collision UP true");
                    break;
                    // System.out.println("collision");
                }

                if(gp.obj.get(positionUp) != null && gp.obj.get(positionUp).name != "exit"){
                    // System.out.println(gp.obj.get(positionUp).name);
                    gp.obj.get(positionUp).destroy();
                    stopIndexUp = i;
                }

                if (i < firePower){
                    System.out.println("y "+(y-i*bombWidth)+ " game "+gp.gameBorderUpY);
                    if((y-i*bombWidth) > gp.gameBorderUpY-1)
                        g2.drawImage(expMidUp[fireTimer], x, y - i * bombHeight, bombWidth, bombHeight, null);
                }
                
                if (i == firePower){
                    if((y-i*bombWidth) > gp.gameBorderUpY-1)
                        g2.drawImage(expEndUp[fireTimer], x, y - i * bombHeight, bombWidth, bombHeight, null);
                }
            }
        }
        
        for (int i = 1; i <= firePower; i++) {

            // g2.drawRect(x, y + i*bombWidth, gp.tileSize, gp.tileSize);
            g2.setColor(Color.BLUE);

            if(i >= stopIndexDw){
                break;
            }

            int positionDw = ((x-(gp.tileSize+gp.tileSize/2))/gp.tileSize ) * 10 + ((y+ i*bombWidth- (2*gp.tileSize + (gp.tileSize/2)))/gp.tileSize );
            if(positionDw >= 0 && positionDw <140){
                
                // System.out.println("X "+x+" Y "+(y+i*bombWidth)+ " calc x:"+ (((x - (gp.tileSize+gp.tileSize/2)))/gp.tileSize)+ " y:"+(((y+i*bombWidth) - (2*gp.tileSize + (gp.tileSize/2)))/gp.tileSize ));
                
                // System.out.print("DW ");  // da eliminare
                if(gp.tileM.isHouse(x, y+i*bombWidth)){
                    // System.out.println("X "+x+" Y "+(y+i*bombWidth)+" Collision DW true");
                    g2.drawRect(x, y+i*bombWidth, bombWidth, bombWidth);
                    break;
                    // System.out.println("collision");
                }
                    if(gp.obj.get(positionDw)!= null && gp.obj.get(positionDw).name != "exit"){  // è un blocco o powerUp che non sia l'uscita
                        // System.out.println(gp.obj.get(positionDw).name);
                        gp.obj.get(positionDw).destroy();
                        stopIndexDw = i;
                    }
                // else if(gp.obj.get(positionDw) != null && gp.obj.ge){  // è un powerUp
                //     gp.obj.set(positionDw, null);
                // }

                if (i < firePower){
                    if((y+i*bombWidth) < gp.gameBorderDownY)
                        g2.drawImage(expMidBtm[fireTimer], x, y + i * bombHeight, bombWidth, bombHeight, null);
                }
                
                if (i == firePower){
                    if((y+i*bombWidth) < gp.gameBorderDownY)
                        g2.drawImage(expEndBtm[fireTimer], x, y + i * bombHeight, bombWidth, bombHeight, null);
                }
            }
        }
    }  
}  