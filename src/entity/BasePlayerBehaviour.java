package entity;


public class BasePlayerBehaviour implements EntityMovementBehaviour{
    
    public void update(Entity entity){
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
            if(!entity.collisionOn){ // se si puo muovere
                switch(entity.direction){
                    case "up": 
                        entity.imageP.y -= entity.speed;
                        entity.hitbox.y -= entity.speed;
                        break;  // la posizione Y diminuisce della velocita del player
                    case "down": 
                        entity.imageP.y += entity.speed;
                        entity.hitbox.y += entity.speed;
                        break;
                    case "left": 
                        entity.imageP.x -= entity.speed; 
                        entity.hitbox.x -= entity.speed;
                        break;
                    case "right": 
                        entity.imageP.x += entity.speed; 
                        entity.hitbox.x += entity.speed;
                        break;
                }
            }

            // hitbox.x = x + hitboxX;
            // hitbox.y = y + hitboxY;
            // controlliamo cosa fare con l'oggetto

            entity.spriteCounter++;
            if(entity.spriteCounter > 15){  // ogni 15/60 volte al secondo 
                entity.spriteNum++;
                if(entity.spriteNum == entity.maxSpriteNum)
                    entity.spriteNum = 0;
                
                entity.spriteCounter = 0;  // e resetta il counter
                // System.out.println(x+" "+y);  // da eliminare
                //System.out.println(getPlayerTileX()+ " "+getPlayerTileY());
            }
        }
        if(entity.keyH.bombPressed){ // se preme il tasto P (bomba)
            // BombHandler bomb = new BombHandler(getPlayerCenterCol()*gp.tileSize+(gp.tileSize/2), getPlayerCenterRow()*gp.tileSize+(24 * gp.scale), firePower, g2, gp.tileSize);
            entity.gp.bombH.createBomb(entity.getTileX(), entity.getTileY(), entity.getTileNumRow(), entity.getTileNumCol(), entity.firePower);
            // gp.cChecker.checkBomb(this);
            // BombHandler bomb = new BombHandler(x, y, firePower, g2, gp.tileSize);
            // gp.bombs.add(bomb);
            // gp.obj.add(bomb);
        }
    }

}
