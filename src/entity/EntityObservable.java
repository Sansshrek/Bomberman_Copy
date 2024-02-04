package entity;

//Implementiamo l'Observable per le entità
public interface EntityObservable {
    //metodi del pattern observer
    public void registerObserver(EntityObserver observer);

    public void removeObserver(EntityObserver observer);

    public void notifyObservers();
}
