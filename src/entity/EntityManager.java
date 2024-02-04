package entity;

import java.util.concurrent.ConcurrentHashMap;

//L'EntityManager è un singleton che gestisce tutte le entità del gioco.
public class EntityManager implements EntityObserver { //EntityManagerObservable
    private static EntityManager instance = null;

    public static EntityManager getInstance() {
        if(instance == null){
            instance = new EntityManager();}
        return instance;
    }
    private ConcurrentHashMap<Integer, Entity> entityMap;  // Mappa 
    //private List<EntityObserver> observers;

    public EntityManager() {
        this.entityMap = new ConcurrentHashMap<>();  //
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
