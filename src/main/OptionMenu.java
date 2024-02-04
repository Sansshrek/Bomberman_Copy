package main;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.BasicStroke;
import java.awt.Color;

import javax.imageio.ImageIO;

import entity.BasePlayerBehaviour;
import entity.EntityMovementBehaviour;
import entity.MouseBehaviour;

public class OptionMenu implements Panel{
    KeyHandler keyH = KeyHandler.getInstance(); 

    BufferedImage backgroundImage, difficultyOptionImage, movementOptionImage, backImage, pointerImage, pointerDownImage, pointerUpImage;
    int titleX, titleY, titleWidth, titleHeight, optionX, optionY, startPointerY, pointerX, pointerY, pointerIndex = 0, pointerWidth, pointerHeight, choosePointerX, choosePointerUpY, choosePointerDownY, choosePointerWidth, choosePointerHeight, borderOffset, borderX, borderHeight, optionWidth, optionHeight, optionDistance, chooseDistance, totOptionNumber;
    BufferedImage[] difficultyImages = new BufferedImage[3];
    String[] difficultyStrings = {"easy", "normal", "hard"};
    BufferedImage[] movementImages = new BufferedImage[2];
    EntityMovementBehaviour[] movementBehaviours = {new MouseBehaviour(), new BasePlayerBehaviour()}; // mouse and player controls
    int selectedDifficultyIndex = 1, selectedMovementIndex = 1;  // difficulty: normal | movement: keyboard
    boolean difficultySelect = false, movementSelect = false;
    public OptionMenu(GamePanel gp){
        totOptionNumber = 3;
        titleX = 17*gp.scale;
        titleY = 11*gp.scale;
        titleWidth = 205*gp.scale;
        titleHeight = 136*gp.scale;
        optionWidth = 55*gp.scale; //111*gp.scale / 2
        optionHeight = 10*gp.scale;
        optionX = (gp.screenWidth - optionWidth*2)/2 ;
        optionY = (gp.screenHeight - optionHeight)/2 - 140;
        pointerWidth = 8*gp.scale;
        pointerHeight = 12*gp.scale;
        choosePointerWidth = 6*gp.scale;
        choosePointerHeight = 8*gp.scale;
        pointerX = optionX - pointerWidth - 3*gp.scale;  // distanza di partenza del pointer
        startPointerY = optionY - 1*gp.scale;
        pointerY = startPointerY;
        optionDistance = 30*gp.scale;   //distanza tra le opzioni
        chooseDistance = 10*gp.scale;  // disanza tra le scelte e le opzioni
        borderOffset = 4*gp.scale;  // distanza tra il rettangolo e il testo
        borderX = optionX+optionWidth+chooseDistance-borderOffset;
        borderHeight = optionHeight+borderOffset*2;
        choosePointerX = borderX+(optionWidth/2)-choosePointerWidth/2;
        choosePointerDownY = optionY+optionHeight+borderOffset/2;
        choosePointerUpY = optionY-choosePointerHeight-borderOffset/2;  // posizione del puntatore per le scelte a meta del testo e del bordo del rettangolo
        try {
            // ballonImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/Ballon.png"));
            // cloudImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/Cloud.png"));
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/Background.png"));
            movementOptionImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/options/movement.png"));  // scelta movement
            movementImages[0] = ImageIO.read(getClass().getResourceAsStream("../res/menu/options/mouseOption.png"));  // scelta mouse
            movementImages[1] = ImageIO.read(getClass().getResourceAsStream("../res/menu/options/keyboardOption.png"));  // scelta keyboard
            backImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/back.png"));  // back
            pointerImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/pointer.png"));
            pointerUpImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/options/pointerUp.png"));
            pointerDownImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/options/pointerDown.png"));
            difficultyOptionImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/options/difficulty.png"));
            difficultyImages[0] = ImageIO.read(getClass().getResourceAsStream("../res/menu/options/easyDifficulty.png"));
            difficultyImages[1] = ImageIO.read(getClass().getResourceAsStream("../res/menu/options/normalDifficulty.png"));
            difficultyImages[2] = ImageIO.read(getClass().getResourceAsStream("../res/menu/options/hardDifficulty.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawPanel(Graphics2D g2, GamePanel gp){
        g2.setColor(new Color(0, 0, 255));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);  // disegna il background
        g2.drawImage(backgroundImage, 0, 0, gp.screenWidth, gp.screenHeight, null);
        g2.drawImage(movementOptionImage, optionX, optionY, optionWidth, optionHeight, null);
        g2.drawImage(movementImages[selectedMovementIndex], optionX+optionWidth+chooseDistance, optionY, optionWidth, optionHeight, null);
        g2.drawImage(difficultyOptionImage, optionX, optionY+optionDistance, optionWidth, optionHeight, null);
        g2.drawImage(difficultyImages[selectedDifficultyIndex], optionX+optionWidth+chooseDistance, optionY+optionDistance, optionWidth, optionHeight, null);
        g2.drawImage(backImage, optionX, optionY+optionDistance*2, optionWidth, optionHeight, null);
        // g2.drawImage(pointerImage, pointerX, pointerY, pointerWidth, pointerHeight, null);
        if(!movementSelect && !difficultySelect)  // se non ha selezionato nulla disegna il pointer dell'opzione
            g2.drawImage(pointerImage, pointerX, pointerY, pointerWidth, pointerHeight, null);
        if (movementSelect){
            //disegna un rettangolo rosso
            g2.setColor(new Color(255, 0, 0, 255));
            g2.setStroke(new BasicStroke(4)); // Imposta lo spessore del bordo a 4
            g2.drawRect(borderX, optionY-borderOffset, optionWidth, borderHeight);
            if(selectedMovementIndex == 0)  // se sto alla prima opzione disegna la freccia per andare sotto
                g2.drawImage(pointerDownImage, choosePointerX, choosePointerDownY, choosePointerWidth, choosePointerHeight, null);
            else if(selectedMovementIndex == 1)  // se sto all'ultima opzione disegna la freccia per andare sopra 
                g2.drawImage(pointerUpImage, choosePointerX, choosePointerUpY, choosePointerWidth, choosePointerHeight, null);
        }else if (difficultySelect){
            //disegna un rettangolo rosso
            g2.setColor(new Color(255, 0, 0, 255));
            g2.setStroke(new BasicStroke(4)); // Imposta lo spessore del bordo a 4
            g2.drawRect(borderX, optionY-borderOffset+optionDistance, optionWidth, borderHeight);
            if(selectedDifficultyIndex != 0)  // se mi trovo in una posizione diversa dalla prima disegna la freccia per andare sopra
                g2.drawImage(pointerUpImage, choosePointerX, choosePointerUpY+optionDistance, choosePointerWidth, choosePointerHeight, null);
            if(selectedDifficultyIndex != 2)  // se mi trovo in una posizione diversa dall'ultima disegna la freccia per andare sotto
                g2.drawImage(pointerDownImage, choosePointerX, choosePointerDownY+optionDistance, choosePointerWidth, choosePointerHeight, null);
        }
    }

    public void chooseOptions(GamePanel gp){
        if(!difficultySelect && !movementSelect){  // se ancora non ha selezionato nulla
            if(keyH.downPressed){
                pointerIndex+=1;  // aumenta il pointerIndex per andare alla prossima scelta
                if (pointerIndex == totOptionNumber){  // se supera il numero di scelte
                    pointerIndex=0;  // resetta l'index del puntatore e lo fa tornare alla prima scelta
                    pointerY = startPointerY;
                }else{
                    pointerY = pointerY+optionDistance;  // altrimenti fa scendere la posizione del puntatore alla scelta successiva
                }
                keyH.downPressed = false;
            }else if(keyH.upPressed){ 
                pointerIndex-=1;
                if (pointerIndex<0){
                    pointerIndex=totOptionNumber-1;
                    pointerY = startPointerY+optionDistance*(totOptionNumber-1);  // vai alla pos dell'ultima opzione
                }else{
                    pointerY = pointerY-optionDistance;
                }
                keyH.upPressed = false;
            }
        }
        // scelta difficolta
        else if(difficultySelect){
            if(keyH.downPressed && difficultySelect){
                if(selectedDifficultyIndex < 2)  // se non sono all'ultima scekta
                    selectedDifficultyIndex+=1;  // posso andare alla scelta successiva
                System.out.println("Difficoltà: "+ selectedDifficultyIndex);
                keyH.downPressed = false;
            }
            else if(keyH.upPressed && difficultySelect){
                if(selectedDifficultyIndex > 0)  // se non sono alla prima scelta
                    selectedDifficultyIndex-=1;  // posso andare alla scelta precedente
                System.out.println("Difficoltà: "+ selectedDifficultyIndex);
                keyH.upPressed = false;
            }
        }else if(movementSelect){
            if(keyH.downPressed && movementSelect){
                if(selectedMovementIndex == 0)  // se sto alla prima opzione (mouse)
                    selectedMovementIndex = 1;  // va all'opzione successiva (keyboard)
                System.out.println("Movimento: "+ selectedMovementIndex);
                keyH.downPressed = false;
            }
            else if(keyH.upPressed && movementSelect){
                if(selectedMovementIndex == 1)  // se sto all'ultima opzione (keyboard)
                    selectedMovementIndex = 0;  // va all'opzione precedente (mouse)
                System.out.println("Movimento: "+ selectedMovementIndex);
                keyH.upPressed = false;
            }
        }
        // quando preme invio
        if(keyH.pausePressed){
            if(pointerIndex==0){  // scelta movimento
                System.out.println("INVIOOOOOO");
                if (movementSelect){  // se è già selezionato
                    movementSelect = false;  // deseleziona e il pointer torna alla posizione iniziale
                }
                else{
                    movementSelect = true;  // altrimenti permette di selezionare come muoversi
                }
                keyH.pausePressed = false;
            }else if(pointerIndex==1){  // scelta difficolta
                System.out.println("INVIOOOOOO");
                if (difficultySelect){  // se è già selezionato
                    difficultySelect = false;  // deseleziona e il pointer torna alla posizione iniziale
                }
                else{
                    difficultySelect = true;  // altrimenti permette di selezionare come muoversi
                }
                keyH.pausePressed = false;

            }else if(pointerIndex==2){  // back
                //disegna transizione
                gp.gameDifficulty = difficultyStrings[selectedDifficultyIndex];
                gp.player.movementBehaviour = movementBehaviours[selectedMovementIndex];
                System.out.println("Scelta difficolta: "+ gp.gameDifficulty);
                gp.currentPanel = new StartMenu(gp);
                keyH.pausePressed = false;
            }
        }
    }
}