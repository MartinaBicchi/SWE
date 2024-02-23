package BusinessLogic;


import DomainModel.Activity;

public interface Observer {
    void update(Activity activity);
}
