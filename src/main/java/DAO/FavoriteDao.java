package DAO;


import DomainModel.Activity;
import DomainModel.Client;

public interface FavoriteDao {

    void insertFavorite(String fiscalCode, int idActivity);
    Activity[] getFavoriteActivites(String fiscalCode);
    Client[] getFavoritesByActivity(int idAd);
    void deleteFavorite(String fiscalCode, int idActivity);
}
