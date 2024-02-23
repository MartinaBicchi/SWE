package DAO;

import DomainModel.Activity;
import DomainModel.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class SQLiteFavoriteDao implements FavoriteDao {

    @Override
    public void insertFavorite(String fiscalCode, int idAd) {
        try {
            Connection connection = Database.getConnection();

            String insertQuery = "INSERT INTO favorites (idActivity, idClient) VALUES (?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, idAd);
            preparedStatement.setString(2, fiscalCode);

            preparedStatement.executeUpdate();

            preparedStatement.close();
            Database.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestione dell'eccezione, log, o altra logica necessaria
        }
    }

    @Override
    public void deleteFavorite(String fiscalCode, int idActivity) {
        try {
            Connection connection = Database.getConnection();

            // Abilita le foreign keys
            Statement pragmaStatement = connection.createStatement();
            pragmaStatement.execute("PRAGMA foreign_keys = ON");
            pragmaStatement.close();

            String deleteQuery = "DELETE FROM favorites WHERE idActivity = ? AND idClient = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, idActivity);
            preparedStatement.setString(2, fiscalCode);

            preparedStatement.executeUpdate();

            preparedStatement.close();
            Database.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestione dell'eccezione, log, o altra logica necessaria
        }
    }

    @Override
    public Activity[] getFavoriteActivites(String fiscalCode) {
        List<Activity> favoriteAdList = new ArrayList<>();

        try {
            Connection connection = Database.getConnection();

            String selectQuery = "SELECT activities.* FROM favorites " +
                    "INNER JOIN activities ON favorites.idActivity = activities.id " +
                    "WHERE favorites.idClient = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, fiscalCode);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Activity ad = extractActivitiesFromResultSet(resultSet);
                favoriteAdList.add(ad);
            }

            preparedStatement.close();
            Database.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestione dell'eccezione, log, o altra logica necessaria
        }

        return favoriteAdList.toArray(new Activity[0]);
    }

    @Override
    public Client[] getFavoritesByActivity(int idAd){
        List<Client> adSavedBy = new ArrayList<>();

        try {
            Connection connection = Database.getConnection();

            String selectQuery = "SELECT clients.* FROM favorites " +
                    "INNER JOIN clients ON favorites.idClient = clients.fiscal_code " +
                    "WHERE favorites.idActivity = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, idAd);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Client client = extractClientFromResultSet(resultSet);
                adSavedBy.add(client);
            }

            preparedStatement.close();
            Database.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return adSavedBy.toArray(new Client[0]);

    }

    private Activity extractActivitiesFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String title = resultSet.getString("title");
        String description = resultSet.getString("description");
        String type = resultSet.getString("type");
        int advertiserid = resultSet.getInt("advertiserId");
        String address = resultSet.getString("address");
        String city = resultSet.getString("city");
        int duration= resultSet.getInt("duration");
        int price = resultSet.getInt("price");

        return new Activity(id, title, description, type, advertiserid, address, city, duration, price);
    }

    private Client extractClientFromResultSet(ResultSet resultSet) throws SQLException {
        String fiscalCode = resultSet.getString("fiscal_code");
        String name = resultSet.getString("name");
        String lastName = resultSet.getString("last_name");
        return new Client(fiscalCode, name, lastName);
    }
}