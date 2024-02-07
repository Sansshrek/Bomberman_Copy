package main;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.awt.BasicStroke;
import java.awt.Color;

import javax.imageio.ImageIO;

import entity.BasePlayerBehaviour;
import entity.EntityMovementBehaviour;
import entity.MouseBehaviour;

public class ScoreMenu implements Panel{
    KeyHandler keyH = KeyHandler.getInstance(); 
    
    ArrayList<BufferedImage> letters = new ArrayList<>();
    ArrayList<BufferedImage> numbers = new ArrayList<>();
    Map<String, BufferedImage> characters = new HashMap<>();

    BufferedImage backgroundImage, backImage, pointerImage, pointerLeftImage, pointerRightImage, scoreBox;
    int nicknameX, nicknameY, borderOffset, borderX, borderWidth, borderHeight;  // nickname
    int boxX, boxY, boxWidth, boxHeight, nicknameLetterDistance, gamesLetterDistance, lastLetterDistance;  // box e lettere
    int bestScoreX, lastInfoX, lastInfoY, gamesInfoX, gamesInfoY, infoDistance; // informazioni
    int avatarX, avatarY, avatarWidth, avatarHeight;
    int backImageX, backImageY, backImageWidth, backImageHeight;
    int startPointerX, startPointerY, pointerX, pointerY, backPointerX, backPointerY, pointerIndex = 0, pointerWidth, pointerHeight, pointerOptionDistance, choosePointerX, choosePointerWidth, choosePointerHeight;
    int avatarPointerX, avatarPointerY, avatarPointerWidth, avatarPointerHeight, avatarPointerDistance;
    BufferedImage[] avatarImages = new BufferedImage[4];
    BufferedImage avatarImage;
    String nicknameString = "nickname", bestScoreString = "00000", lastGameString = "1-2", lastScoreString = "00000", gamesWonString = "00", gamesLostString = "00", gamesPlayedString = "00";
    int selectedAvatarIndex = 0, totAvatarOption = 4, maxNicknameLenght = 8, scoreLenght = 1, scoreMaxLenght = 6, scoreDefault = 0, totOptionNumber = 3, gpScale;  // difficulty: normal | movement: keyboard
    boolean nicknameSelect = false, avatarSelect = false;
    public ScoreMenu(GamePanel gp){
        gpScale = gp.scale;
        boxX = 15*gpScale;
        boxY = 15*gpScale;
        boxWidth = gp.screenWidth - boxX*2;
        boxHeight = gp.screenHeight/2;
        // nickname info
        nicknameX = 25*gpScale;
        nicknameY = 30*gpScale;
        nicknameLetterDistance = 8*gpScale;
        // nickname border info
        borderOffset = 2*gpScale;
        borderWidth = nicknameLetterDistance*8 + borderOffset*2;
        borderHeight = nicknameLetterDistance + borderOffset*2;
        // avatar info
        avatarX = nicknameX + 7*gpScale;
        avatarY = nicknameY + 20*gpScale;
        avatarWidth = gp.player.hittableWidth * 2;
        avatarHeight = gp.player.hittableHeight * 2;
        // avatar pointer index
        avatarPointerWidth = 8*gpScale;
        avatarPointerHeight = 12*gpScale;
        avatarPointerX = avatarX - 10*gpScale;
        avatarPointerY = (avatarY + avatarY+avatarHeight)/2 - avatarPointerHeight/2;
        avatarPointerDistance = avatarWidth + 10*gpScale;
        // bestScore
        bestScoreX = gp.screenWidth/2 + 55*gpScale;
        // last info
        lastInfoX = boxX + boxWidth- 30*gpScale;
        lastInfoY = nicknameY + 41*gpScale;
        lastLetterDistance = 5*gpScale;
        // games info
        gamesInfoX = gp.screenWidth/2 + 21*gpScale;
        gamesInfoY = nicknameY + 31*gpScale;
        gamesLetterDistance = 7*gpScale;
        infoDistance = 19*gpScale;
        // pointer
        pointerWidth = 8*gpScale;
        pointerHeight = 12*gpScale;
        startPointerX = 17*gpScale;
        startPointerY = 28*gpScale;
        pointerX = startPointerX;
        pointerY = startPointerY;
        pointerOptionDistance = nicknameY + 20*gpScale;   //distanza tra le opzioni
        // back
        backImageX = gp.screenWidth/2 - 25*gpScale;
        backImageY = gp.screenHeight/2 + 30*gpScale;
        backImageWidth = 111*gp.scale;
        backImageHeight = 10*gp.scale;
        backPointerX = backImageX - 10*gpScale;
        backPointerY = backImageY;
        // chooseDistance = 10*gpScale;  // disanza tra le scelte e le opzioni
        
        /*
        borderOffset = 4*gpScale;  // distanza tra il rettangolo e il testo
        borderX = optionX+optionWidth+chooseDistance-borderOffset;
        borderHeight = optionHeight+borderOffset*2;

        choosePointerX = borderX+(optionWidth/2)-choosePointerWidth/2;
        choosePointerDownY = optionY+optionHeight+borderOffset/2;
        choosePointerUpY = optionY-choosePointerHeight-borderOffset/2;  // posizione del puntatore per le scelte a meta del testo e del bordo del rettangolo
        */
        try {
            avatarImages[0] = ImageIO.read(getClass().getResourceAsStream("../res/player/walking white/down2.png"));
            avatarImages[1] = ImageIO.read(getClass().getResourceAsStream("../res/player/walking blue/down2.png"));
            avatarImages[2] = ImageIO.read(getClass().getResourceAsStream("../res/player/walking black/down2.png"));
            avatarImages[3] = ImageIO.read(getClass().getResourceAsStream("../res/player/walking orange/down2.png"));
            for(char ch = 'a'; ch <= 'z'; ++ch){
                // letters.add(ImageIO.read(getClass().getResourceAsStream("../res/letters/"+ch+".png")));
                characters.put(Character.toString(ch), ImageIO.read(getClass().getResourceAsStream("../res/letters/"+ch+".png")));
            }
            for(int i = 0; i < 10; ++i){
                // numbers.add(ImageIO.read(getClass().getResourceAsStream("../res/letters/"+i+".png")));
                characters.put(Integer.toString(i), ImageIO.read(getClass().getResourceAsStream("../res/letters/"+i+".png")));
            }
            characters.put("-", ImageIO.read(getClass().getResourceAsStream("../res/letters/hypen.png")));


            // ballonImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/Ballon.png"));
            // cloudImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/Cloud.png"));

            backgroundImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/Background.png"));
            // avatarImages[0] = ImageIO.read(getClass().getResourceAsStream("../res/menu/options/mouseOption.png"));  // scelta mouse
            // avatarImages[1] = ImageIO.read(getClass().getResourceAsStream("../res/menu/options/keyboardOption.png"));  // scelta keyboard
            avatarImage = ImageIO.read(getClass().getResourceAsStream("../res/player/walking white/down2.png"));  // immagine player
            backImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/back.png"));  // back
            pointerImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/pointer.png"));
            pointerRightImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/pointer.png"));
            pointerLeftImage = ImageIO.read(getClass().getResourceAsStream("../res/menu/options/pointerLeft.png"));
            scoreBox = ImageIO.read(getClass().getResourceAsStream("../res/menu/scoreBox.png"));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawPanel(Graphics2D g2, GamePanel gp){
        g2.setColor(new Color(0, 0, 255));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);  // disegna il background
        g2.drawImage(backgroundImage, 0, 0, gp.screenWidth, gp.screenHeight, null);  //Disegna l'etichetta del nickname
        // g2.drawImage(letters.get(0), 100, 100, 15*gpScale, 15*gpScale, null);
        g2.drawImage(scoreBox, boxX, boxY, boxWidth, boxHeight, null);
        g2.drawImage(backImage, backImageX, backImageY, backImageWidth, backImageHeight, null);  //Disegna l'opzione back
        g2.drawImage(avatarImages[selectedAvatarIndex], avatarX, avatarY, avatarWidth, avatarHeight, null);
        // stampa nickname
        printCharacters(g2, nicknameString, nicknameX, nicknameY, nicknameLetterDistance, nicknameLetterDistance, nicknameLetterDistance);
        // stampa score
        printCharacters(g2, bestScoreString, bestScoreX, nicknameY, nicknameLetterDistance, nicknameLetterDistance, nicknameLetterDistance);
        printCharacters(g2, lastGameString, lastInfoX, lastInfoY, lastLetterDistance, lastLetterDistance, lastLetterDistance);
        printCharacters(g2, lastScoreString, lastInfoX, lastInfoY+infoDistance, lastLetterDistance, lastLetterDistance, lastLetterDistance);
        printCharacters(g2, gamesWonString, gamesInfoX, gamesInfoY, gamesLetterDistance, gamesLetterDistance, gamesLetterDistance);
        printCharacters(g2, gamesLostString, gamesInfoX, gamesInfoY+infoDistance, gamesLetterDistance, gamesLetterDistance, gamesLetterDistance);
        printCharacters(g2, gamesPlayedString, gamesInfoX, gamesInfoY+infoDistance*2, gamesLetterDistance, gamesLetterDistance, gamesLetterDistance);
        
        if(!nicknameSelect && !avatarSelect)  // se non ha selezionato nulla disegna il pointer dell'opzione
            g2.drawImage(pointerImage, pointerX, pointerY, pointerWidth, pointerHeight, null);
        if (nicknameSelect){
            //disegna un rettangolo rosso
            g2.setColor(new Color(255, 0, 0));
            g2.setStroke(new BasicStroke(2)); // Imposta lo spessore del bordo a 2
            g2.drawRect(nicknameX-borderOffset, nicknameY-borderOffset, borderWidth, borderHeight);
            //Metodo di disegno lettera per lettera a seconda del contenuto di nicknameString
            
        }else if (avatarSelect){
            //disegna un rettangolo rosso
            g2.setColor(new Color(255, 0, 0));
            g2.setStroke(new BasicStroke(2)); // Imposta lo spessore del bordo a 2
            // g2.drawRect(borderX, optionY-borderOffset+optionDistance, optionWidth, borderHeight);
            g2.drawImage(pointerLeftImage, avatarPointerX, avatarPointerY, avatarPointerWidth, avatarPointerWidth, null);
            g2.drawImage(pointerRightImage, avatarPointerX + avatarPointerDistance, avatarPointerY, avatarPointerWidth, avatarPointerWidth, null);
            /*
            if(selectedAvatarIndex != 0)  // se mi trovo in una posizione diversa dalla prima disegna la freccia per andare sopra
                g2.drawImage(pointerUpImage, choosePointerX, choosePointerUpY+optionDistance, choosePointerWidth, choosePointerHeight, null);
            if(selectedAvatarIndex != 2)  // se mi trovo in una posizione diversa dall'ultima disegna la freccia per andare sotto
                g2.drawImage(pointerDownImage, choosePointerX, choosePointerDownY+optionDistance, choosePointerWidth, choosePointerHeight, null);
            */
        }
    }

    public void printCharacters(Graphics2D g2, String string, int x, int y, int width, int height, int letterDistance){
        for (int i=0;i<string.length();i++){
            BufferedImage image = characters.get(Character.toString(string.charAt(i)));
            g2.drawImage(image, x+letterDistance*i, y, letterDistance, letterDistance, null);
        }
    }

    public void chooseOptions(GamePanel gp){
        if(!nicknameSelect && !avatarSelect){  // se ancora non ha selezionato nulla
            System.out.println(pointerIndex);
            if(keyH.upPressed){  // se preme a destra
                gp.playSfx(12);  // sound cursore
                pointerIndex--;  // aumenta il pointerIndex per andare alla prossima scelta
                if (pointerIndex < 0)  // se supera il numero di scelte
                    pointerIndex = totOptionNumber-1;  // resetta l'index del puntatore e lo fa tornare alla prima scelta
                if(pointerIndex == 0){
                    pointerX = startPointerX;
                    pointerY = startPointerY;
                }else if(pointerIndex == 1){
                    pointerX = startPointerX;
                    pointerY = startPointerY + pointerOptionDistance;
                }else{  // è il back
                    pointerX = backPointerX;
                    pointerY = backPointerY;
                }
                keyH.upPressed = false;
            }else if(keyH.downPressed){ 
                System.out.println("sx");
                gp.playSfx(12);  // sound cursore
                pointerIndex++;
                if (pointerIndex == totOptionNumber)  // se supera il numero di scelte
                    pointerIndex=0;  // resetta l'index del puntatore e lo fa tornare alla prima scelta
                if(pointerIndex == 0){
                    pointerX = startPointerX;
                    pointerY = startPointerY;
                }else if(pointerIndex == 1){
                    pointerX = startPointerX;
                    pointerY = startPointerY + pointerOptionDistance;
                }else{  // è il back
                    pointerX = backPointerX;
                    pointerY = backPointerY;
                }
                keyH.downPressed = false;
            }
        }else if(nicknameSelect){
            if (keyH.backspacePressed){
                if(nicknameString.length() > 0){
                    gp.playSfx(12);  // sound cursore
                    nicknameString = nicknameString.substring(0, nicknameString.length()-1);
                }
                keyH.backspacePressed = false;
            }
            if (keyH.isKeyPressed()){
                //add the getLetter letter to the nickname
                if(nicknameString.length() < maxNicknameLenght){
                    gp.playSfx(12);  // sound cursore
                    nicknameString += keyH.getLetter();
                }
            }
        }else if(avatarSelect){
            if(keyH.leftPressed && avatarSelect){
                gp.playSfx(12);  // sound cursore
                selectedAvatarIndex--;
                if(selectedAvatarIndex < 0)  // se va prima del primo avatar
                    selectedAvatarIndex = totAvatarOption-1;  // va all'ultimo avatar
                System.out.println("Avatar: "+ selectedAvatarIndex);
                keyH.leftPressed = false;
            }
            else if(keyH.rightPressed && avatarSelect){
                gp.playSfx(12);  // sound cursore
                selectedAvatarIndex++;
                if(selectedAvatarIndex == totAvatarOption)  // se supera l'ultimo avatar
                    selectedAvatarIndex = 0;  // torna al primo
                System.out.println("avatar: "+ selectedAvatarIndex);
                keyH.rightPressed = false;
            }
        }
        // quando preme invio
        if(keyH.pausePressed){
            gp.playSfx(11);  // sound invio selezione
            if(pointerIndex==0){  // scelta difficolta
                if (nicknameSelect){  // se è già selezionato
                    nicknameSelect = false;  // deseleziona e il pointer torna alla posizione iniziale
                }
                else{
                    nicknameSelect = true;  // altrimenti permette di selezionare come muoversi
                }
            }else if(pointerIndex==1){  // scelta movimento
                if (avatarSelect){  // se è già selezionato
                    avatarSelect = false;  // deseleziona e il pointer torna alla posizione iniziale
                }
                else{
                    avatarSelect = true;  // altrimenti permette di selezionare come muoversi
                } 
            }else if(pointerIndex==2){  // back
                //disegna transizione 
                // gp.player.movementBehaviour = avatarChoice[selectedAvatarIndex];
                gp.currentPanel = new StartMenu(gp);
            }
            keyH.pausePressed = false; 
        }
    }
}