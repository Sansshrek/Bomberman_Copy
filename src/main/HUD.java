package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class HUD {
    public GamePanel gp;
    public int tileSize;
    public int scale;
    public BufferedImage image;
    public int hudHeight;
    private BufferedImage[] numberImages, timerHandImage;
    int numberWidth, numberHeight;
    int scoreX;
    int scoreY;
    int scoreWidth ;
    int scoreHeight;
    int lifeX;
    int lifeY;
    int timerTick, timerHand;
    public int clockLeft;
    int timerX, timerY, timerWidth, timerHeight;
    int clockHandX, clockHandY, clockHandWidth, clockHandHeight;

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
        clockHandX = 125*scale;
        clockHandY = 15*scale;
        clockHandWidth = 7*scale;
        clockHandHeight = 7*scale;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("../res/HUD/HUD.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        resetTimer();
        loadClockImages();
        loadNumberImages();
    }

    public void resetTimer(){
        this.clockLeft = 28;
        this.timerTick = 0;
        this.timerHand = 0;
        this.timerX = 10*scale;
        this.timerY = 26*scale;
        this.timerWidth = 228*scale;
        this.timerHeight = 3*scale;
    }

    public void loadClockImages(){
        timerHandImage = new BufferedImage[8];
        try {
            for(int i=0; i<8; i++){
                timerHandImage[i] = ImageIO.read(getClass().getResourceAsStream("../res/HUD/timer"+i+".png"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void updateTime(){
        // per disegnare il timer crea un rettangolo bianco al di sotto dell'HUD e ad ogni diminuzione del timer (clockLeft) cambia la x di dove parte il rettangolo aggiungendo 
        // la distanza tra l'inizio di un blocco all'inizio di quello dopo e diminuisce della stessa distanza la larghezza del rettangolo cosi che restringa il rettangolo solo da sinistra verso destra
        timerTick++;  // contatore che aumenta indipendentemente a ogni update dell'orologio 
        if(timerTick == 90){  // per ogni tot. tick dell'update  (modificare qui per cambiare il tempo che ci mette )
        // 90(tick) * 8 (giri lancetta) = 720 tick totali -> 720/60 (FPS) = 12 secondi (durata di un giro totale di orologio)
            timerHand++;  // gira la lancetta dell'orologio di 1
            timerTick = 0;  // resetta il contatore dei tick a 0
        }
        // dopo timerTick * 8 (quante volte gira la lancetta) diminuisce di 1 il tempo rimasto
        // 60 FPS. se timerTick = 30 allora 30*8 = 240 -> 240/60 = 4sec (secondi che impiega per diminuire di 1 il tempo rimasto)
        // dovrebbe essere giusto il calcolo credo
        if(timerHand == 8){  // quando il contatore raggiunge un certo limite decrementa il numero di tempo rimasto
            timerHand = 0; // resetta la lancetta dell'orologio
            clockLeft--;  // e diminuisce il tempo rimasto per giocare
            timerX += 8*scale;  // larghezza di un blocco  (8 pixel)
            timerWidth -= 8*scale;
            if(clockLeft == 14){  // quando arriva all'orologio aumenta la X del rect della grandezza dell'orologio
                timerX += 16*scale;  // larghezza orologio  (16 pixel)
                timerWidth -= 16*scale;
            }
        }
    }

    public void drawTime(Graphics2D g2){
        g2.setColor(Color.WHITE);
        g2.fillRect(timerX, timerY, timerWidth, timerHeight);
    }

    public void drawClock(Graphics2D g2){
        g2.drawImage(timerHandImage[timerHand], clockHandX, clockHandY, clockHandWidth, clockHandHeight, null);
    }
}