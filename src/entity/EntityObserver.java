package entity;
import java.awt.Point;
import java.awt.Rectangle;

//Implementazione dell'interfaccia Observer (Observer Pattern)
public interface EntityObserver {
    //metodi per l'aggiornamento delle entità osservate
    public void updateEntity(Entity entity);
    public void removeEntity(int uniCode);
}