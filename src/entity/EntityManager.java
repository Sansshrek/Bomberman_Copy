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
        
    }
}
