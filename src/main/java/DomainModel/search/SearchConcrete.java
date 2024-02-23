package DomainModel.search;


import DAO.Database;
import DomainModel.Activity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchConcrete implements Search {
    private final String type;


    public SearchConcrete(String type) {
        this.type = type;
    }

    @Override
    public Activity[] searchActivity() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Activity> activitiesList = new ArrayList<>();

        try {
            connection = Database.getConnection("main.db");
            String query = "SELECT * FROM activities WHERE type = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, type); // Imposta il parametro sulla base del valore 'type'

            resultSet = preparedStatement.executeQuery(); // Esegue la query

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                int advertiserId = resultSet.getInt("advertiserId");
                String address = resultSet.getString("address");
                String city = resultSet.getString("city");
                int duration = resultSet.getInt("duration");
                int price=resultSet.getInt("price");

                Activity activity = new Activity(id, title, description, type, advertiserId, address, city, duration, price);
                activitiesList.add(activity);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return activitiesList.toArray(new Activity[0]);
    }



}