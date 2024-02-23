package DAO;

import DomainModel.Activity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class SQLiteActivityDao implements ActivityDao {

    @Override
    public void insert(Activity activity) {
        try {
            Connection connection = Database.getConnection();

            String insertQuery = "INSERT INTO activities (title, description, type, advertiserId, address, city, duration, price) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, activity.getTitle());
            preparedStatement.setString(2, activity.getDescription());
            preparedStatement.setString(3, activity.getType());
            preparedStatement.setInt(4, activity.getAdvertiserId());
            preparedStatement.setString(5, activity.getAddress());
            preparedStatement.setString(6, activity.getCity());
            preparedStatement.setInt(7, activity.getDuration());
            preparedStatement.setInt(8, activity.getPrice());

            preparedStatement.executeUpdate();

            preparedStatement.close();
            Database.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestione dell'eccezione, log, o altra logica necessaria
        }
    }

    @Override
    public Activity get(Integer id) {
        try {
            Connection connection = Database.getConnection();
            Activity activity = null;

            String selectQuery = "SELECT * FROM activities WHERE id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                activity = new Activity(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getString("type"),
                        resultSet.getInt("advertiserId"),
                        resultSet.getString("address"),
                        resultSet.getString("city"),
                        resultSet.getInt("duration"),
                        resultSet.getInt("price")
                );
            }

            resultSet.close();
            preparedStatement.close();
            Database.closeConnection(connection);
            return activity;
        } catch (
                SQLException e) {
            e.printStackTrace();
            // Gestione dell'eccezione, log, o altra logica necessaria
        }

        return null;
    }


    @Override
    public boolean remove(Integer idActivity) {
        try {
            Activity activity = get((int) idActivity);
            if (activity == null) return false;

            Connection connection = Database.getConnection();

            String deleteQuery = "DELETE FROM activities WHERE id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, idActivity);

            preparedStatement.executeUpdate();

            preparedStatement.close();
            Database.closeConnection(connection);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestione dell'eccezione, log, o altra logica necessaria
        }
        return false;
    }

    @Override
    public void updateActivity(Activity activity, int newPrice) {
        try {
            Connection connection = Database.getConnection();

            String updateQuery = "UPDATE activities SET price = ? WHERE id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setInt(1, newPrice);
            preparedStatement.setInt(2, activity.getId());

            preparedStatement.executeUpdate();

            preparedStatement.close();
            Database.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestione dell'eccezione, log, o altra logica necessaria
        }
    }

    @Override
    public List<Activity> getAll() {
        List<Activity> activityList = new ArrayList<>();

        try {
            Connection connection = Database.getConnection();

            String selectQuery = "SELECT * FROM activities";

            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Activity activity = extractActivitiesFromResultSet(resultSet);
                activityList.add(activity);
            }

            preparedStatement.close();
            Database.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestione dell'eccezione, log, o altra logica necessaria
        }

        return List.of(activityList.toArray(new Activity[0]));
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

    @Override
    public Activity[] getActivitybyAdvertiser(int idAdvertiser) {
        List<Activity> activityList = new ArrayList<>();

        try {
            Connection connection = Database.getConnection();

            String selectQuery = "SELECT * FROM activities WHERE advertiserId = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, idAdvertiser);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Activity activity = extractActivitiesFromResultSet(resultSet);
                activityList.add(activity);
            }

            preparedStatement.close();
            Database.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return activityList.toArray(new Activity[0]);
    }

    @Override
    public Integer getNextActivityId() throws SQLException {
        Connection connection = Database.getConnection();

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT MAX(id) FROM activities");
        int id = rs.getInt(1) + 1;

        rs.close();
        stmt.close();
        Database.closeConnection(connection);

        return id;
    }


}