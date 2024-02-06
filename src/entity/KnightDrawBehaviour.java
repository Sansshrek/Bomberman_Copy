package entity;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

public class KnightDrawBehaviour implements EntityDrawBehaviour{

    BufferedImage[] attackImage = new BufferedImage[4];
    public KnightDrawBehaviour(){
        // carico le sprite dell'attacco
        try {
            // attack1 = ImageIO.read(getClass().getResourceAsStream("../res/enemies/knight/attack1.png"));
            for(int sprite=1; sprite<=4; sprite++){  // per quante sprite ci stanno in una direzione
                attackImage[sprite-1] = ImageIO.read(getClass().getResourceAsStream("../res/enemies/knight/attack"+String.valueOf(sprite)+".png"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // disegna l'enemy
    public void draw(Entity entity){

        entity.image = null;
        // System.out.println(entity.startAttack);
        if(!entity.died){  // se ancora non è stato colpito dalla bomba allora disegna l'enemy normale
            entity.invincibleCheck();

            if(entity.startAttack){  // se inizia l'attacco allora parte l'animazione dell'attacco
                entity.spriteCounter++;
                if(entity.spriteCounter > 15){  // ogni 15/60 volte al secondo 
                    entity.spriteNum++;
                    if(entity.spriteNum == 4){  // quando finisce l'animazione dell'attacco
                        entity.spriteNum = 0;  // resetta lo sprite dell'attacco
                        entity.startAttack = false;  // disattiva il controllo dell'inizio attacco
                        entity.finishAttack = true;  // attiva il controllo della fine attacco
                    }
                    entity.spriteCounter = 0;  // e resetta il counter
                }
                entity.image = attackImage[entity.spriteNum];   // carica la prossima immagine della animazione
            }else{  // se ancora non ha iniziato l'attacco prendi lo sprite base di movimento
                entity.image = entity.imageList[0].get(0);  // lo sprite del movimento del knight è uno solo quindi non serve fare lo switch per le direzioni
            }
        }else{  // altrimenti se l'enemy è stato colpito dalla bomba allora disegna l'esplosione
            System.out.println("DEATH");
            entity.extinguished = true;  // per ora lo elimina
        }

        if(!entity.extinguished){
            // entity.g2.drawImage(entity.image, entity.imageP.x, entity.imageP.y, entity.width, entity.height, null);  // disegna lo sprite del personaggio (image) nella posizione x,y di grandezza tileSize
            // da eliminare
            entity.g2.setColor(Color.BLUE);
            entity.g2.draw(entity.hitbox);

            entity.g2.setColor(Color.GREEN);
            // entity.g2.drawRect(entity.getTileX(), entity.getTileY(), entity.tileSize, entity.tileSize);
        }
        entity.notifyObservers();
    }

    
}