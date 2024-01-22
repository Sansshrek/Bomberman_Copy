package entity;
import java.awt.Point;
import java.awt.Rectangle;

public interface EntityObserver {
    public void updateEntity(Entity entity);
    public void removeEntity(int uniCode);
}