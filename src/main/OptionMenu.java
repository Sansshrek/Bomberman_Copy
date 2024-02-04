package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.BasicStroke;
import java.awt.Color;

import javax.imageio.ImageIO;

import entity.BasePlayerBehaviour;
import entity.MouseBehaviour;

public class OptionMenu implements Panel{
    KeyHandler keyH = KeyHandler.getInstance(); 
    
    BufferedImage backgroundImage, optionStartImage, optionSettingsImage, optionScoreImage, pointerImage;
    int titleX, titleY, titleWidth, titleHeight, optionX, optionY, startPointerY, pointerX, pointerY, difficultyIndex = 0, pointerIndex = 0, pointerWidth, pointerHeight, optionWidth, optionHeight, optionDistance, totOptionNumber;
    ArrayList<BufferedImage> difficultyImage = new ArrayList<>();
    boolean difficultySelect = false;
    //Salvo nella lista options a seconda dell'opzione optionX, optionY
    /*options = new int[3][4]; 
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
    options[2][3] = (gp.screenHeight - options[2][1])/2+220;//optionY0*/
    public OptionMenu(GamePanel gp){
        totOptionNumber = 3;
        titleX = 17*gp.scale;
        titleY = 11*gp.scale;
        titleWidth = 205*gp.scale;
        titleHeight = 136*gp.scale;
        optionWidth = 55*gp.scale; //111*gp.scale
        optionHeight = 10*gp.scale;
        optionX = (gp.screenWidth - optionWidth)/2;
        optionY = (gp.screenHeight - optionHeight)/2 - 140;
        pointerWidth = 8*gp.scale;
        pointerHeight = 12*gp.scale;
        pointerX = optionX - pointerWidth - 3*gp.scale;  // distanza di partenza del pointer
        startPointerY = optionY - 1*gp.scale;
        pointerY = startPointerY;
        optionDistance = 30*gp.scale;   //distanza tra le opzioni
        try {
            // ballonImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/Ballon.png"));
            // cloudImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/Cloud.png"));
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/Background.png"));
            optionStartImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/Start.png"));
            optionSettingsImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/Settings.png"));
            optionScoreImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/Score.png"));
            pointerImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/pointer.png"));
            difficultyImage.add(ImageIO.read(getClass().getResourceAsStream("../res/menu/Background.png")));
            difficultyImage.add(ImageIO.read(getClass().getResourceAsStream("../res/menu/Start.png")));
            difficultyImage.add(ImageIO.read(getClass().getResourceAsStream("../res/menu/Settings.png")));
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
        g2.drawImage(optionSettingsImage, optionX+optionWidth+3*gp.scale, optionY, optionWidth, optionHeight, null);
        g2.drawImage(optionScoreImage, optionX, optionY+optionDistance*2, optionWidth, optionHeight, null);
        g2.drawImage(pointerImage, pointerX, pointerY, pointerWidth, pointerHeight, null);
        if (difficultySelect){
            //disegna un rettangolo rosso
            g2.setColor(new Color(255, 0, 0, 255));
            g2.setStroke(new BasicStroke(4)); // Imposta lo spessore del bordo a 3
            g2.drawRect(optionX+optionWidth+3*gp.scale, optionY, optionWidth, optionHeight);
        }
    }

    public void chooseOptions(GamePanel gp){
        if(keyH.downPressed && !difficultySelect){
                pointerIndex+=1;
                if (pointerIndex == totOptionNumber){
                    pointerIndex=0;
                    pointerY = startPointerY;
                }else{
                    pointerY = pointerY+optionDistance;
                }
                keyH.downPressed = false;
            }
            else if(keyH.upPressed && !difficultySelect){
                pointerIndex-=1;
                if (pointerIndex<0){
                    pointerIndex=totOptionNumber-1;
                    pointerY = startPointerY+optionDistance*(totOptionNumber-1);  // vai alla pos dell'ultima opzione
                }else{
                    pointerY = pointerY-optionDistance;
                }
                keyH.upPressed = false;
            }
            if(keyH.downPressed && difficultySelect){
                difficultyIndex+=1;
                if (difficultyIndex == 3){
                    difficultyIndex=0;
                }
                System.out.println("Difficoltà: "+ difficultyIndex);
                keyH.downPressed = false;
            }
            else if(keyH.upPressed && difficultySelect){
                difficultyIndex-=1;
                if (difficultyIndex<0){
                    difficultyIndex=2;
                }
                System.out.println("Difficoltà: "+ difficultyIndex);
                keyH.upPressed = false;
            }
            if(keyH.pausePressed && pointerIndex==0){
                System.out.println("INVIOOOOOO");
                if (difficultySelect){
                difficultySelect = false;
                }
                else{
                difficultySelect = true;
                }
                keyH.pausePressed = false;

            }
            if(keyH.pausePressed && pointerIndex==1){
                //disegna transizione
                // gp.currentPanel = new OptionMenu();
                keyH.pausePressed = false;
            }
            if(keyH.pausePressed && pointerIndex==2){
                //disegna transizione
                
                gp.currentPanel = new StartMenu(gp);
                keyH.pausePressed = false;
            }
    }
}