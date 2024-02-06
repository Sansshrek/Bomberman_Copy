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

public class ScoreMenu implements Panel{
    KeyHandler keyH = KeyHandler.getInstance(); 
    
    ArrayList<BufferedImage> letters = new ArrayList<>();

    BufferedImage backgroundImage, difficultyOptionImage, movementOptionImage, backImage, pointerImage, pointerDownImage, pointerUpImage, scoreBox;
    int boxX, boxY, boxWidth, boxHeight, nicknameX, nicknameY, letterDistance, optionX, optionY, startPointerX, pointerX, pointerY, pointerIndex = 0, pointerWidth, pointerHeight, choosePointerX, choosePointerUpY, choosePointerDownY, choosePointerWidth, choosePointerHeight, borderOffset, borderX, borderHeight, optionWidth, optionHeight, optionDistance, chooseDistance, totOptionNumber;
    // BufferedImage[] avatarImages = new BufferedImage[2];
    BufferedImage avatarImage;
    String nicknameString = "user";
    int selectedAvatarIndex = 1, nicknameLenght = 4, maxNicknameLenght = 10;  // difficulty: normal | movement: keyboard
    boolean nicknameSelect = false, avatarSelect = false;
    public ScoreMenu(GamePanel gp){
        totOptionNumber = 2;
        boxX = 15*gp.scale;
        boxY = 15*gp.scale;
        boxWidth = gp.screenWidth - boxX*2;
        boxHeight = gp.screenHeight/2;
        optionWidth = 55*gp.scale; //111*gp.scale / 2
        optionHeight = 10*gp.scale;
        nicknameX = (gp.screenWidth - optionWidth*2)/2+ optionWidth;
        nicknameY = (gp.screenWidth - optionWidth*2)/2 - 140;
        letterDistance = 16*gp.scale;
        optionX = (gp.screenWidth - optionWidth*2)/2 ;
        optionY = (gp.screenHeight - optionHeight)/2 - 140;
        pointerWidth = 8*gp.scale;
        pointerHeight = 12*gp.scale;
        choosePointerWidth = 6*gp.scale;
        choosePointerHeight = 8*gp.scale;
        pointerX = optionX - pointerWidth - 3*gp.scale;  // distanza di partenza del pointer
        startPointerX = optionX - 1*gp.scale;
        pointerX = startPointerX;
        optionDistance = 120*gp.scale;   //distanza tra le opzioni
        chooseDistance = 10*gp.scale;  // disanza tra le scelte e le opzioni
        
        borderOffset = 4*gp.scale;  // distanza tra il rettangolo e il testo
        borderX = optionX+optionWidth+chooseDistance-borderOffset;
        borderHeight = optionHeight+borderOffset*2;

        choosePointerX = borderX+(optionWidth/2)-choosePointerWidth/2;
        choosePointerDownY = optionY+optionHeight+borderOffset/2;
        choosePointerUpY = optionY-choosePointerHeight-borderOffset/2;  // posizione del puntatore per le scelte a meta del testo e del bordo del rettangolo
        try {
            for(char ch = 'a'; ch <= 'z'; ++ch){
                letters.add(ImageIO.read(getClass().getResourceAsStream("../res/letters/"+ch+".png")));
            }
            // ballonImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/Ballon.png"));
            // cloudImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/Cloud.png"));

            backgroundImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/Background.png"));
            movementOptionImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/options/movement.png"));  // scelta movement
            // avatarImages[0] = ImageIO.read(getClass().getResourceAsStream("../res/menu/options/mouseOption.png"));  // scelta mouse
            // avatarImages[1] = ImageIO.read(getClass().getResourceAsStream("../res/menu/options/keyboardOption.png"));  // scelta keyboard
            avatarImage = ImageIO.read(getClass().getResourceAsStream("../res/player/walking white/down2.png"));  // immagine player
            backImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/back.png"));  // back
            pointerImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/pointer.png"));
            // pointerUpImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/options/pointerLeft.png"));
            // pointerDownImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/options/pointerRight.png"));
            difficultyOptionImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/options/difficulty.png"));
            scoreBox = ImageIO.read(getClass().getResourceAsStream("../res/menu/scoreBoxTest.png"));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawPanel(Graphics2D g2, GamePanel gp){
        g2.setColor(new Color(0, 0, 255));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);  // disegna il background
        g2.drawImage(backgroundImage, 0, 0, gp.screenWidth, gp.screenHeight, null);  //Disegna l'etichetta del nickname
        g2.drawImage(letters.get(0), 100, 100, 15*gp.scale, 15*gp.scale, null);
        g2.drawImage(scoreBox, boxX, boxY, boxWidth, boxHeight, null);
        g2.drawImage(movementOptionImage, optionX, optionY, optionWidth, optionHeight, null);  //Disegna l'etichetta delle partite giocate
        // g2.drawImage(difficultyOptionImage, optionX, optionY+optionDistance, optionWidth, optionHeight, null);  //Disegna l'etichetta delle partite vinte
        // g2.drawImage(backImage, optionX, optionY+optionDistance*2, optionWidth, optionHeight, null);  //Disegna l'etichetta delle partite perse
        //g2.drawImage(movementImages[0], borderX, optionY-borderOffset, optionWidth, borderHeight, null);  //Disegna l'etichetta dello score massimo ottenuto
        //g2.drawImage(movementImages[1], borderX, optionY-borderOffset+optionDistance, optionWidth, borderHeight, null);  //Disegna l'etichetta dell'ultimo score ottenuto
        //g2.drawImage(difficultyImages[0], borderX, optionY-borderOffset+optionDistance, optionWidth, borderHeight, null);  //Disegna l'etichetta dell'ultimo livello raggiunto
        // g2.drawImage(pointerImage, pointerX, pointerY, pointerWidth, pointerHeight, null);
        // stampa nickname
        for (int i=0;i<nicknameString.length();i++){
            // System.out.println(nicknameString.charAt(i)-'a');
            g2.drawImage(letters.get(nicknameString.charAt(i)-'a'), nicknameX+letterDistance*i, nicknameY, letterDistance, letterDistance, null);
        }
        if(!nicknameSelect && !avatarSelect)  // se non ha selezionato nulla disegna il pointer dell'opzione
            g2.drawImage(pointerImage, pointerX, pointerY, pointerWidth, pointerHeight, null);
        if (nicknameSelect){
            //disegna un rettangolo rosso
            g2.setColor(new Color(255, 0, 0));
            g2.setStroke(new BasicStroke(2)); // Imposta lo spessore del bordo a 2
            g2.drawRect(borderX, optionY-borderOffset, optionWidth, borderHeight);
            //Metodo di disegno lettera per lettera a seconda del contenuto di nicknameString
            
        }else if (avatarSelect){
            //disegna un rettangolo rosso
            g2.setColor(new Color(255, 0, 0));
            g2.setStroke(new BasicStroke(2)); // Imposta lo spessore del bordo a 2
            // g2.drawRect(borderX, optionY-borderOffset+optionDistance, optionWidth, borderHeight);
            if(selectedAvatarIndex != 0)  // se mi trovo in una posizione diversa dalla prima disegna la freccia per andare sopra
                g2.drawImage(pointerUpImage, choosePointerX, choosePointerUpY+optionDistance, choosePointerWidth, choosePointerHeight, null);
            if(selectedAvatarIndex != 2)  // se mi trovo in una posizione diversa dall'ultima disegna la freccia per andare sotto
                g2.drawImage(pointerDownImage, choosePointerX, choosePointerDownY+optionDistance, choosePointerWidth, choosePointerHeight, null);
        }
    }

    public void chooseOptions(GamePanel gp){
        if(!nicknameSelect && !avatarSelect){  // se ancora non ha selezionato nulla
            if(keyH.rightPressed){  // se preme a destra
                System.out.println("destra");
                gp.playSfx(12);  // sound cursore
                pointerIndex+=1;  // aumenta il pointerIndex per andare alla prossima scelta
                if (pointerIndex == totOptionNumber){  // se supera il numero di scelte
                    pointerIndex=0;  // resetta l'index del puntatore e lo fa tornare alla prima scelta
                    pointerX = startPointerX;
                }else{
                    pointerX = pointerX+optionDistance;  // altrimenti fa scendere la posizione del puntatore alla scelta successiva
                }
                keyH.rightPressed = false;
            }else if(keyH.leftPressed){ 
                System.out.println("sx");
                gp.playSfx(12);  // sound cursore
                pointerIndex-=1;
                if (pointerIndex<0){
                    pointerIndex=totOptionNumber-1;
                    pointerX = startPointerX+optionDistance*(totOptionNumber-1);  // vai alla pos dell'ultima opzione
                }else{
                    pointerX = pointerX-optionDistance;
                }
                keyH.leftPressed = false;
            }
        }else if(nicknameSelect){
            System.out.println("nick");
            if (keyH.backspacePressed){
                if(nicknameString.length() > 0){
                    gp.playSfx(12);  // sound cursore
                    nicknameString = nicknameString.substring(0, nicknameString.length()-1);
                    nicknameLenght -= 1;
                }
                keyH.backspacePressed = false;
            }
            if (keyH.isKeyPressed()){
                //add the getLetter letter to the nickname
                if(nicknameString.length() < maxNicknameLenght){
                    gp.playSfx(12);  // sound cursore
                    nicknameString += keyH.getLetter();
                    nicknameLenght += 1;
                }
            }
        }else if(avatarSelect){
            if(keyH.downPressed && avatarSelect){
                gp.playSfx(12);  // sound cursore
                if(selectedAvatarIndex == 0)  // se sto alla prima opzione (mouse)
                    selectedAvatarIndex = 1;  // va all'opzione successiva (keyboard)
                System.out.println("Movimento: "+ selectedAvatarIndex);
                keyH.downPressed = false;
            }
            else if(keyH.upPressed && nicknameSelect){
                gp.playSfx(12);  // sound cursore
                if(selectedAvatarIndex == 1)  // se sto all'ultima opzione (keyboard)
                    selectedAvatarIndex = 0;  // va all'opzione precedente (mouse)
                System.out.println("Movimento: "+ selectedAvatarIndex);
                keyH.upPressed = false;
            }
        }
        // quando preme invio
        if(keyH.pausePressed){
            gp.playSfx(11);  // sound invio selezione
            if(pointerIndex==0){  // scelta difficolta
                System.out.println("INVIOOOOOO NICK");
                if (nicknameSelect){  // se è già selezionato
                    nicknameSelect = false;  // deseleziona e il pointer torna alla posizione iniziale
                }
                else{
                    nicknameSelect = true;  // altrimenti permette di selezionare come muoversi
                }
                keyH.pausePressed = false;

            }
            }else if(pointerIndex==1){  // scelta movimento
                System.out.println("INVIOOOOOO");
                if (avatarSelect){  // se è già selezionato
                    avatarSelect = false;  // deseleziona e il pointer torna alla posizione iniziale
                }
                else{
                    avatarSelect = true;  // altrimenti permette di selezionare come muoversi
                }
                keyH.pausePressed = false; /*
            else if(pointerIndex==2){  // back
                //disegna transizione
                gp.gameDifficulty = difficultyStrings[selectedDifficultyIndex];
                // gp.player.movementBehaviour = avatarChoice[selectedAvatarIndex];
                System.out.println("Scelta difficolta: "+ gp.gameDifficulty);
                gp.currentPanel = new StartMenu(gp);
                keyH.pausePressed = false;
            }*/
        }
    }
}