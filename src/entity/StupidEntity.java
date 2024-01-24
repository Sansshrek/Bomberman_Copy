package entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StupidEntity implements EntityMovementBehaviour{
    
    public void updateMovement(Entity entity){
        if(entity.collisionOn && entity.gp.cChecker.canMove(entity)){
            ArrayList<String> directions = entity.gp.cChecker.validDirections(entity);  // prende ogni posizione in cui si puo muovere
            Collections.shuffle(directions);  
            entity.direction = directions.get(0);  // prende la prima posizione random disponibile
            entity.notifyObservers();
        }
    }

}