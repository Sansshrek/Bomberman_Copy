package entity;

public interface EntityObservable {
    public void registerObserver(EntityObserver observer);

    public void removeObserver(EntityObserver observer);

    public void notifyObservers();
}
