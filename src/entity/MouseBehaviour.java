package entity;

import java.awt.Point;
import java.util.ArrayList;

public class MouseBehaviour implements EntityMovementBehaviour{
    public void updateMovement(Entity entity){
        entity.collisionOn = false;
        
        int mouseTileNumCol = (entity.mouseX - 72) / entity.gp.tileSize;
        int mouseTileNumRow = (entity.mouseY - 120) / entity.gp.tileSize;
        // System.out.println("Mouse Row: "+mouseTileNumRow+" Mouse Col: "+mouseTileNumCol);

        if(entity.getTileNumCol() != mouseTileNumCol || entity.getTileNumRow() != mouseTileNumRow || !entity.fullyInsideTile()){
            Point findP = new Point(mouseTileNumCol, mouseTileNumRow);  // prendo la pos del player
            ArrayList<Node> path = entity.gp.cChecker.findPath(entity, findP.y, findP.x, entity.gp);  // cerco il sentiero per arrivare al player
            entity.pathSearch = path;
            if(path.size() > 1){  // se puo muoversi verso il player
                if(entity.pathSearch.size() > 1){
                    Node nextPos = entity.pathSearch.get(1);  // la prima posizione è quella dove si trova l'entity, la seconda è dove deve andare dopo
                    int nextRow = nextPos.getRow() - entity.getTileNumRow();
                    int nextCol = nextPos.getCol() - entity.getTileNumCol();
                    if(nextRow == -1 && nextCol == 0)  // up
                        entity.direction = "up";
                    if(nextRow == +1 && nextCol == 0)  // down
                        entity.direction = "down";
                    if(nextRow == 0 && nextCol == -1)  // left
                        entity.direction = "left";
                    if(nextRow == 0 && nextCol == +1)  // right
                        entity.direction = "right";
                }
                entity.notifyObservers();
            }else{
                // entity.collisionOn = true;
            }

            if((entity.getTileNumCol() == mouseTileNumCol && entity.getTileNumRow() == mouseTileNumRow && entity.fullyInsideTile()) || path.size() <= 0){
                // se è 
                entity.collisionOn = true;
            }
            
            entity.gp.cChecker.checkTile(entity);  // se in quella posizione si puo muovere allora collisionOn resta false
            entity.gp.cChecker.checkObj(entity);
            
            entity.changeSpriteDirection();

            // hitbox.x = x + hitboxX;
            // hitbox.y = y + hitboxY;
            // controlliamo cosa fare con l'oggetto
            if(!entity.collisionOn){
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
