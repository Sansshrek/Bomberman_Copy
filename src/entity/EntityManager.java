package entity;

import java.util.concurrent.ConcurrentHashMap;

public class EntityManager implements EntityObserver { //EntityManagerObservable
    
    private ConcurrentHashMap<Integer, Entity> entityMap;
    //private List<EntityObserver> observers;

    public EntityManager() {
        this.entityMap = new ConcurrentHashMap<>();
        //observers = new ArrayList<>();
        
    }

    public void updateEntity(Entity entity){
        entityMap.put(entity.uniCode, entity);
        /*if (entity instanceof Player) {
            System.out.println("Entity updated" + entity.hitbox.x + " " + entity.hitbox.y);  }
        */
    }
    public void removeEntity(int uniCode){
        entityMap.remove(uniCode);
    }
}
