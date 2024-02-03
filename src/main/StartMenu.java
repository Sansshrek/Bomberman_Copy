package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Color;

import javax.imageio.ImageIO;

public class StartMenu implements Panel{
    KeyHandler keyH = KeyHandler.getInstance(); 
    
    BufferedImage backgroundImage, ballonImage, cloudImage, optionStartImage, optionSettingsImage, optionScoreImage, pointerImage;
    //Salvo nella lista options a seconda dell'opzione optionX, optionY
    int[][] options = new int[3][4];
    int optionX, optionY, startPointerY, pointerX, pointerY, pointerIndex = 0, pointerWidth, pointerHeight, optionWidth, optionHeight, optionDistance, totOptionNumber=3;
    public StartMenu(GamePanel gp){
        optionWidth = 111*gp.scale;
        optionHeight = 10*gp.scale;
        optionX = (gp.screenWidth - optionWidth)/2;
        optionY = (gp.screenHeight - optionHeight)/2 + 100;
        pointerWidth = 8*gp.scale;
        pointerHeight = 12*gp.scale;
        pointerX = optionX - pointerWidth - 3*gp.scale;  // distanza di partenza del pointer
        startPointerY = optionY - 1*gp.scale;
        pointerY = startPointerY;
        optionDistance = 20*gp.scale;   //distanza tra le opzioni
        

        /* 
        options[0][0] = 400;//optionWidth
        options[0][1] = 55;//optionHeight
        options[0][2] = (gp.screenWidth - options[0][0])/2;//optionX
        options[0][3] = (gp.screenHeight - options[0][1])/2+100;//optionY
        options[1][0] = 400;//optionWidth
        options[1][1] = 55;//optionHeight
        options[1][2] = (gp.screenWidth - options[1][0])/2;//optionX
        options[1][3] = (gp.screenHeight - options[1][1])/2+160;//optionY
        options[2][0] = 400;//optionWidth
        options[2][1] = 55;//optionHeight
        options[2][2] = (gp.screenWidth - options[2][0])/2;//optionX
        options[2][3] = (gp.screenHeight - options[2][1])/2+220; //optionY0*/
        try {
            // ballonImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/Ballon.png"));
            // cloudImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/Cloud.png"));
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/Start.png"));
            optionStartImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/start sprite.png"));
            optionSettingsImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/Settings.png"));
            optionScoreImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/Score.png"));
            pointerImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/pointer.png"));
            // transitionImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/transiton.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void drawPanel(Graphics2D g2, GamePanel gp){
        g2.setColor(new Color(0, 0, 255));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);  // disegna il background
        g2.drawImage(backgroundImage, 0, 0, gp.screenWidth, gp.screenHeight, null);
        g2.drawImage(optionStartImage, optionX, optionY, optionWidth, optionHeight, null);
        g2.drawImage(optionSettingsImage, optionX, optionY+optionDistance, optionWidth, optionHeight, null);
        g2.drawImage(optionScoreImage, optionX, optionY+optionDistance*2, optionWidth, optionHeight, null);
        g2.drawImage(pointerImage, pointerX, pointerY, 50, 50, null);
    }

    public void chooseOptions(GamePanel gp){
        System.out.println("pointer index:" + pointerIndex);
        if(keyH.downPressed){
            pointerIndex+=1;
            if (pointerIndex>2){
                pointerIndex=0;
                pointerY = startPointerY;
            }
            else{
                pointerY = optionY+optionDistance;
            }
        }
        else if(keyH.upPressed){
            pointerIndex-=1;
            if (pointerIndex<0){
                pointerIndex=2;
                pointerY = optionY+optionDistance*pointerIndex;
            }
            else{
                pointerY = optionY-optionDistance;
            }
        }
        
        if(keyH.pausePressed && pointerIndex==0){
            System.out.println("INVIOOOOOO");

            if(!gp.checkGameOn){
                //disegna transizione
            }
            gp.setupGame();
            // gp.checkGameOn=true;
            //gp.currentPanel = null;
        }
        if(keyH.pausePressed && pointerIndex==1){
            //disegna transizione
            gp.currentPanel = new OptionMenu();
        }
        if(keyH.pausePressed && pointerIndex==2){
            //disegna transizione
            gp.currentPanel = new ScoreMenu();
        }
    }
}