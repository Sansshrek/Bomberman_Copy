package entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StupidEntity implements EntityMovementBehaviour{
    
    public void updateMovement(Entity entity){
        if(entity.collisionOn){
            List<String> directions = Arrays.asList("up", "down", "left", "right");
            Collections.shuffle(directions);
            entity.direction = directions.get(0);
            entity.notifyObservers();
        }
    }

}