package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Color;

import java.util.concurrent.*;
import javax.imageio.ImageIO;

public class DeathPanel implements Panel{
    
    KeyHandler keyH = KeyHandler.getInstance();
    BufferedImage panelImage, pointerImage;
    int alphaVal, startPointerX, pointerX, pointerY, pointerIndex = 0, optionDistance, pointerWidth, pointerHeight;
    boolean startMenu = false, startGame = false;
    public DeathPanel(GamePanel gp){
        alphaVal = 0;
        pointerWidth = 8*gp.scale;
        pointerHeight = 12*gp.scale;
        pointerX = 57*gp.scale;  // distanza di partenza del pointer
        startPointerX = pointerX;
        pointerY = 111*gp.scale;
        optionDistance = 70*gp.scale;   //distanza tra le opzioni
        
        try {
            panelImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/death menu.png"));
            pointerImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/pointer.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void drawPanel(Graphics2D g2, GamePanel gp){
        g2.drawImage(panelImage, 0, 0, gp.screenWidth, gp.screenHeight, null);
        if(!startGame && !startMenu){ 
            g2.drawImage(pointerImage, pointerX, pointerY, pointerWidth, pointerHeight, null);
        }
        if((startGame || startMenu)&& alphaVal<255){  // se il gioco è partito
            alphaVal += 5;  // aumenta il valore alpha per la transizione
        }
        closingTransition(g2, gp);  // e disegna la transizione
    }

    public void closingTransition(Graphics2D g2, GamePanel gp){
        g2.setColor(new Color(0, 0, 0, alphaVal));  // setta il colore con alpha
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);  // disegna il background
    }

    public void chooseOptions(GamePanel gp){
        // System.out.println("pointer index:" + pointerIndex);
        // System.out.println(pointerY);
        if(startGame && alphaVal==255){  // quando è partito il gioco e finisce la transizione
            gp.alphaVal = 255;  // reimposto le variabili necessarie nel gamePanel per permettere la transizione di entrata nel gamepanel
            gp.checkSetup = false;
            gp.setupGame();
            gp.currentPanel = null;
        }
        if(!startGame && !startMenu){
            if(keyH.leftPressed){
                if(pointerIndex == 1){
                    pointerIndex = 0;
                    pointerX = startPointerX;
                }
                keyH.downPressed = false;
            }
            else if(keyH.rightPressed){
                if(pointerIndex == 0){
                    pointerIndex = 1;
                    pointerX = startPointerX+optionDistance;
                }
                keyH.upPressed = false;
            }
            
            if(keyH.pausePressed && pointerIndex==0){
                System.out.println("INVIOOOOOO");
                startGame = true;
                keyH.pausePressed = false;
            }
            if(keyH.pausePressed && pointerIndex==1){
                //disegna transizione
                startMenu = true;
                gp.currentPanel = new StartMenu(gp);
                keyH.pausePressed = false;
            }
        }
    }
}