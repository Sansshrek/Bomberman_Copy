package entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StupidEntity implements EntityMovementBehaviour{
    
    public void updateMovement(Entity entity){
        if(entity.hitbox.x == entity.getTileX() && entity.hitbox.y == entity.getTileY()){
            entity.collisionOn = false;

            entity.gp.cChecker.checkTile(entity);
            entity.gp.cChecker.checkObj(entity);
            if(entity.collisionOn){
                ArrayList<String> directions = entity.gp.cChecker.validDirections(entity);  // prende ogni posizione in cui si puo muovere
                Collections.shuffle(directions);  
                for(String dir: directions){  // per ogni posizione disponibile in cui si puo muovere
                    entity.collisionOn = false;  // resettiamo la collisione
                    entity.direction = dir;  // impostiamo la posizione per l'entity
                    entity.gp.cChecker.checkTile(entity);  // se in quella posizione si puo muovere allora entity.collision resta false
                    if(!entity.collisionOn){  // se si puo muovere
                        break;  // esce dal ciclo e lascia la direzione inserita come quella scelta
                    }
                }
            }
            entity.notifyObservers();
        }
    }

}