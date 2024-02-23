package BusinessLogic;

import DAO.ClientDao;
import DAO.FavoriteDao;
import DAO.SQLiteFavoriteDao;
import DomainModel.Activity;
import DomainModel.Client;

public class ClientController implements Observer{
    private ClientDao clientDao;
    ActivityController activityController ;
    private FavoriteDao favoriteDao=new SQLiteFavoriteDao();

    public ClientController(ClientDao clientDao, ActivityController activityController) {
        this.clientDao = clientDao;
        this.activityController = activityController;
        activityController.addObserver(this);
    }

    public void addClient(String name, String lastName, String fiscalCode) {
        // Check if the client already exists
        Client client = null;
        try {
            client = clientDao.get(fiscalCode);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (client != null) {
            throw new IllegalArgumentException("Il cliente esiste già");
        } else {
            // Create a new Client instance
            Client newClient = new Client(fiscalCode, name, lastName);
            // Insert the new client into the database
            try {
                clientDao.insert(newClient);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void removeClient(String fiscalCode) {
        // Delete the client with the given fiscal code from the database
        try {
            clientDao.remove(fiscalCode);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("Cliente rimosso");
    }

    //Metodo update Observer
    public void update(Activity activity){
        Client[] clients = favoriteDao.getFavoritesByActivity(activity.getId());
        for(Client c: clients)
            System.out.println("L'attività " + activity.getTitle() + " è stato rimosso ed era nei preferiti di "+ c.getFiscalCode());
    }


    public Client getClient(String fiscalCode) {
        // Get the client with the given fiscal code from the database
        try {
            return  clientDao.get(fiscalCode);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
