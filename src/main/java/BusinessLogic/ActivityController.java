package BusinessLogic;

import DAO.*;
import DomainModel.Activity;
import DomainModel.Advertiser;
import DomainModel.Agency;
import DomainModel.Client;
import DomainModel.search.*;

import java.util.ArrayList;
import java.util.List;


public class ActivityController implements Subject {
    private ActivityDao activitydao;
    private FavoriteDao favoritedao=new SQLiteFavoriteDao();

    private List<Observer> observers = new ArrayList<>();//lista observers

    public ActivityController(ActivityDao dao) {
        this.activitydao = dao;
    }

    public boolean checkActivity(Activity activity) {
        //check if ad exists in database (by title, type, advertiserId, address, city, duration, price)
        //if exists return true else return false
        List<Activity> allActivites=new ArrayList<>();
        try {
            allActivites = activitydao.getAll(); // Assume che dao.getAll() restituisca tutti gli annunci dal database
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (Activity existingAd : allActivites) {
            if (existingAd.getTitle().equals(activity.getTitle()) &&
                    existingAd.getType().equals(activity.getType()) &&
                    existingAd.getAddress().equals(activity.getAddress()) &&
                    existingAd.getCity().equals(activity.getCity()) &&
                    existingAd.getPrice() == activity.getPrice() &&
                    existingAd.getDuration() == activity.getDuration() &&
                    existingAd.getAdvertiserId() == activity.getAdvertiserId()) {
                return true; // Se esiste un annuncio con gli stessi attributi, restituisci true
            }
        }

        return false;
    }

    public Activity[] searchAd(Search s) {
        return s.searchActivity();
    }


    public Activity createActivity(String title, String description, String type, int advertiserId, String address, String city, int duration, int price) throws Exception {
        // Implement the logic to create a new ad and save it to the database
        Activity newActivity = new Activity(activitydao.getNextActivityId(), title, description, type, advertiserId, address, city, duration, price);
        if (!checkActivity(newActivity)) {
            activitydao.insert(newActivity);
            return newActivity;
        } else {
            // stampa messaggio di errore
            System.out.println("Nel database è già presente un'attivita con gli stessi attributi, non è possibile inserire questo annuncio");
            return null;
        }
    }

    public void deleteActivity(int id) {
        // Implement the logic to delete an ad from the database
        Activity activityToDelete;
        try {
           activityToDelete=activitydao.get(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            activitydao.remove(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        notifyObservers(activityToDelete);

    }

    public void modifyPrice(Activity activity, int price) {
        // Implement the logic to modify the price of an activity
        activity.setPrice(price);
        try {
            activitydao.updateActivity(activity, price);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("Prezzo modificato in" + activity.getPrice());
    }


    //funzione che stampa tutti gli annunci nel sistema
    public void printAllAds() {
        List<Activity> activities = new ArrayList<>();
        try {
            activities = activitydao.getAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        for (Activity activity : activities) {
            System.out.println(activity);
        }
    }

    public Activity[] getActivityForAdvertiser(int advertiserId) {
        // Implement the logic to get all activites for a specific advertiser
        try {
            return activitydao.getActivitybyAdvertiser(advertiserId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Activity[] getFavouriteAds(String idClient) {
        // Implement the logic to get favorite activites for a client
        return favoritedao.getFavoriteActivites(idClient);
    }

    public void addToFavourites(String idClient, int idAd) {
        // Implement the logic to add an activity to the client's favorites
        favoritedao.insertFavorite(idClient, idAd);
        System.out.println("l'annuncio " + idAd + " e' stato aggiunto ai preferiti per il cliente: "+idClient);
    }

    public void removeFromFavourites(String idClient, int idAd) {
        // Implement the logic to remove an activity from the client's favorites
        favoritedao.deleteFavorite(idClient, idAd);
        System.out.println("l'annuncio " + idAd + " e' stato rimosso dai preferiti!");
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers(Activity activity) {
        for (Observer observer : observers) {
            observer.update(activity);
        }
    }
}