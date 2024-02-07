package entity;

//Implementazione dell'interfaccia Observer (Observer Pattern)
public interface EntityObserver {
    //metodi per l'aggiornamento delle entità osservate
    public void updateEntities(Entity entity);
    public void removeEntities(int uniCode);
}