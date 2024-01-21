package entity;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class EntityManager implements EntityObserver { //EntityManagerObservable
    
    public Point imageP;
    public Rectangle hitbox;
    public double speed;
    public boolean invulnerable, died, extinguished;
    public int uniCode;
    
    private HashMap<Entity, Integer> entityMap;
    //private List<EntityObserver> observers;

    public EntityManager() {
        entityMap = new HashMap<>();
        
        //observers = new ArrayList<>();
        
    }

    public void updateEntity(Entity entity){
        entityMap.put(entity, entity.uniCode);
        if(entity instanceof Player){
            System.out.println("hitbox.x:" + entity.hitbox.x + "hitbox.y" + entity.hitbox.y);
        }
    }
}
