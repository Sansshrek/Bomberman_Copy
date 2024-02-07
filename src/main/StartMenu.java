package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Color;

import javax.imageio.ImageIO;

public class StartMenu implements Panel{
    
    KeyHandler keyH = KeyHandler.getInstance();
    BufferedImage backgroundImage, ballonImage, cloudImage, optionStartImage, optionSettingsImage, optionScoreImage, pointerImage, title;
    int alphaVal, titleX, titleY, titleWidth, titleHeight, optionX, optionY, startPointerY, pointerX, pointerY, pointerIndex = 0, pointerWidth, pointerHeight, optionWidth, optionHeight, optionDistance, totOptionNumber;
    boolean optionMenu = false, scoreMenu = false, startGame = false;
    public StartMenu(GamePanel gp){
        // this.keyH = gp.keyH;
        gp.playMusic(13);  // Theme menu
        alphaVal = 0;
        totOptionNumber = 3;
        titleX = 17*gp.scale;
        titleY = 11*gp.scale;
        titleWidth = 205*gp.scale;
        titleHeight = 136*gp.scale;
        optionWidth = 111*gp.scale;
        optionHeight = 10*gp.scale;
        optionX = (gp.screenWidth - optionWidth)/2;
        optionY = (gp.screenHeight - optionHeight)/2 + 140;
        pointerWidth = 8*gp.scale;
        pointerHeight = 12*gp.scale;
        pointerX = optionX - pointerWidth - 3*gp.scale;  // distanza di partenza del pointer
        startPointerY = optionY - 1*gp.scale;
        pointerY = startPointerY;
        optionDistance = 20*gp.scale;   //distanza tra le opzioni
    
        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/Background.png"));
            optionStartImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/Start.png"));
            optionSettingsImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/Settings.png"));
            optionScoreImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/Score.png"));
            pointerImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/pointer.png"));
            title = ImageIO.read(getClass().getResourceAsStream("../res/menu/Title.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void drawPanel(Graphics2D g2, GamePanel gp){
        g2.setColor(new Color(0, 0, 255));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);  // disegna il background
        g2.drawImage(backgroundImage, 0, 0, gp.screenWidth, gp.screenHeight, null);
        g2.drawImage(title, titleX, titleY, titleWidth, titleHeight, null);
        if(!optionMenu && !scoreMenu && !scoreMenu){
            g2.drawImage(optionStartImage, optionX, optionY, optionWidth, optionHeight, null);
            g2.drawImage(optionSettingsImage, optionX, optionY+optionDistance, optionWidth, optionHeight, null);
            g2.drawImage(optionScoreImage, optionX, optionY+optionDistance*2, optionWidth, optionHeight, null);
            g2.drawImage(pointerImage, pointerX, pointerY, pointerWidth, pointerHeight, null);
        }
        if((startGame || scoreMenu)&& alphaVal<255){  // se il gioco è partito
            alphaVal += 5;  // aumenta il valore alpha per la transizione
        }
        closingTransition(g2, gp);  // e disegna la transizione
    }

    public void closingTransition(Graphics2D g2, GamePanel gp){
        g2.setColor(new Color(0, 0, 0, alphaVal));  // setta il colore con alpha
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);  // disegna il background
    }

    public void chooseOptions(GamePanel gp){
        if(startGame && alphaVal==255){  // quando è partito il gioco e finisce la transizione
            gp.resetGamePanel();
        }
        if(!optionMenu && !scoreMenu){
            if(keyH.downPressed){
                gp.playSfx(12);  // sound cursore
                pointerIndex+=1;
                if (pointerIndex == totOptionNumber){
                    pointerIndex=0;
                    pointerY = startPointerY;
                }else{
                    pointerY = pointerY+optionDistance;
                }
                keyH.downPressed = false;
            }
            else if(keyH.upPressed){
                gp.playSfx(12);  // sound cursore
                pointerIndex-=1;
                if (pointerIndex<0){
                    pointerIndex=totOptionNumber-1;
                    pointerY = startPointerY+optionDistance*(totOptionNumber-1);  // vai alla pos dell'ultima opzione
                }else{
                    pointerY = pointerY-optionDistance;
                }
                keyH.upPressed = false;
            }
            
            if(keyH.pausePressed && pointerIndex==0){
                gp.stopMusic();
                gp.playSfx(11);  // sound invio selezione
                startGame = true;
                keyH.pausePressed = false;
            }
            if(keyH.pausePressed && pointerIndex==1){
                gp.stopMusic();
                gp.playSfx(11);  // sound invio selezione
                //disegna transizione
                optionMenu = true;
                keyH.pausePressed = false;
            }
            if(keyH.pausePressed && pointerIndex==2){
                gp.stopMusic();
                gp.playSfx(11);  // sound invio selezione
                //disegna transizione
                scoreMenu = true;
                keyH.pausePressed = false;
            }
        }else if(!startGame && (optionMenu || scoreMenu)){
            titleY -= 11;
            if(titleHeight+titleY<0){
                if(optionMenu)
                    gp.currentPanel = new OptionMenu(gp);
                else if(scoreMenu)
                    gp.currentPanel = new ScoreMenu(gp);
            }
        }
    }
}