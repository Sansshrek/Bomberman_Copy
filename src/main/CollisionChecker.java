package main;

import java.awt.Rectangle;

import entity.Entity;
import entity.Player;
import objects.Bomb;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;


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
        int tileCol = entity.getTileNumCol();
        int tileRow = entity.getTileNumRow();
        int hitboxCenterX = entity.getCenterX();  // prendiamo il centro del player
        int hitboxCenterY = entity.getCenterY();
        int hitboxWidthHalf = entity.hitboxWidth/2;  // i due valori di altezza e larghezza della hitbox dimezzati
        int hitboxHeightHalf = entity.hitboxHeight/2;
        Rectangle hitboxUpSx = new Rectangle(entity.hitbox.x, entity.hitbox.y, hitboxWidthHalf, hitboxHeightHalf);  // dividiamo in 4 sezioni la hitbox del player
        Rectangle hitboxUpDx = new Rectangle(hitboxCenterX, entity.hitbox.y, hitboxWidthHalf, hitboxHeightHalf);
        Rectangle hitboxDwSx = new Rectangle(entity.hitbox.x, hitboxCenterY, hitboxWidthHalf, hitboxHeightHalf);
        Rectangle hitboxDwDx = new Rectangle(hitboxCenterX, hitboxCenterY, hitboxWidthHalf, hitboxHeightHalf);
        Rectangle hitboxCheck1 = new Rectangle(0, 0, hitboxWidthHalf, hitboxHeightHalf);  // creiamo due hitbox temporanee che variano in base alla direzione(es. direction="down", usiamo hitboxDwSx e hitboxDwDx come base)
        Rectangle hitboxCheck2 = new Rectangle(0, 0, hitboxWidthHalf, hitboxHeightHalf);
        Rectangle wallCheck1 = null, wallCheck2 = null, wallCheckCx = null;  // impostiamo i valori di default
        switch(entity.direction){
            case "up":
                if((entity.hitbox.y - entity.speed) >= gp.gameBorderUpY){  // se non supera il bordo di gioco
                    if(tileRow > 0){  // nella prima riga non serve controllare se sopra c'è un blocco
                        hitboxCheck1.x = hitboxUpSx.x;  // imposta la hitbox delle due hitbox da controllare (in alto a sinistra/destra) nella pos in cui si deve spostare
                        hitboxCheck1.y = (int)(hitboxUpSx.y - entity.speed);
                        hitboxCheck2.x = hitboxUpDx.x;
                        hitboxCheck2.y = (int)(hitboxUpDx.y - entity.speed);
                        if(tileCol-1 >= 0)  // se la pos del blocco a sinistra non è fuori dalla mappa
                            wallCheck1 = gp.tileM.houseHitbox[tileRow-1][tileCol-1];  // carichiamo l'hitbox del blocco in alto a sinistra
                        if(tileCol+1 < 13)  // se la pos del blocco a destra non è fuori dalla mappa
                            wallCheck2 = gp.tileM.houseHitbox[tileRow-1][tileCol+1];  // carichiamo l'hitbox del blocco in alto a destra
                        wallCheckCx = gp.tileM.houseHitbox[tileRow-1][tileCol];
                    }
                }else
                    entity.collisionOn = true;
            break;
            case "down":
                if((entity.hitbox.y + entity.hitboxHeight + entity.speed) <= gp.gameBorderDownY){  // se non supera il bordo di gioco
                    if(tileRow < 10){  // nell'ultima riga non serve controllare se sopra c'è un blocco
                        hitboxCheck1.x = hitboxDwSx.x;  // imposta la hitbox delle due hitbox da controllare (in basso a sinistra/destra) nella pos in cui si deve spostare
                        hitboxCheck1.y = (int)(hitboxDwSx.y + entity.speed);
                        hitboxCheck2.x = hitboxDwDx.x;
                        hitboxCheck2.y = (int)(hitboxDwDx.y + entity.speed);
                        // prendiamo i 3 muri in alto all'entity (se per esempio siamo in tile (3,5) prende le hitbox dei muri in basso a sinistra(4,4), basso in centro (4,5) e basso a destra (4,6))
                        if(tileCol-1 >= 0)  // se la pos del blocco a sinistra non è fuori dalla mappa
                            wallCheck1 = gp.tileM.houseHitbox[tileRow+1][tileCol-1];  // carichiamo l'hitbox del blocco in basso a sinistra
                        if(tileCol+1 < 13)  // se la pos del blocco a destra non è fuori dalla mappa
                            wallCheck2 = gp.tileM.houseHitbox[tileRow+1][tileCol+1];  // carichiamo l'hitbox del blocco in alto a sinistra
                        wallCheckCx = gp.tileM.houseHitbox[tileRow+1][tileCol];
                    }
                }else
                    entity.collisionOn = true;
            break;
            case "left":
                if((entity.hitbox.x - entity.speed) >= gp.gameBorderLeftX){  // se non supera il bordo di gioco
                    if(tileCol > 0){  // nella prima colonna non serve controllare se sopra c'è un blocco
                        hitboxCheck1.x = (int)(hitboxUpSx.x - entity.speed);  // imposta la hitbox delle due hitbox da controllare (in alto/basso a sinistra) nella pos in cui si deve spostare
                        hitboxCheck1.y = hitboxUpSx.y;
                        hitboxCheck2.x = (int)(hitboxDwSx.x - entity.speed);
                        hitboxCheck2.y = hitboxDwSx.y;
                        if(tileRow-1 >= 0)  // se la pos del blocco sopra non è fuori dalla mappa
                            wallCheck1 = gp.tileM.houseHitbox[tileRow-1][tileCol-1];  // carichiamo l'hitbox del blocco in alto a sinistra
                        if(tileRow+1 < 11)  // se la pos del blocco sotto non è fuori dalla mappa
                            wallCheck2 = gp.tileM.houseHitbox[tileRow+1][tileCol-1];  // carichiamo l'hitbox del blocco in basso a sinistra
                        wallCheckCx = gp.tileM.houseHitbox[tileRow][tileCol-1];
                    }
                }else
                    entity.collisionOn = true;
            break;
            case "right":
                if((entity.hitbox.x + entity.hitbox.width + entity.speed) <= gp.gameBorderRightX){  // se non supera il bordo di gioco
                    if(tileCol < 12){  // nell'ultima colonna non serve controllare se sopra c'è un blocco
                        hitboxCheck1.x = (int)(hitboxUpDx.x - entity.speed);  // imposta la hitbox delle due hitbox da controllare (in alto/basso a destra) nella pos in cui si deve spostare
                        hitboxCheck1.y = hitboxUpDx.y;
                        hitboxCheck2.x = (int)(hitboxDwDx.x - entity.speed);
                        hitboxCheck2.y = hitboxDwDx.y;
                        if(tileRow-1 >= 0)  // se la pos del blocco sopra non è fuori dalla mappa
                            wallCheck1 = gp.tileM.houseHitbox[tileRow-1][tileCol+1];  // carichiamo l'hitbox del blocco in alto a destra
                        if(tileRow+1 < 11)  // se la pos del blocco sotto non è fuori dalla mappa
                            wallCheck2 = gp.tileM.houseHitbox[tileRow+1][tileCol+1];  // carichiamo l'hitbox del blocco in basso a destra
                        wallCheckCx = gp.tileM.houseHitbox[tileRow][tileCol+1];
                    }
                }else
                    entity.collisionOn = true;
            break;
        }
        // controllo delle hitbox
        if(wallCheckCx != null && (hitboxCheck1.intersects(wallCheckCx) || hitboxCheck2.intersects(wallCheckCx))){  // se entrambe le hitbox intercettano il blocco al centro
            entity.collisionOn = true;  // allora l'hitbox dell'entity colpisce interamente il blocco
        }
        else if(wallCheck1 != null && hitboxCheck1.intersects(wallCheck1)){  // se solo l'hitbox1 intercetta il blocco in wallCheck1
            if(wallCheckCx == null || !hitboxCheck2.intersects(wallCheckCx)){  // se l'hitbox2 non intercetta il blocco al centro
                if(entity.direction == "up" || entity.direction == "down"){  // se la dir è verso l'alto/basso sposta verso destra
                    entity.imageP.x += entity.speed;
                    entity.hitbox.x += entity.speed;
                }else{  // se la dir è verso destra/sinistra sposta verso il basso
                    entity.imageP.y += entity.speed;
                    entity.hitbox.y += entity.speed;
                }
            }else
                entity.collisionOn = true;  // altrimenti non passa
        }else if(wallCheck2 != null && hitboxCheck2.intersects(wallCheck2)){  // se solo l'hitbox2 intercetta il blocco in wallCheck2
            if(wallCheckCx == null || !hitboxCheck1.intersects(wallCheckCx)){  // se l'hitbox1 non intercetta il blocco al centro
                if(entity.direction == "up" || entity.direction == "down"){  // se la dir è verso l'alto/basso sposta verso sinistra
                    entity.imageP.x -= entity.speed;
                    entity.hitbox.x -= entity.speed;
                }else{  // se la dir è verso l'alto/basso sposta verso l'alto
                    entity.imageP.y -= entity.speed;
                    entity.hitbox.y -= entity.speed;
                }
            }else
                entity.collisionOn = true;
        }
    }
    
    public boolean checkPlayerCollision(Entity enemy, Player player, boolean checkPlayer) {
        if(!checkPlayer){
        // se il check del player è falso vuol dire che l'enemy non ha colpito il player
        // Controlla se l'hitbox del nemico interseca l'hitbox del giocatore
            if (enemy.hitbox.intersects(player.hitbox) && !player.invulnerable) {  // se colpisce l'hitbox del player e non è invulnerabile
                // Se il nemico sta andando a destra e il giocatore è alla sua destra, ferma il nemico
                enemy.collisionOn = true;
                System.out.println("Colpito player");
                player.lifeNumber -= 1;
                player.kill();
                return true;
                // appena l'enemy colpisce il player, attiva la var. bool playerCollision nell'oggetto enemy
                // cosi che la funzione non viene chiamata finche playerCollision non torna false dopo che finisce il timer in enemy
            }
        }
        return false;
    }
 
    public Point checkObj(Entity entity, boolean player, Graphics2D g2){
        int index = 999; // default index
        for(int row=0; row<gp.maxGameRow; row++){
            for(int col=0; col<gp.maxGameCol; col++){
                if(gp.obj[row][col] != null){  //se non è null
                    Rectangle entityHitboxCheck = new Rectangle(entity.hitbox.x, entity.hitbox.y, entity.hitboxWidth, entity.hitboxHeight);
                    Rectangle objHitboxCheck = new Rectangle(gp.obj[row][col].hitbox.x, gp.obj[row][col].hitbox.y, gp.tileSize, gp.tileSize);

                    switch(entity.direction){
                        case "up":
                            entityHitboxCheck.y -= entity.speed;
                        break;
                        case "down":
                            entityHitboxCheck.y += entity.speed;
                        break;
                        case "left":
                            entityHitboxCheck.x -= entity.speed;
                        break;
                        case "right":
                            entityHitboxCheck.x += entity.speed;
                        break;
                    }

                    if(entityHitboxCheck.intersects(objHitboxCheck)){  // controlla se l'hitbox del player interseca l'hitbox dell'oggetto
                        if(gp.obj[row][col].collision && !(gp.obj[row][col] instanceof Bomb)){  // se puo essere scontrato setta la collisione del player a true
                            entity.collisionOn = true;
                        }
                        if(player){ // se è il player a toccare l'oggetto allora ritorna l'indice
                            return new Point(col, row);
                        }
                    }
                    objHitboxCheck = null; // viene eliminato per lasciare spazio alla memoria
                    entityHitboxCheck = null; // viene eliminato per lasciare spazio alla memoria
                }
            }
        }
        return new Point(999, 999);  // punto default
    }

    public void checkBomb(Entity entity){  // FATTO DA FRANCESCO PAGLIACCIA™
    
        Rectangle entityHitboxCheck = new Rectangle(entity.hitbox.x, entity.hitbox.y, entity.hitboxWidth, entity.hitboxHeight);
        switch(entity.direction){
            case "up":
                entityHitboxCheck.y -= entity.speed;
            break;
            case "down":
                entityHitboxCheck.y += entity.speed;
            break;
            case "left":
                entityHitboxCheck.x -= entity.speed;
            break;
            case "right":
                entityHitboxCheck.x += entity.speed;
            break;
        }
        for(Bomb bomb: gp.bombH.bombs){
            if(!bomb.exploded){
                if(entityHitboxCheck.intersects(bomb.hitbox)){
                    if(entity.bombExitHitbox){  // controlla se è gia uscito
                        entity.collisionOn = true;
                    }
                }else{
                    entity.bombExitHitbox = true;
                }
            }else{
                entity.bombExitHitbox = false;
            }
        }
    }

    public void checkBomb2(Entity entity){  // sistema carino che ti sposta dove è libera la posizione piu vicina
        for(Bomb bomb: gp.bombH.bombs){
            if(bomb.exploded){
                entity.goTo = ""; // resetta la posizione in cui deve andare l'entity
                break;
            }
            String direction = "";
            int bombLeftBorder = bomb.x;
            int bombRightBorder = bomb.x + bomb.hitbox.width;
            int bombTopBorder = bomb.y;
            int bombBottomBorder = bomb.y + bomb.hitbox.height;
            int tileCol = bomb.tileCol;
            int tileRow = bomb.tileRow;
            int playerDistance = 999;
            if(entity.goTo == ""){  // se la posizione in cui deve andare l'entity è vuota
                // allora trova la posizione piu vicina
                if(tileCol-1 >= 0 && gp.obj[tileRow][tileCol-1] == null && gp.tileM.houseTileNum[tileRow][tileCol-1] == 0){  // se Sx libera
                    direction = "Sx";
                    playerDistance = Math.abs(entity.getCenterX() - bombLeftBorder);  // modifico la distanza dal centro del player al bordo a sinistra della bomba
                }
                if(tileCol+1 < 13 && gp.obj[tileRow][tileCol+1] == null && gp.tileM.houseTileNum[tileRow][tileCol+1] == 0){  // se Dx libera
                    int distanceCheck = Math.abs(entity.getCenterX() - bombRightBorder);
                    if(distanceCheck < playerDistance){  // se una delle due distanze è minore di una gia controllata
                        direction = "Dx";  // cambio la direzione
                        playerDistance = distanceCheck;  // reimposto la distanza a quella piu piccola
                    }
                }
                if(tileRow-1 >= 0 && gp.obj[tileRow-1][tileCol] == null && gp.tileM.houseTileNum[tileRow-1][tileCol] == 0){  // se Up libera
                    int distanceCheck = Math.abs(entity.getCenterY() - bombTopBorder);
                    if(distanceCheck < playerDistance){  // se una delle due distanze è minore di una gia controllata
                        direction = "Up";  // cambio la direzione
                        playerDistance = distanceCheck;  // reimposto la distanza a quella piu piccola
                    }
                }
                if(tileRow+1 < 11 && gp.obj[tileRow+1][tileCol] == null && gp.tileM.houseTileNum[tileRow+1][tileCol] == 0){  // se Up libera
                    int distanceCheck = Math.abs(entity.getCenterY() - bombBottomBorder);
                    if(distanceCheck < playerDistance){  // se una delle due distanze è minore di una gia controllata
                        direction = "Dw";  // cambio la direzione
                        playerDistance = distanceCheck;  // reimposto la distanza a quella piu piccola
                    }
                }
                entity.goTo = direction;
            }
            // System.out.println("Direzione libera: "+direction); // da eliminare
            // controllo hitbox con bomba
            if(!bomb.exploded && entity.hitbox.intersects(bomb.hitbox)){  // se il player colpisce una bomba
                // direction = getPlayerNearestAvTile(entity, bomb);
                switch (entity.goTo){
                    case "Sx":
                        entity.imageP.x -= entity.speed;  // sposto l'entity verso sinistra
                        entity.hitbox.x -= entity.speed;
                    break;
                    case "Dx":
                        entity.imageP.x += entity.speed;
                        entity.hitbox.x += entity.speed;
                    break;
                    case "Up":
                        entity.imageP.y -= entity.speed;
                        entity.hitbox.y -= entity.speed;
                    break;
                    case "Dw":
                        entity.imageP.y += entity.speed;
                        entity.hitbox.y += entity.speed;
                    break;
                }
            }
        }
    }
}
