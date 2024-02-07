package entity;

import java.util.ArrayList;
import java.util.Collections;

public class StupidEntityRandom implements EntityMovementBehaviour{
    private String lastDirection = "";
    public void updateMovement(Entity entity){
        if(entity.hitbox.x == entity.getTileX() && entity.hitbox.y == entity.getTileY()){
            entity.collisionOn = false;
            
            if(entity.gp.cChecker.canMove(entity)){  // se si puo muovere in qualche direzione
                ArrayList<String> directions = entity.gp.cChecker.validDirections(entity);  // prende ogni posizione in cui si puo muovere
                if(directions.size() > 2){  // se è a una intersezione
                    directions.remove(lastDirection);  // rimuove dalla lista la direzione precedente
                    Collections.shuffle(directions);  // randomizza le direzioni
                    String newDirection = directions.get(0);  // sceglie una direzione a caso tra quelle disponibili
                    entity.direction = newDirection;
                    if(newDirection == "up")
                        lastDirection = "down";
                    else if(newDirection == "down")
                        lastDirection = "up";
                    else if(newDirection == "left")
                        lastDirection = "right";
                    else if(newDirection == "right")
                        lastDirection = "left";
                }else{
                    if(entity.collisionOn){  // se colpisce un muro
                        ArrayList<String> directionsHit = entity.gp.cChecker.validDirections(entity);  // prende ogni posizione in cui si puo muovere          
                        Collections.shuffle(directionsHit);  // randomizza le direzioni
                        entity.direction = directionsHit.get(0);  // sceglie una direzione a caso tra quelle disponibili
                    }
                }
            }
            entity.notifyObservers();
        }
    }
}
