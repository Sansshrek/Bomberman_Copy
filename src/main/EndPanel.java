package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class EndPanel implements Panel{
    KeyHandler keyH = KeyHandler.getInstance();
    BufferedImage victoryImage;
    boolean exit = false;
    int alphaVal = 0;

    public EndPanel(GamePanel gp){
        try {
            victoryImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/victory.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        gp.playMusic(16);
        gp.player.gamesWon += 1;
    }

    public void drawPanel(Graphics2D g2, GamePanel gp){
        g2.setColor(new Color(0, 0, 255));  // sfondo blu
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        g2.drawImage(victoryImage, 0, 0, gp.screenWidth, gp.screenHeight, null);
        
        if(exit && alphaVal<255){  // se il gioco è partito
            alphaVal += 5;  // aumenta il valore alpha per la transizione
        }
        closingTransition(g2, gp);  // e disegna la transizione
    }

    public void closingTransition(Graphics2D g2, GamePanel gp){
        g2.setColor(new Color(0, 0, 0, alphaVal));  // setta il colore con alpha
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);  // disegna il background
    }

    public void chooseOptions(GamePanel gp){
        if(exit && alphaVal==255){  // quando finisce la transizione
            gp.stopMusic();  // ferma la musica

            gp.saveScore();  // salva lo score
            gp.currentPanel = new StartMenu(gp);  // torna al menu principale
        }
        if(keyH.pausePressed){  // se preme enter allora inizia la transizione
            exit = true; 
        }
    }
}
