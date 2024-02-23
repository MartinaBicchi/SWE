package BusinessLogic;

import DomainModel.Activity;

public interface Subject {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(Activity activity);
}
