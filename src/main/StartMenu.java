package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class StartMenu {
    public GamePanel gp;
    public KeyHandler keyH;
    private long lastOptionChangeTime = 0;
    private final long optionChangeCoolDown = 200; // tempo di cooldown in millisecondi
    public int screenWidth, screenHeight, pointerX, pointerY, poinerIndex;
    public int options[][];
    public BufferedImage transitionImage, pointerImage, backgroundImage, ballonImage, cloudImage, optionStartStirngImage, optionSettingsStringImage, optionDataStringImage;
    public Graphics2D g2;

    public StartMenu(GamePanel gp, KeyHandler keyH){
        this.gp = gp;
        this.keyH = keyH;
        //Salvo nella lista options a seconda dell'opzione optionX, optionY
        options = new int[3][4];
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
        options[2][3] = (gp.screenHeight - options[2][1])/2+220;//optionY

        //caricamento immagini
        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("../res/startMenu/startMenu.png"));
            ballonImage = ImageIO.read(getClass().getResourceAsStream("../res/startMenu/Ballon.png"));
            cloudImage = ImageIO.read(getClass().getResourceAsStream("../res/startMenu/Cloud.png"));
            optionStartStirngImage = ImageIO.read(getClass().getResourceAsStream("../res/startMenu/Start.png"));
            optionSettingsStringImage = ImageIO.read(getClass().getResourceAsStream("../res/startMenu/Settings.png"));
            optionDataStringImage = ImageIO.read(getClass().getResourceAsStream("../res/startMenu/Data.png"));
            pointerImage = ImageIO.read(getClass().getResourceAsStream("../res/startMenu/Pointer.png"));
            transitionImage = ImageIO.read(getClass().getResourceAsStream("../res/startMenu/transiton.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void drawStartMenu(Graphics2D g2){
        g2.drawImage(backgroundImage, 0, 0, gp.screenWidth, gp.screenHeight, null);
        g2.drawImage(optionStartStirngImage, options[0][2], options[0][3], options[0][0], options[0][1], null);
        g2.drawImage(optionSettingsStringImage, options[1][2], options[1][3], options[1][0], options[1][1], null);
        g2.drawImage(optionDataStringImage, options[2][2], options[2][3], options[2][0], options[2][1], null);
        g2.drawImage(pointerImage, pointerX, pointerY, 50, 50, null);
    }
    public void chooseOptions(){
        pointerX = options[0][2]-60;
        pointerY = options[0][3];
        if(keyH.downPressed && System.currentTimeMillis()-lastOptionChangeTime>optionChangeCoolDown){
            lastOptionChangeTime = System.currentTimeMillis();
            poinerIndex+=1;
            if (poinerIndex>2){
                poinerIndex=0;
            }
        }
        else if(keyH.upPressed && System.currentTimeMillis()-lastOptionChangeTime>optionChangeCoolDown){
            lastOptionChangeTime = System.currentTimeMillis();
            poinerIndex-=1;
            if (poinerIndex<0){
                poinerIndex=2;
            }
        }
        //se preme invio e sono passati almeno 3 secondi dall'inizio dell'avvio del programma
        if(keyH.pausePressed  && poinerIndex==0 && System.currentTimeMillis()>3000 ){
            if(!gp.checkGameOn){
                //disegna transizione
            }
            gp.setupGame();
        }

        pointerX = options[poinerIndex][2];
        pointerY = options[poinerIndex][3];
    }

}