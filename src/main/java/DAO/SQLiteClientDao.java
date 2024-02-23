package DAO;

import DomainModel.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class SQLiteClientDao implements ClientDao {

    @Override
    public Client get(String fiscalCode) {
        try {
            Connection connection = Database.getConnection();
            Client client = null;

            String selectQuery = "SELECT * FROM clients WHERE fiscal_code = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, fiscalCode);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                client = new Client(
                        resultSet.getString("fiscal_code"),
                        resultSet.getString("name"),
                        resultSet.getString("last_name")
                );
            }

            resultSet.close();
            preparedStatement.close();
            Database.closeConnection(connection);
            return client;


        } catch (SQLException e) {
            e.printStackTrace();
            // Gestione dell'eccezione, log, o altra logica necessaria
        }

        return null;
    }

    @Override
    public List<Client> getAll() {
        List<Client> clientList = new ArrayList<>();

        try {
            Connection connection = Database.getConnection();

            String selectQuery = "SELECT * FROM clients";

            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Client client = extractClientFromResultSet(resultSet);
                clientList.add(client);
            }

            preparedStatement.close();
            Database.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestione dell'eccezione, log, o altra logica necessaria
        }

        return List.of(clientList.toArray(new Client[0]));
    }


    public void insert(Client client) {
        try {
            Connection connection = Database.getConnection();

            String insertQuery = "INSERT INTO clients (fiscal_code, name, last_name) " +
                    "VALUES (?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, client.getFiscalCode());
            preparedStatement.setString(2, client.getName());
            preparedStatement.setString(3, client.getLastName());

            preparedStatement.executeUpdate();

            preparedStatement.close();
            Database.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestione dell'eccezione, log, o altra logica necessaria
        }
    }

    @Override
    public boolean remove(String fiscalCode) {
        try {
            Connection connection = Database.getConnection();

            String deleteQuery = "DELETE FROM clients WHERE fiscal_code = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setString(1, fiscalCode);

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

    private Client extractClientFromResultSet(ResultSet resultSet) throws SQLException {
        String fiscalCode = resultSet.getString("fiscal_code");
        String name = resultSet.getString("name");
        String lastName = resultSet.getString("last_name");
        return new Client(fiscalCode, name, lastName);
    }



}

