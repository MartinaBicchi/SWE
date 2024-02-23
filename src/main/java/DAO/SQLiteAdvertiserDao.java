package DAO;

import DomainModel.Advertiser;
import DomainModel.Agency;
import DomainModel.Private;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class SQLiteAdvertiserDao implements AdvertiserDao {

    @Override
    public void insert(Advertiser advertiser) {
        try {
            Connection connection = Database.getConnection();

            String insertQuery = "INSERT INTO advertisers (id, balance, agency_name, agency_costs, private_name, private_last_name) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, advertiser.getId());
            preparedStatement.setInt(2, advertiser.getBalance());

            // Check the type of advertiser and set the appropriate fields
            if (advertiser instanceof Agency) {
                preparedStatement.setString(3, ((Agency) advertiser).getName());
                preparedStatement.setInt(4, ((Agency) advertiser).getAgencyCosts());
                preparedStatement.setNull(5, Types.VARCHAR);
                preparedStatement.setNull(6, Types.VARCHAR);
            } else if (advertiser instanceof Private) {
                preparedStatement.setNull(3, Types.VARCHAR);
                preparedStatement.setNull(4, Types.INTEGER);
                preparedStatement.setString(5, ((Private) advertiser).getName());
                preparedStatement.setString(6, ((Private) advertiser).getLastName());
            }

            preparedStatement.executeUpdate();

            preparedStatement.close();
            Database.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestione dell'eccezione, log, o altra logica necessaria
        }
    }

    @Override
    public boolean remove(Integer idAdvertiser) {
        try {
            Connection connection = Database.getConnection();

            // Abilita le foreign keys
            Statement pragmaStatement = connection.createStatement();
            pragmaStatement.execute("PRAGMA foreign_keys = ON");
            pragmaStatement.close();

            String deleteQuery = "DELETE FROM advertisers WHERE id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);

            preparedStatement.setInt(1, idAdvertiser);

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
    public void updateAdvertiser(Advertiser advertiser, int newBalance) {
        try {
            Connection connection = Database.getConnection();

            String updateQuery = "UPDATE advertisers SET balance = ? WHERE id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setInt(1, newBalance);
            preparedStatement.setInt(2, advertiser.getId());

            preparedStatement.executeUpdate();

            preparedStatement.close();
            Database.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestione dell'eccezione, log, o altra logica necessaria
        }
    }

    @Override
    public Advertiser get(Integer id) {
        try {
            Connection connection = Database.getConnection();
            Advertiser advertiser = null;

            String selectQuery = "SELECT * FROM advertisers WHERE id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int bankAccount = resultSet.getInt("balance");
                String agencyName = resultSet.getString("agency_name");
                int agency = resultSet.getInt("agency_costs");
                String privateName = resultSet.getString("private_name");
                String privateLastName = resultSet.getString("private_last_name");

                if (agencyName != null && agency >= 0) {
                    advertiser = new Agency(id, bankAccount, agencyName, agency);
                } else if (privateName != null && privateLastName != null) {
                    advertiser = new Private(id, bankAccount, privateName, privateLastName);
                }
            }

            preparedStatement.close();
            Database.closeConnection(connection);

            return advertiser;
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestione dell'eccezione, log, o altra logica necessaria
        }

        return null;
    }

    @Override
    public List<Advertiser> getAll() {
        List<Advertiser> advertiserList = new ArrayList<>();

        try {
            Connection connection = Database.getConnection();

            String selectQuery = "SELECT * FROM advertisers";

            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Advertiser advertiser = extractAdvertiserFromResultSet(resultSet);
                advertiserList.add(advertiser);
            }

            preparedStatement.close();
            Database.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestione dell'eccezione, log, o altra logica necessaria
        }

        return List.of(advertiserList.toArray(new Advertiser[0]));
    }

    private Advertiser extractAdvertiserFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int bankAccount = resultSet.getInt("balance");
        String agencyName = resultSet.getString("agency_name");
        int agencyFee = resultSet.getInt("agency_costs");
        String privateOwnersName = resultSet.getString("private_name");
        String privateOwnersLastName = resultSet.getString("private_last_name");

        if (agencyName != null && agencyFee > 0) {
            return new Agency(id, bankAccount, agencyName, agencyFee);
        } else if (privateOwnersName != null && privateOwnersLastName != null) {
            return new Private(id, bankAccount, privateOwnersName, privateOwnersLastName);
        }

        return null;
    }

    @Override
    public Integer getNextAdvertiserId() throws SQLException {
        Connection connection = Database.getConnection();

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT MAX(id) FROM advertisers");
        int id = rs.getInt(1) + 1;

        rs.close();
        stmt.close();
        Database.closeConnection(connection);

        return id;
    }


}
