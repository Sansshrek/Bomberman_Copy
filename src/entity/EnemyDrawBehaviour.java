package entity;

public class EnemyDrawBehaviour implements EntityDrawBehaviour{

    // disegna l'enemy
    public void draw(Entity entity){
        entity.image = null;
        if(!entity.died){  // se ancora non è stato colpito dalla bomba allora disegna l'enemy normale
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
        }else{  // altrimenti se l'enemy è stato colpito dalla bomba allora disegna l'esplosione
            entity.spriteCounter++;
            if(entity.spriteCounter > 10){
                entity.spriteDeathNum++;
                entity.spriteCounter = 0;
            }
            if(entity.spriteDeathNum == 7){  // se ha finito l'animazione di morte
                entity.extinguished = true;
            }else{
                entity.image = entity.deathImage.get(entity.spriteDeathNum);
            }
        }
        
        entity.g2.drawImage(entity.image, entity.imageP.x, entity.imageP.y, entity.imageWidth, entity.imageHeight, null);  // disegna lo sprite del personaggio (image) nella posizione x,y di grandezza tileSize
        
        entity.notifyObservers();
    }

    
}