package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import entity.BasePlayerBehaviour;
import entity.MouseBehaviour;

public class OptionMenu implements Panel{
    KeyHandler keyH = KeyHandler.getInstance(); 
    
    BufferedImage backgroundImage, ballonImage, cloudImage, optionStartImage, optionSettingsImage, optionScoreImage, pointerImage;
    public int optionX, optionY, pointerIndex, optionWidth, optionHeight, totOptionNumber=3;
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
    public OptionMenu(){
        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("../res/startMenu/startMenu.png"));
            ballonImage = ImageIO.read(getClass().getResourceAsStream("../res/startMenu/Ballon.png"));
            cloudImage = ImageIO.read(getClass().getResourceAsStream("../res/startMenu/Cloud.png"));
            optionStartImage = ImageIO.read(getClass().getResourceAsStream("../res/startMenu/Start.png"));
            optionSettingsImage = ImageIO.read(getClass().getResourceAsStream("../res/startMenu/Settings.png"));
            optionScoreImage = ImageIO.read(getClass().getResourceAsStream("../res/startMenu/Data.png"));
            pointerImage = ImageIO.read(getClass().getResourceAsStream("../res/startMenu/Pointer.png"));
            // transitionImage = ImageIO.read(getClass().getResourceAsStream("../res/startMenu/transiton.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawPanel(Graphics2D g2, GamePanel gp){
        g2.drawImage(backgroundImage, 0, 0, gp.screenWidth, gp.screenHeight, null);
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
            gp.currentPanel = new OptionMenu();
        }
        if(keyH.pausePressed && pointerIndex==1){  //scelta movementBheavior
            //disegna un rettangolo rosso se selezionato 
            if(keyH.pausePressed && gp.player.movementBehaviour instanceof BasePlayerBehaviour)
                //cambia la scritta dell'opzione
                gp.player.movementBehaviour = new BasePlayerBehaviour();
            if(keyH.pausePressed && gp.player.movementBehaviour instanceof MouseBehaviour){
                //cambia la scritta dell'opzione
                gp.player.movementBehaviour = new MouseBehaviour();
            }
        }
        if(keyH.pausePressed && pointerIndex==2){  //scelta della difficlotà
            //disegna un rettangolo rosso se selezionato
        }
        pointerX = options[poinerIndex][2];
        pointerY = options[poinerIndex][3];
    }
}