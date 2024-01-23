package entity;


public class BasePlayerBehaviour implements EntityMovementBehavior{
    
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
                if(!entity.spriteEnd){  // se sta iterando in ordine gli sprite
                    entity.spriteNum++;  // aumenta l'index degli sprite
                    if(entity.spriteNum == entity.maxSpriteNum-1)  // se arriva al numero max di sprite
                        entity.spriteEnd = true;  // ha finito la lista degli sprite e inizia la stampa in reverse degli sprite
                }else{  // itera in reverse gli sprite
                    entity.spriteNum--;  // diminuisce l'index degli sprite
                    if(entity.spriteNum == 0)  // se torna a 0
                        entity.spriteEnd = false;  // ricomincia l'ordine normale degli sprite
                }
                
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
