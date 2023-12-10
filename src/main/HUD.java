package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class HUD {
    public GamePanel gp;
    public int tileSize;
    public int scale;
    public BufferedImage image;
    public int hudHeight;
    private BufferedImage[] numberImages;
    int numberWidth, numberHeight;
    int scoreX;
    int scoreY;
    int scoreWidth ;
    int scoreHeight;
    int lifeX;
    int lifeY;
    
    public HUD(GamePanel gp){
        this.gp = gp;
        this.tileSize = gp.getTileSize();
        this.scale = gp.getScale();
        this.scoreX = 3*tileSize;
        this.scoreY = 9*scale;
        this.scoreWidth = 4*tileSize;
        this.scoreHeight = 14*scale;
        this.lifeX = tileSize+tileSize/2;
        this.lifeY = 9*scale;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("../res/HUD.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadNumberImages();
    }

    public void loadNumberImages(){
        numberImages = new BufferedImage[10];
        try {
            for(int i=0; i<10; i++){
                numberImages[i] = ImageIO.read(getClass().getResourceAsStream("../res/numbers/"+i+".png"));
            }
            numberWidth = numberImages[0].getWidth()*gp.scale;
            numberHeight = numberImages[0].getHeight()*gp.scale;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawScore(Graphics2D g2, int score){
        String scoreString = Integer.toString(score);  // transformiamo lo score del player in una stringa
        int totalDigits = scoreString.length();
        int startX = scoreX + (scoreWidth-numberWidth) + numberWidth - (totalDigits * numberWidth);  // calcoliamo da dove parte ogni volta lo score
        int startY = scoreY + (scoreHeight - numberHeight) / 2;  // e l'altezza

        for(int i=0; i<totalDigits; i++){
            char digitChar = scoreString.charAt(i);  // per ogni cifra dello score
            int digit = Character.getNumericValue(digitChar);  // la ritransformiamo in numero singolo
            BufferedImage numImage = numberImages[digit];  // prendiamo l'immagine per quel numero
            int digitX = startX + (i * numberWidth);  // calcoliamo da dove parte la nuove immagine dopo quella precedente

            g2.drawImage(numImage, digitX, startY, numberWidth, numberHeight, null);
        }
    }

    public void drawLife(Graphics2D g2, int life){
        BufferedImage lifeImage = numberImages[life];
        g2.drawImage(lifeImage, lifeX, lifeY, numberWidth, numberHeight, null);
    }
}
