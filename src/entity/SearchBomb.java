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
        if(entity.hitbox.x == entity.getTileX() && entity.hitbox.y == entity.getTileY()){
            // entity.findP = new Point(entity.gp.player.getTileNumCol(), entity.gp.player.getTileNumRow());  // prendo la pos del player
            Bomb bombSearch = null;
            for(Bomb bomb: entity.gp.bombH.bombs){
                if(!bomb.exploded && !bomb.extinguished){
                    bombSearch = bomb;  // prende la prima bomba non esplosa
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
            }else{  // comportamento idiota
                entity.gp.cChecker.checkTile(entity);
                System.out.println("Collision: " + entity.collisionOn + " Entity.x: " + (entity.hitbox.y + entity.hitboxHeight));
                entity.gp.cChecker.checkObj(entity);
                System.out.println("Direction: " + entity.direction);
                System.out.println("Collision: " + entity.collisionOn + " Entity.x: " + (entity.hitbox.y + entity.hitboxHeight));
                if(entity.collisionOn){
                    ArrayList<String> directions = entity.gp.cChecker.validDirections(entity);  // prende ogni posizione in cui si puo muovere
                    Collections.shuffle(directions);  
                    for(String dir: directions){  // per ogni posizione disponibile in cui si puo muovere
                        entity.collisionOn = false;  // resettiamo la collisione
                        entity.direction = dir;  // impostiamo la posizione per l'entity
                        entity.gp.cChecker.checkTile(entity);  // se in quella posizione si puo muovere allora entity.collision resta false
                        System.out.println("Direction: " + entity.direction);
                        System.out.println("Collision: " + entity.collisionOn);
                        if(!entity.collisionOn){  // se si puo muovere
                            entity.notifyObservers();
                            break;  // esce dal ciclo e lascia la direzione inserita come quella scelta
                        }
                    }
                }
                
            }
            entity.notifyObservers();
        }
    }
}
