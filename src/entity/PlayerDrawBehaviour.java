package entity;


public class PlayerDrawBehaviour implements EntityDrawBehaviour{
    // classe che implementa l'interfaccia EntityDrawBehaviour, che permette di disegnare il player
    public void draw(Entity entity){
        entity.image = null;
        
        if(!entity.died){
            
            entity.invincibleCheck();
            switch(entity.direction){  // in base alla direzione, la variabile image prende il valore dell'immagine inserita
                case "up":
                    entity.image = entity.imageList[0].get(entity.spriteNum);
                break;
                case "down":
                    entity.image = entity.imageList[1].get(entity.spriteNum);
                break;
                case "left":
                    entity.image = entity.imageList[2].get(entity.spriteNum);
                break;
                case "right":
                    entity.image = entity.imageList[3].get(entity.spriteNum);
                break;
            }
        }else{  // se il player è morto
            if(!entity.checkDeathFall){  // se ancora non ha toccato terra allora salta
                entity.image = entity.deathImage.get(0);
                if(entity.checkDeathJump){  // dopo aver toccato 2 blocchi di altezza
                    entity.imageP.y += 8;  // scende
                }
                else{
                    entity.imageP.y -= 8;  // altrimenti sale
                }
                if(entity.imageP.y == entity.startDeathY-entity.gp.tileSize-entity.gp.tileSize/2){  // se supera i 2 blocchi
                    entity.checkDeathJump = true;
                }
                if(entity.imageP.y == entity.startDeathY){
                    entity.checkDeathFall = true;
                    entity.spriteNum = 1;
                }
            }else{  // altrimenti se tocca terra er poro chicco sta a stira
                if(entity.spriteDeathNum == 5){
                    entity.extinguished = true;
                }
        
    
                entity.spriteCounter++;
                if(entity.spriteCounter > 10){  // ogni 15/60 volte al secondo 
                    if(entity.spriteNum == 6){
                        entity.spriteNum = 5;
                        entity.spriteDeathNum++;
                    }else
                        entity.spriteNum++;
                    
                    entity.spriteCounter = 0;  // e resetta il counter
                }
                entity.image = entity.deathImage.get(entity.spriteNum);
            }
        }
        entity.g2.drawImage(entity.image, entity.imageP.x, entity.imageP.y, entity.gp.player.hittableWidth, entity.gp.player.hittableHeight, null);  // disegna lo sprite del personaggio (image) nella posizione x,y di grandezza tileSize

        entity.notifyObservers();
    }
}