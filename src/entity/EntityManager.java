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
    private ConcurrentHashMap<Integer, Entity> entityMap;  // Mappa delle entita

    public EntityManager() {
        this.entityMap = new ConcurrentHashMap<>(); 
    }

    public void updateEntities(Entity entity){
        entityMap.put(entity.uniCode, entity);
    }
    public void removeEntities(int uniCode){
        entityMap.remove(uniCode);
    }
}
