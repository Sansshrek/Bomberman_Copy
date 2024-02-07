package entity;

import java.util.ArrayList;
import java.util.Collections;

public class StupidEntity implements EntityMovementBehaviour{
    
    public void updateMovement(Entity entity){

        if(entity.hitbox.x == entity.getTileX() && entity.hitbox.y == entity.getTileY()){
            if(entity.collisionOn && entity.gp.cChecker.canMove(entity)){  // se in quella direzione non si puo muovere
                ArrayList<String> directions = entity.gp.cChecker.validDirections(entity);  // prende ogni posizione in cui si puo muovere
                directions.remove(entity.direction);  // rimuove dalla lista la direzione in cui sta andando perche collide
                Collections.shuffle(directions);
                if(directions.size() > 0)
                    entity.direction = directions.get(0);  // sceglie una direzione a caso tra quelle disponibili  
            }
            entity.notifyObservers();
        }
    }

}