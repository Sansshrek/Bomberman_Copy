package main;

import entity.Entity;
import entity.Player;

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
        int entityLeftWorldX = entity.x + entity.hitbox.x - (24 * gp.scale);  // Coordinata x dove parte la hitbox
        int entityRightWorldX = entity.x + entity.hitbox.x + entity.hitbox.width - (24 * gp.scale);  // cordinata x dove arriva la hitbox
        int entityTopWorldY = entity.y + entity.hitbox.y - (gp.hudHeight + (8 * gp.scale));  // coordinata y dove parte la hitbox
        int entityBottomWorldY = entity.y + entity.hitbox.y + entity.hitbox.height - (gp.hudHeight + (8 * gp.scale));  // coordinata y dove arriva la hitbox

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
                tileNum1 = gp.tileM.groundTileNum[entityLeftCol][entityTopRow];  // controlliamo l'angolo alto a destra e alto a sinistra della hitbox sul terreno
                tileNum2 = gp.tileM.groundTileNum[entityRightCol][entityTopRow];
                tileCenter = gp.tileM.groundTileNum[entityCenterX][entityTopRow];  // controlliamo il centro in alto

                if(gp.tileM.tile[tileNum1].collision && gp.tileM.tile[tileNum2].collision || (entityTopWorldY - entity.speed) < boundaryY){  // sta colpendo un blocco o esco dal boundary
                    entity.collisionOn = true;

                }if(gp.tileM.tile[tileNum1].collision && !gp.tileM.tile[tileNum2].collision){  // se la spalla sinistra urta un blocco ma la destra no
                    if(!gp.tileM.tile[tileCenter].collision){  // se il centro in alto non urta un blocco
                        entity.x += entity.speed;  // lo sposta un po a destra
                    }else{
                        entity.collisionOn = true;  // altrimenti non passa
                    }
                }else if(!gp.tileM.tile[tileNum1].collision && gp.tileM.tile[tileNum2].collision){  // se la spalla sinistra urta un blocco ma la destra no
                    if(!gp.tileM.tile[tileCenter].collision){  // se il centro in alto non urta un blocco
                        entity.x -= entity.speed;  // lo sposta un po a sinistra
                    }else{
                        entity.collisionOn = true;
                    }
                }
            break;
            case "down":
                entityBottomRow = (int)(entityBottomWorldY + entity.speed) / gp.tileSize;  // calcoliamo dove si trovera il player quando si va su
                tileNum1 = gp.tileM.groundTileNum[entityLeftCol][entityBottomRow];  // controlliamo l'angolo basso a destra e basso a sinistra della hitbox sul terreno
                tileNum2 = gp.tileM.groundTileNum[entityRightCol][entityBottomRow];
                tileCenter = gp.tileM.groundTileNum[entityCenterX][entityBottomRow];  // controlliamo il centro in basso

                if(gp.tileM.tile[tileNum1].collision && gp.tileM.tile[tileNum2].collision || (entityBottomWorldY + entity.speed) > boundaryY+boundaryHeight){  // sta colpendo un blocco o esco dal boundary 
                    entity.collisionOn = true;

                }if(gp.tileM.tile[tileNum1].collision && !gp.tileM.tile[tileNum2].collision){  // se in basso a sinistra urta un blocco ma in basso a destra no
                    if(!gp.tileM.tile[tileCenter].collision){  // se il centro in basso non urta un blocco
                        entity.x += entity.speed;  // lo sposta un po a destra
                    }else{
                        entity.collisionOn = true;  // altrimenti non passa
                    }
                }else if(!gp.tileM.tile[tileNum1].collision && gp.tileM.tile[tileNum2].collision){  // se in basso a sinistra urta un blocco ma in basso a destra no
                    if(!gp.tileM.tile[tileCenter].collision){  // se il centro in basso non urta un blocco
                        entity.x -= entity.speed;
                    }else{
                        entity.collisionOn = true;
                    }
                }
            break;
            case "left":
                entityLeftCol = (int)(entityLeftWorldX - entity.speed) / gp.tileSize;  // calcoliamo dove si trovera il player quando si va su
                tileNum1 = gp.tileM.groundTileNum[entityLeftCol][entityTopRow];  // controlliamo l'angolo alto a sinistra e basso a sinistra della hitbox sul terreno
                tileNum2 = gp.tileM.groundTileNum[entityLeftCol][entityBottomRow];
                tileCenter = gp.tileM.groundTileNum[entityLeftCol][entityCenterY];  // controlliamo il centro a sinistra

                if(gp.tileM.tile[tileNum1].collision && gp.tileM.tile[tileNum2].collision || (entityLeftWorldX - entity.speed) < boundaryX){  // sta colpendo un blocco o esco dal boundary
                    entity.collisionOn = true;

                }if(gp.tileM.tile[tileNum1].collision && !gp.tileM.tile[tileNum2].collision){  // se in alto a sinistra urta un blocco ma in basso a sinistra no
                    if(!gp.tileM.tile[tileCenter].collision){  // se il centro a sinistra non urta un blocco
                        entity.y += entity.speed;  // lo sposta un po sotto
                    }else{
                        entity.collisionOn = true;  // altrimenti non passa
                    }
                }else if(!gp.tileM.tile[tileNum1].collision && gp.tileM.tile[tileNum2].collision){  // se in basso sinistra urta un blocco ma in alto a destra no
                    if(!gp.tileM.tile[tileCenter].collision){  // se il centro a sinistra non urta un blocco
                        entity.y -= entity.speed;  // lo sposta un po sopra
                    }else{
                        entity.collisionOn = true;
                    }
                }
            break;
            case "right":
                entityRightCol = (int)(entityRightWorldX + entity.speed) / gp.tileSize;  // calcoliamo dove si trovera il player quando si va su
                tileNum1 = gp.tileM.groundTileNum[entityRightCol][entityTopRow];  // controlliamo l'angolo alto a destra e basso a destra della hitbox sul terreno
                tileNum2 = gp.tileM.groundTileNum[entityRightCol][entityBottomRow];
                tileCenter = gp.tileM.groundTileNum[entityRightCol][entityCenterY];  // controlliamo il centro a destra

                if(gp.tileM.tile[tileNum1].collision && gp.tileM.tile[tileNum2].collision || (entityRightWorldX + entity.speed) > boundaryX+boundaryWidth){  // sta colpendo un blocco o esco dal boundary
                    entity.collisionOn = true;

                }if(gp.tileM.tile[tileNum1].collision && !gp.tileM.tile[tileNum2].collision){  // se in alto a destra urta un blocco ma in basso a destra no
                    if(!gp.tileM.tile[tileCenter].collision){  // se il centro in alto non urta un blocco
                        entity.y += entity.speed;  // lo sposta un po sotto
                    }else{
                        entity.collisionOn = true;  // altrimenti non passa
                    }
                }else if(!gp.tileM.tile[tileNum1].collision && gp.tileM.tile[tileNum2].collision){  // se in basso a destra urta un blocco ma alto a dstra no
                    if(!gp.tileM.tile[tileCenter].collision){  // se il centro in alto non urta un blocco
                        entity.y -= entity.speed;  // lo sposta un po sopra
                    }else{
                        entity.collisionOn = true;
                    }
                }
            break;
        }
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

    public int checkObj(Entity entity, boolean player){  // controlliamo se il player si scontra con un oggetto e ritorna l'indice dell'oggetto
        int index = 999;  // default index
        for(int i=0; i<gp.obj.size(); i++){
            if(gp.obj.get(i) != null){  // se non è null
                // prendo la posizione dell'hitbox del player
                entity.hitbox.x = entity.x + entity.hitbox.x;
                entity.hitbox.y = entity.y + entity.hitbox.y;
                // prendo la posizione dell'hitbox dell'oggetto
                gp.obj.get(i).hitbox.x = gp.obj.get(i).x + gp.obj.get(i).hitbox.x;
                gp.obj.get(i).hitbox.y = gp.obj.get(i).y + gp.obj.get(i).hitbox.y;

                switch(entity.direction){
                    case "up":
                        entity.hitbox.y -= entity.speed;
                        if(entity.hitbox.intersects(gp.obj.get(i).hitbox)){  // controlla se l'hitbox del player interseca l'hitbox dell'oggetto
                            if(gp.obj.get(i).collision){  // se puo essere scontrato setta la collisione del player a true
                                entity.collisionOn = true;
                            }
                            if(player){ // se è il player a toccare l'oggetto allora ritorna l'indice
                                index = i;
                            }
                        }
                    break;
                    case "down":
                        entity.hitbox.y += entity.speed;
                        if(entity.hitbox.intersects(gp.obj.get(i).hitbox)){
                            if(gp.obj.get(i).collision){  // se puo essere scontrato setta la collisione del player a true
                                entity.collisionOn = true;
                            }
                            if(player){ // se è il player a toccare l'oggetto allora ritorna l'indice
                                index = i;
                            }
                        }
                        
                    break;
                    case "left":
                        entity.hitbox.x -= entity.speed;
                        if(entity.hitbox.intersects(gp.obj.get(i).hitbox)){
                            if(gp.obj.get(i).collision){  // se puo essere scontrato setta la collisione del player a true
                                entity.collisionOn = true;
                            }
                            if(player){ // se è il player a toccare l'oggetto allora ritorna l'indice
                                index = i;
                            }
                        }
                    break;
                    case "right":
                        entity.hitbox.x += entity.speed;
                        if(entity.hitbox.intersects(gp.obj.get(i).hitbox)){
                            if(gp.obj.get(i).collision){  // se puo essere scontrato setta la collisione del player a true
                                entity.collisionOn = true;
                            }
                            if(player){ // se è il player a toccare l'oggetto allora ritorna l'indice
                                index = i;
                            }
                        }
                    break;
                }
                entity.hitbox.x = entity.hitboxDefaultX;
                entity.hitbox.y = entity.hitboxDefaultY;
                gp.obj.get(i).hitbox.x = gp.obj.get(i).hitboxDefaultX;
                gp.obj.get(i).hitbox.y = gp.obj.get(i).hitboxDefaultY;
            }
        }
        return index;
    }
}
