package entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StupidEntity implements EntityBehavior{
    
    public void update(Entity entity){
        List<String> directions = Arrays.asList("up", "down", "left", "right");
        Collections.shuffle(directions);
        for (String dir : directions) {  // itera le posizioni disponibili in cui puo andare
            entity.direction = dir;
            entity.gp.cChecker.checkTile(entity);  // controlla se puo andare in quella posizione
            if(!entity.collisionOn){
                break;
            }
        }
    }

}
