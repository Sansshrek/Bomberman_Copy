package entity;


public class BasePlayerBehaviour implements EntityMovementBehaviour{
    
    public void updateMovement(Entity entity){
        entity.collisionOn = false;
        if(entity.keyH.upPressed || entity.keyH.downPressed || entity.keyH.leftPressed || entity.keyH.rightPressed){

            if(entity.keyH.upPressed){ // se clicchi sopra
                entity.direction = "up";
            }
            if(entity.keyH.downPressed){
                entity.direction = "down";
            }
            if(entity.keyH.leftPressed){
                entity.direction = "left";
            }
            if(entity.keyH.rightPressed){
                entity.direction = "right";
            }
            entity.gp.cChecker.checkTile(entity);  // se in quella posizione si puo muovere allora collisionOn resta false
            entity.gp.cChecker.checkObj(entity);
            
            entity.changeSpriteDirection();

            // hitbox.x = x + hitboxX;
            // hitbox.y = y + hitboxY;
            // controlliamo cosa fare con l'oggetto

            entity.spriteCounter++;
            if(entity.spriteCounter > 15){  // ogni 15/60 volte al secondo 
                if(entity.endAnimation)  // se è finita l'animazione
                    entity.spriteNum--;  // ristampa gli sprite in reverse
                else
                    entity.spriteNum++;  // altrimenti li stampa in modo normale

                if(entity.spriteNum == entity.maxSpriteNum-1){
                    entity.endAnimation = true;
                }
                if(entity.spriteNum <= 0){
                    entity.endAnimation = false;
                    entity.spriteNum = 0;
                }
                
                entity.spriteCounter = 0;  // e resetta il counter
            }
        }
        
    }

}
