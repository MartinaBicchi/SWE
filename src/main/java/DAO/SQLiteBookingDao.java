package DAO;

import DomainModel.Booking;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class SQLiteBookingDao implements BookingDao {

    @Override
    public void insert( Booking booking) {
        try {
            Connection connection = Database.getConnection();

            String insertQuery = "INSERT INTO bookings (date, time, idActivity, idClient) " +
                    "VALUES (?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setObject(1, Date.valueOf(booking.getDate())); // Convert LocalDate to java.sql.Date
            preparedStatement.setObject(2, Time.valueOf(booking.getTime())); // Convert LocalTime to java.sql.Time
            preparedStatement.setInt(3, booking.getAdId());
            preparedStatement.setString(4, booking.getClientId());

            preparedStatement.executeUpdate();

            preparedStatement.close();
            Database.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestione dell'eccezione, log, o altra logica necessaria
        }
    }

    @Override
    public boolean remove(Integer idBooking) {
        try {
            Connection connection = Database.getConnection();

            // Abilita le foreign keys
            Statement pragmaStatement = connection.createStatement();
            pragmaStatement.execute("PRAGMA foreign_keys = ON");
            pragmaStatement.close();

            String deleteQuery = "DELETE FROM bookings WHERE id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, idBooking);

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
    public void updateBookingDate(Booking booking, LocalDate newDate) {
        try {
            Connection connection = Database.getConnection();

            String updateQuery = "UPDATE bookings SET date = ? WHERE id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setDate(1, Date.valueOf(newDate));
            preparedStatement.setInt(2, booking.getId());

            preparedStatement.executeUpdate();

            preparedStatement.close();
            Database.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestione dell'eccezione, log, o altra logica necessaria
        }
    }

    @Override
    public void updateBookingTime(Booking booking, LocalTime newTime) {
        try {
            Connection connection = Database.getConnection();

            String updateQuery = "UPDATE bookings SET time = ? WHERE id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setTime(1, Time.valueOf(newTime));
            preparedStatement.setInt(2, booking.getId());

            preparedStatement.executeUpdate();

            preparedStatement.close();
            Database.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestione dell'eccezione, log, o altra logica necessaria
        }
    }

    @Override
    public Booking get(Integer id) {
        try {
            Connection connection = Database.getConnection();
            Booking booking = null;

            String selectQuery = "SELECT * FROM bookings WHERE id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                booking = new Booking(
                        resultSet.getInt("id"),
                        resultSet.getDate("date").toLocalDate(),
                        resultSet.getTime("time").toLocalTime(),
                        resultSet.getInt("idActivity"),
                        resultSet.getString("idClient")
                );
            }

            resultSet.close();
            preparedStatement.close();
            Database.closeConnection(connection);
            return booking;
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestione dell'eccezione, log, o altra logica necessaria
        }

        return null;
    }

    @Override
    public List<Booking> getAll() {
        List<Booking> bookingList = new ArrayList<>();

        try {
            Connection connection = Database.getConnection();

            String selectQuery = "SELECT * FROM bookings";

            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Booking booking = extractBookingFromResultSet(resultSet);
                bookingList.add(booking);
            }

            preparedStatement.close();
            Database.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestione dell'eccezione, log, o altra logica necessaria
        }

        return List.of(bookingList.toArray(new Booking[0]));
    }

    @Override
    public Booking[] getBookingByActivity(int idActivity) {
        List<Booking> bookingList = new ArrayList<>();

        try {
            Connection connection = Database.getConnection();

            String selectQuery = "SELECT * FROM bookings WHERE idActivity = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, idActivity);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Booking booking = extractBookingFromResultSet(resultSet);
                bookingList.add(booking);
            }

            preparedStatement.close();
            Database.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookingList.toArray(new Booking[0]);
    }

    @Override
    public Booking[] getBookingByAdvertiser(int idAdvertiser) {
        //get bookings by advertiser
        List<Booking> bookingList = new ArrayList<>();
        try {
            Connection connection = Database.getConnection();
            String selectQuery = "SELECT * FROM bookings INNER JOIN activities ON bookings.idActivity = activities.id WHERE advertiserId = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, idAdvertiser);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Booking booking = extractBookingFromResultSet(resultSet);
                bookingList.add(booking);
            }
            preparedStatement.close();
            Database.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookingList.toArray(new Booking[0]);
    }

    private Booking extractBookingFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        LocalDate date = resultSet.getDate("date").toLocalDate();
        LocalTime time = resultSet.getTime("time").toLocalTime();
        int idAd = resultSet.getInt("idActivity");
        String idClient = resultSet.getString("idClient");

        return new Booking(id, date, time, idAd, idClient);
    }

    @Override
    public Integer getNextBookingId() throws SQLException {
        Connection connection = Database.getConnection();

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT MAX(id) FROM bookings");
        int id = rs.getInt(1) + 1;

        rs.close();
        stmt.close();
        Database.closeConnection(connection);

        return id;
    }


}
