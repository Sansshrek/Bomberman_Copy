package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
public class ScoreMenu implements Panel{
    KeyHandler keyH = KeyHandler.getInstance(); 

    BufferedImage backgroundImage, ballonImage, cloudImage, optionStartImage, optionSettingsImage, optionScoreImage, pointerImage;
    int optionX, optionY, pointerIndex, optionWidth, optionHeight;
    public ScoreMenu(){
        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("../res/startMenu/startMenu.png"));
            ballonImage = ImageIO.read(getClass().getResourceAsStream("../res/startMenu/Ballon.png"));
            cloudImage = ImageIO.read(getClass().getResourceAsStream("../res/startMenu/Cloud.png"));
            optionStartImage = ImageIO.read(getClass().getResourceAsStream("../res/startMenu/Start.png"));
            optionSettingsImage = ImageIO.read(getClass().getResourceAsStream("../res/startMenu/Settings.png"));
            optionScoreImage = ImageIO.read(getClass().getResourceAsStream("../res/startMenu/Data.png"));
            pointerImage = ImageIO.read(getClass().getResourceAsStream("../res/startMenu/Pointer.png"));
            // transitionImage = ImageIO.read(getClass().getResourceAsStream
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void drawPanel(Graphics2D g2, GamePanel gp){
        g2.setColor(new Color(0, 0, 255));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);  // disegna il background
        g2.drawImage(optionStartImage, (gp.screenWidth - optionWidth)/2, (gp.screenHeight - optionHeight)/2+100, optionWidth, optionHeight, null);
        g2.drawImage(optionSettingsImage, (gp.screenWidth - optionWidth)/2, (gp.screenHeight - optionHeight)/2+160, optionWidth, optionHeight, null);
        g2.drawImage(optionScoreImage, (gp.screenWidth - optionWidth)/2, (gp.screenHeight - optionHeight)/2+220, optionWidth, optionHeight, null);
        g2.drawImage(pointerImage, pointerX, pointerY, 50, 50, null);
    }
    public void chooseOptions(GamePanel gp){
        pointerX = options[0][2]-60;
        pointerY = options[0][3];
        if(keyH.downPressed){
            pointerIndex+=1;
            if (pointerIndex>2){
                pointerIndex=0;
            }
        }
        else if(keyH.upPressed){
            pointerIndex-=1;
            if (pointerIndex<0){
                pointerIndex=2;
            }
        }
        //se preme invio e sono passati almeno 3 secondi dall'inizio dell'avvio del programma
        if(keyH.pausePressed && pointerIndex==0){
            if(!gp.checkGameOn){
                //disegna transizione
            }
            gp.setupGame();
        }
        if(keyH.pausePressed && pointerIndex==1){
            //disegna transizione
            gp.currentPanel = new OptionMenu();
        }
        if(keyH.pausePressed && pointerIndex==2){
            //disegna transizione
            gp.currentPanel = new ScoreMenu();
        }
        pointerX = options[poinerIndex][2];
        pointerY = options[poinerIndex][3];
    }
}
