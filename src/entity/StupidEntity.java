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
            if(entity.collisionOn && entity.gp.cChecker.canMove(entity)){  // se in quella direzione non si puo muovere
                ArrayList<String> directions = entity.gp.cChecker.validDirections(entity);  // prende ogni posizione in cui si puo muovere
                directions.remove(entity.direction);  // rimuove dalla lista la direzione in cui sta andando perche collide
                Collections.shuffle(directions);
                if(directions.size() > 0)
                    entity.direction = directions.get(0);  // sceglie una direzione a caso tra quelle disponibili  
                /*
                for(String dir: directions){  // per ogni posizione disponibile in cui si puo muovere
                    entity.collisionOn = false;  // resettiamo la collisione
                    entity.direction = dir;  // impostiamo la posizione per l'entity
                    entity.gp.cChecker.checkTile(entity);  // se in quella posizione si puo muovere allora entity.collision resta false
                    if(!entity.collisionOn){  // se si puo muovere
                        break;  // esce dal ciclo e lascia la direzione inserita come quella scelta
                    }
                } */
            }
            entity.notifyObservers();
        }
    }

}