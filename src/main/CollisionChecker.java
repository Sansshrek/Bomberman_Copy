package main;

import java.awt.Rectangle;

import entity.Entity;
import entity.Player;
import objects.Bomb;

import java.awt.Color;
import java.awt.Graphics2D;


public class CollisionChecker {
    GamePanel gp;
    int boundaryX, boundaryY, boundaryWidth, boundaryHeight;
    
    public CollisionChecker(GamePanel gp){
        this.gp = gp;
        boundaryX = 0;
        boundaryY = 0;
        boundaryWidth = 13*gp.tileSize;
        boundaryHeight = 11*gp.tileSize;
    }

    public void checkTile(Entity entity){
        int entityLeftWorldX = entity.hitbox.x - (24 * gp.scale);  // Coordinata x dove parte la hitbox
        int entityRightWorldX = entity.hitbox.x + entity.hitbox.width - (24 * gp.scale);  // cordinata x dove arriva la hitbox
        int entityTopWorldY = entity.hitbox.y - (gp.hudHeight + (8 * gp.scale));  // coordinata y dove parte la hitbox
        int entityBottomWorldY = entity.hitbox.y + entity.hitbox.height - (gp.hudHeight + (8 * gp.scale));  // coordinata y dove arriva la hitbox

        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;
        int entityCenterX = ((entityLeftWorldX + entityRightWorldX) / 2 ) / gp.tileSize;
        int entityCenterY = ((entityTopWorldY + entityBottomWorldY) / 2 ) / gp.tileSize;
        
        int tileNum1, tileNum2, tileCenter;

        switch(entity.direction){
            case "up":
                entityTopRow = (int)(entityTopWorldY - entity.speed) / gp.tileSize;  // calcoliamo dove si trovera il player quando si va su
                if(entityTopRow >= 0 && entityTopRow < 17){
                    tileNum1 = gp.tileM.blockTileNum[entityLeftCol][entityTopRow];  // controlliamo l'angolo alto a destra e alto a sinistra della hitbox sul terreno
                    tileNum2 = gp.tileM.blockTileNum[entityRightCol][entityTopRow];
                    tileCenter = gp.tileM.blockTileNum[entityCenterX][entityTopRow];  // controlliamo il centro in alto

                    if(gp.tileM.tile[tileNum1].collision && gp.tileM.tile[tileNum2].collision || (entityTopWorldY - entity.speed) < boundaryY){  // sta colpendo un blocco o esco dal boundary
                        entity.collisionOn = true;

                    }if(gp.tileM.tile[tileNum1].collision && !gp.tileM.tile[tileNum2].collision){  // se la spalla sinistra urta un blocco ma la destra no
                        if(!gp.tileM.tile[tileCenter].collision){  // se il centro in alto non urta un blocco
                            entity.x += entity.speed;  // lo sposta un po a destra
                            entity.hitbox.x += entity.speed;
                        }else{
                            entity.collisionOn = true;  // altrimenti non passa
                        }
                    }else if(!gp.tileM.tile[tileNum1].collision && gp.tileM.tile[tileNum2].collision){  // se la spalla sinistra urta un blocco ma la destra no
                        if(!gp.tileM.tile[tileCenter].collision){  // se il centro in alto non urta un blocco
                            entity.x -= entity.speed;  // lo sposta un po a sinistra
                            entity.hitbox.x -= entity.speed;
                        }else{
                            entity.collisionOn = true;
                        }
                    }
                }
                else{
                    entity.collisionOn = true;
                }
            break;
            case "down":
                entityBottomRow = (int)(entityBottomWorldY + entity.speed) / gp.tileSize;  // calcoliamo dove si trovera il player quando si va su
                if(entityBottomRow >= 0 && entityBottomRow < 17){
                    tileNum1 = gp.tileM.blockTileNum[entityLeftCol][entityBottomRow];  // controlliamo l'angolo basso a destra e basso a sinistra della hitbox sul terreno
                    tileNum2 = gp.tileM.blockTileNum[entityRightCol][entityBottomRow];
                    tileCenter = gp.tileM.blockTileNum[entityCenterX][entityBottomRow];  // controlliamo il centro in basso

                    if(gp.tileM.tile[tileNum1].collision && gp.tileM.tile[tileNum2].collision || (entityBottomWorldY + entity.speed) > boundaryY+boundaryHeight){  // sta colpendo un blocco o esco dal boundary 
                        entity.collisionOn = true;

                    }if(gp.tileM.tile[tileNum1].collision && !gp.tileM.tile[tileNum2].collision){  // se in basso a sinistra urta un blocco ma in basso a destra no
                        if(!gp.tileM.tile[tileCenter].collision){  // se il centro in basso non urta un blocco
                            entity.x += entity.speed;  // lo sposta un po a destra
                            entity.hitbox.x += entity.speed;
                        }else{
                            entity.collisionOn = true;  // altrimenti non passa
                        }
                    }else if(!gp.tileM.tile[tileNum1].collision && gp.tileM.tile[tileNum2].collision){  // se in basso a sinistra urta un blocco ma in basso a destra no
                        if(!gp.tileM.tile[tileCenter].collision){  // se il centro in basso non urta un blocco
                            entity.x -= entity.speed;
                            entity.hitbox.x -= entity.speed;
                        }else{
                            entity.collisionOn = true;
                        }
                    }
                }
                else{
                    entity.collisionOn = true;
                }
            break;
            case "left":
                entityLeftCol = (int)(entityLeftWorldX - entity.speed) / gp.tileSize;  // calcoliamo dove si trovera il player quando si va su
                if(entityLeftCol >= 0 && entityLeftCol < 14){
                    tileNum1 = gp.tileM.blockTileNum[entityLeftCol][entityTopRow];  // controlliamo l'angolo alto a sinistra e basso a sinistra della hitbox sul terreno
                    tileNum2 = gp.tileM.blockTileNum[entityLeftCol][entityBottomRow];
                    tileCenter = gp.tileM.blockTileNum[entityLeftCol][entityCenterY];  // controlliamo il centro a sinistra

                    if(gp.tileM.tile[tileNum1].collision && gp.tileM.tile[tileNum2].collision || (entityLeftWorldX - entity.speed) < boundaryX){  // sta colpendo un blocco o esco dal boundary
                        entity.collisionOn = true;

                    }if(gp.tileM.tile[tileNum1].collision && !gp.tileM.tile[tileNum2].collision){  // se in alto a sinistra urta un blocco ma in basso a sinistra no
                        if(!gp.tileM.tile[tileCenter].collision){  // se il centro a sinistra non urta un blocco
                            entity.y += entity.speed;  // lo sposta un po sotto
                            entity.hitbox.y += entity.speed;
                        }else{
                            entity.collisionOn = true;  // altrimenti non passa
                        }
                    }else if(!gp.tileM.tile[tileNum1].collision && gp.tileM.tile[tileNum2].collision){  // se in basso sinistra urta un blocco ma in alto a destra no
                        if(!gp.tileM.tile[tileCenter].collision){  // se il centro a sinistra non urta un blocco
                            entity.y -= entity.speed;  // lo sposta un po sopra
                            entity.hitbox.y -= entity.speed;
                        }else{
                            entity.collisionOn = true;
                        }
                    }
                }
                else{
                    entity.collisionOn = true;
                }
            break;
            case "right":
                entityRightCol = (int)(entityRightWorldX + entity.speed) / gp.tileSize;  // calcoliamo dove si trovera il player quando si va su
                if(entityLeftCol >= 0 && entityLeftCol < 14){
                    tileNum1 = gp.tileM.blockTileNum[entityRightCol][entityTopRow];  // controlliamo l'angolo alto a destra e basso a destra della hitbox sul terreno
                    tileNum2 = gp.tileM.blockTileNum[entityRightCol][entityBottomRow];
                    tileCenter = gp.tileM.blockTileNum[entityRightCol][entityCenterY];  // controlliamo il centro a destra

                    if(gp.tileM.tile[tileNum1].collision && gp.tileM.tile[tileNum2].collision || (entityRightWorldX + entity.speed) > boundaryX+boundaryWidth){  // sta colpendo un blocco o esco dal boundary
                        entity.collisionOn = true;

                    }if(gp.tileM.tile[tileNum1].collision && !gp.tileM.tile[tileNum2].collision){  // se in alto a destra urta un blocco ma in basso a destra no
                        if(!gp.tileM.tile[tileCenter].collision){  // se il centro in alto non urta un blocco
                            entity.y += entity.speed;  // lo sposta un po sotto
                            entity.hitbox.y += entity.speed;
                        }else{
                            entity.collisionOn = true;  // altrimenti non passa
                        }
                    }else if(!gp.tileM.tile[tileNum1].collision && gp.tileM.tile[tileNum2].collision){  // se in basso a destra urta un blocco ma alto a dstra no
                        if(!gp.tileM.tile[tileCenter].collision){  // se il centro in alto non urta un blocco
                            entity.y -= entity.speed;  // lo sposta un po sopra
                            entity.hitbox.y -= entity.speed;
                        }else{
                            entity.collisionOn = true;
                        }
                    }
                }
                else{
                    entity.collisionOn = true;
                }
            break;
        }
    }

    public void checkTile2(Entity entity){
        
    }
    
    public void checkPlayerCollision(Entity enemy, Player player) {
        // Aggiorna la posizione dell'hitbox del nemico
        enemy.hitbox.x = enemy.x + enemy.hitbox.x;
        enemy.hitbox.y = enemy.y + enemy.hitbox.y;

        // Aggiorna la posizione dell'hitbox del giocatore
        player.hitbox.x = player.x + player.hitbox.x;
        player.hitbox.y = player.y + player.hitbox.y;

        // Controlla se l'hitbox del nemico interseca l'hitbox del giocatore
        if (enemy.hitbox.intersects(player.hitbox)) {
            // Se il nemico sta andando a destra e il giocatore è alla sua destra, ferma il nemico
            if (enemy.x < player.x) {
                enemy.x = player.x - enemy.hitbox.width;
            }
            // Se il nemico sta andando a sinistra e il giocatore è alla sua sinistra, ferma il nemico
            else if (enemy.x > player.x) {
                enemy.x = player.x + player.hitbox.width;
            }
            // Se il nemico sta andando in alto e il giocatore è sopra di lui, ferma il nemico
            if (enemy.y < player.y) {
                enemy.y = player.y - enemy.hitbox.height;
            }
            // Se il nemico sta andando in basso e il giocatore è sotto di lui, ferma il nemico
            else if (enemy.y > player.y) {
                enemy.y = player.y + player.hitbox.height;
            }
        }
    }

    public int checkObj(Entity entity, boolean player, Graphics2D g2){  // controlliamo se il player si scontra con un oggetto e ritorna l'indice dell'oggetto
        int index = 999;  // default index
        for(int i=0; i<gp.obj.size(); i++){
            if(gp.obj.get(i) != null){  // se non è null
                // prendo la posizione dell'hitbox del player
                // entity.hitbox.x = entity.x + entity.hitbox.x;
                // entity.hitbox.y = entity.y + entity.hitbox.y;
                Rectangle entityHitboxCheck = new Rectangle(entity.hitbox.x, entity.hitbox.y, entity.hitboxWidth, entity.hitboxHeight);
                g2.setColor(Color.RED);
                g2.drawRect(entity.x, entity.y, entity.hitboxWidth, entity.hitboxHeight);
                // prendo la posizione dell'hitbox dell'oggetto
                // gp.obj.get(i).hitbox.x = gp.obj.get(i).x + gp.obj.get(i).hitbox.x;
                // gp.obj.get(i).hitbox.y = gp.obj.get(i).y + gp.obj.get(i).hitbox.y;
                Rectangle objHitboxCheck = new Rectangle(gp.obj.get(i).x, gp.obj.get(i).y, gp.tileSize, gp.tileSize);

                switch(entity.direction){
                    case "up":
                        entityHitboxCheck.y -= entity.speed;
                        if(entityHitboxCheck.intersects(objHitboxCheck)){  // controlla se l'hitbox del player interseca l'hitbox dell'oggetto
                            if(gp.obj.get(i).collision){  // se puo essere scontrato setta la collisione del player a true
                                entity.collisionOn = true;
                            }
                            if(player){ // se è il player a toccare l'oggetto allora ritorna l'indice
                                index = i;
                            }
                        }
                    break;
                    case "down":
                        entityHitboxCheck.y += entity.speed;
                        if(entityHitboxCheck.intersects(objHitboxCheck)){
                            if(gp.obj.get(i).collision){  // se puo essere scontrato setta la collisione del player a true
                                entity.collisionOn = true;
                            }
                            if(player){ // se è il player a toccare l'oggetto allora ritorna l'indice
                                index = i;
                            }
                        }
                    break;
                    case "left":
                        entityHitboxCheck.x -= entity.speed;
                        if(entityHitboxCheck.intersects(objHitboxCheck)){
                            if(gp.obj.get(i).collision){  // se puo essere scontrato setta la collisione del player a true
                                entity.collisionOn = true;
                            }
                            if(player){ // se è il player a toccare l'oggetto allora ritorna l'indice
                                index = i;
                            }
                        }
                    break;
                    case "right":
                        entityHitboxCheck.x += entity.speed;
                        if(entityHitboxCheck.intersects(objHitboxCheck)){
                            if(gp.obj.get(i).collision){  // se puo essere scontrato setta la collisione del player a true
                                entity.collisionOn = true;
                            }
                            if(player){ // se è il player a toccare l'oggetto allora ritorna l'indice
                                index = i;
                            }
                        }
                    break;
                }
                // entity.hitbox.x = entity.hitboxDefaultX;
                // entity.hitbox.y = entity.hitboxDefaultY;
                entityHitboxCheck = null; // viene eliminato per lasciare spazio alla memoria
                // gp.obj.get(i).hitbox.x = gp.obj.get(i).hitboxDefaultX;
                // gp.obj.get(i).hitbox.y = gp.obj.get(i).hitboxDefaultY;
            }
        }
        return index;
    }

    public void getTileCenterX(){

    }

    public void getTileCenterY(){

    }

    public void checPlayerNearestTile(){
    }
    
    public void checkBomb(Player player){
        for(Bomb bomb: gp.bombH.bombs){  // iteriamo le bombe presenti
            if(!bomb.exploded){  // se la bomba non è esplosa
                int bombLeftBorder = bomb.x;
                int bombRightBorder = bomb.x + bomb.hitbox.width;
                int bombTopBorder = bomb.y;
                int bombBottomBorder = bomb.y + bomb.hitbox.height;
                //Controlla le 4 posizioni adiacenti alla bomba a croce
                int bombPosSx = (((bomb.x - gp.tileSize - (gp.tileSize+gp.tileSize/2)))/gp.tileSize)  * 10 + ((bomb.y - (2*gp.tileSize + (gp.tileSize/2)))/gp.tileSize );
                if(((bomb.x - gp.tileSize - (gp.tileSize+gp.tileSize/2)))/gp.tileSize>=0 && bombPosSx >= 0 && bombPosSx < 140) {  // check se la posizione è nell'array

                    // System.out.print("SX ");  // da eliminare
                    if(gp.tileM.isHouse(bomb.x-gp.tileSize, bomb.y)){  // se in quella pos. del fuoco c'è una casa 
                        if(gp.obj.get(bombPosSx) == null ){   // check se quella posizione è un blocco esistente
                            int playerDistanceSx = Math.abs(player.getPlayerCenterX() - bombLeftBorder);
                            System.out.println("Player Distance SX: "+playerDistanceSx);
                        }
                    }
                    
                }
                int bombPosDx = (((bomb.x + gp.tileSize - (gp.tileSize+gp.tileSize/2)))/gp.tileSize)  * 10 + ((bomb.y - (2*gp.tileSize + (gp.tileSize/2)))/gp.tileSize );
                if(((bomb.x + gp.tileSize - (gp.tileSize+gp.tileSize/2)))/gp.tileSize>=0 && bombPosDx >= 0 && bombPosDx < 140) {  // check se la posizione è nell'array

                    // System.out.print("DX ");  // da eliminare
                    if(!gp.tileM.isHouse(bomb.x+gp.tileSize, bomb.y)){  // se in quella pos. c'è una casa 
                        if(gp.obj.get(bombPosDx) == null ){   // check se quella posizione è un blocco esistente
                            int playerDistanceDx = Math.abs(player.getPlayerCenterX() - bombRightBorder);
                            System.out.println("Player Distance DX: "+playerDistanceDx);
                        }
                    }
                }
                int bombPosUp = ((bomb.x-(gp.tileSize+gp.tileSize/2))/gp.tileSize ) * 10 + ((bomb.y - gp.tileSize- (2*gp.tileSize + (gp.tileSize/2)))/gp.tileSize );
                if(((bomb.y - gp.tileSize- (2*gp.tileSize + (gp.tileSize/2)))/gp.tileSize )>=0 && bombPosUp >= 0 && bombPosUp < 140) {  // check se la posizione è nell'array

                    // System.out.print("UP ");  // da eliminare
                    if(!gp.tileM.isHouse(bomb.x, bomb.y-gp.tileSize)){  // se in quella pos. del c'è una casa 
                        if(gp.obj.get(bombPosUp) == null ){   // check se quella posizione è un blocco esistente
                            int playerDistanceUp = Math.abs(player.getPlayerCenterY() - bombTopBorder);
                            System.out.println("Player Distance UP: "+playerDistanceUp);
                        }
                    }
                    
                }
                int bombPosDown = ((bomb.x-(gp.tileSize+gp.tileSize/2))/gp.tileSize ) * 10 + ((bomb.y + gp.tileSize- (2*gp.tileSize + (gp.tileSize/2)))/gp.tileSize );
                System.out.println(((bomb.y + gp.tileSize- (2*gp.tileSize + (gp.tileSize/2)))/gp.tileSize ));
                if(((bomb.y + gp.tileSize- (2*gp.tileSize + (gp.tileSize/2)))/gp.tileSize )>=0 && bombPosDown >= 0 && bombPosDown < 140) {  // check se la posizione è nell'array

                    // System.out.print("DOWN ");  // da eliminare
                    if(!gp.tileM.isHouse(bomb.x, bomb.y+gp.tileSize)){  // se in quella pos. del fuoco c'è una casa 
                        if(gp.obj.get(bombPosDown) == null ){   // check se quella posizione è un blocco esistente
                            int playerDistanceDown = Math.abs(player.getPlayerCenterY() - bombBottomBorder);
                            System.out.println("Player Distance DOWN: "+playerDistanceDown);
                        }
                    }
                    
                }
                bomb.collision = true; // Imposta un flag di collisione sull'entità
                System.out.println("BOMBA");
            }
        }
    }
}
