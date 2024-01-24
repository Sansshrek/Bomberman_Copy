package entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import objects.Bomb;

import java.awt.Point;

public class SearchBomb implements EntityMovementBehaviour{
    EntityMovementBehaviour stupidBehaviour = new StupidEntity();
    
    public void updateMovement(Entity entity){
        // entity.findP = new Point(entity.gp.player.getTileNumCol(), entity.gp.player.getTileNumRow());  // prendo la pos del player
        Bomb bombSearch = null;
        for(Bomb bomb: entity.gp.bombH.bombs){
            if(!bomb.exploded && !bomb.extinguished){
                bombSearch = bomb;
                break;
            }
        }
        ArrayList<Node> path = null;
        if(bombSearch != null){
            Point findP = new Point(bombSearch.tileCol, bombSearch.tileRow);
            path = entity.gp.cChecker.findPath(entity, findP.y, findP.x, entity.gp);  // cerco il sentiero per arrivare al player
            entity.pathSearch = path;
        }
        if(path != null && path.size() > 1){  // se puo muoversi verso il player
            System.out.println("BADA "+bombSearch.tileCol+" "+bombSearch.tileRow);
            if(entity.hitbox.x == entity.getTileX() && entity.hitbox.y == entity.getTileY()){
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
            }
        }else{
            if(entity.collisionOn && entity.gp.cChecker.canMove(entity)){
                ArrayList<String> directions = entity.gp.cChecker.validDirections(entity);  // prende ogni posizione in cui si puo muovere
                Collections.shuffle(directions);  
                entity.direction = directions.get(0);  // prende la prima posizione random disponibile
                entity.notifyObservers();
            }
        }
    }
}
