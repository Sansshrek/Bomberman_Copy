package entity;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class ClownDrawBehaviour implements EntityDrawBehaviour{

    BufferedImage[] attackImage = new BufferedImage[4];
    BufferedImage deathImage;
    
    public ClownDrawBehaviour(){
        // carico le sprite dell'attacco
        try {
            for(int sprite=1; sprite<=4; sprite++){  // per quante sprite ci stanno in una direzione
                attackImage[sprite-1] = ImageIO.read(getClass().getResourceAsStream("../res/enemies/clown/attack"+String.valueOf(sprite)+".png"));
            }
            deathImage = ImageIO.read(getClass().getResourceAsStream("../res/enemies/clown/death.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // disegna l'enemy
    public void draw(Entity entity){
        entity.image = null;
        if(!entity.died){  // se ancora non è stato colpito dalla bomba allora disegna l'enemy normale
            entity.invincibleCheck();

            if(entity.startAttack){  // se inizia l'attacco allora parte l'animazione dell'attacco
                entity.spriteCounter++;
                if(entity.spriteCounter > 15){  // ogni 15/60 volte al secondo 
                    entity.spriteNum++;
                    if(entity.spriteNum == 4){  // quando finisce l'animazione dell'attacco
                        entity.spriteNum = 0;  // resetta lo sprite dell'attacco
                        entity.startAttack = false;  // finisce l'attacco
                        

                    }
                    entity.spriteCounter = 0;  // e resetta il counter
                }
                entity.image = attackImage[entity.spriteNum];   // carica la prossima immagine della animazione
            }else{  // se ancora non ha iniziato l'attacco prendi lo sprite base di movimento
                entity.image = entity.imageList[0].get(0);  // lo sprite del movimento del knight è uno solo quindi non serve fare lo switch per le direzioni
            }
        }else{  // altrimenti se l'enemy è stato colpito dalla bomba allora disegna l'esplosione
            entity.spriteCounter++;
            if(entity.spriteCounter > 15 && entity.spriteNum < 9){  // se ancora non è finita l'animazione di morte
                entity.spriteDeathNum++;  // cambia gli sprite della morte
                entity.spriteCounter = 0;
                
            }
            if(entity.spriteDeathNum%2 == 0){  // se lo spriteDeathNum è pari
                entity.image = deathImage;  // mette lo sprite della morte
            }else{
                entity.image = entity.imageList[0].get(0);  // altrimenti quella di base
            }
            if(entity.spriteDeathNum == 9){  // se ha finito l'animazione di morte

                entity.extinguished = true;
            }
        }

        entity.g2.drawImage(entity.image, entity.imageP.x, entity.imageP.y, entity.imageWidth, entity.imageHeight, null);  // disegna lo sprite del personaggio (image) nella posizione x,y di grandezza tileSize

        entity.projectileHandler.drawProjectiles(entity.g2);  // disegna i proiettili
        
        entity.notifyObservers();
    }

    
}