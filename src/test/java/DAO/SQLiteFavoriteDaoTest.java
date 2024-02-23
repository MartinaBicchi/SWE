package DAO;

import DomainModel.Booking;
import DomainModel.Client;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;

public class SQLiteFavoriteDaoTest {
    private FavoriteDao favoriteDao;
    private ClientDao clientDao;


    @BeforeAll
    static void initDb() throws SQLException, IOException {
        // Set up database
        Database.setDatabase("main.db");
        Database.initDatabase();
    }

    @BeforeEach
    public void init() throws Exception {
        Connection connection = Database.getConnection();
        favoriteDao=new SQLiteFavoriteDao();
        clientDao=new SQLiteClientDao();

        connection.prepareStatement("DELETE FROM activities").executeUpdate();
        connection.prepareStatement("DELETE FROM advertisers").executeUpdate();
        connection.prepareStatement("DELETE FROM clients").executeUpdate();
        connection.prepareStatement("DELETE FROM bookings").executeUpdate();

        connection.prepareStatement("INSERT INTO clients VALUES ('RSSMRA00A00A000A', 'Mario', 'Rossi')").executeUpdate();
        connection.prepareStatement("INSERT INTO advertisers VALUES (1, 0, 'Get Your Guide', 100, null, null)").executeUpdate();
        connection.prepareStatement("INSERT INTO activities VALUES (1, 'Test Title', 'Test Description', 'Test Type',1, 'Test Address', 'Test City', 1, 50)").executeUpdate();


    }

    @Test
    public void getFavoritesByAdTest() throws Exception {
        clientDao.insert(new Client("LNGGNN00A00A000A", "Giacomo", "Lanno"));
        favoriteDao.insertFavorite("LNGGNN00A00A000A", 1);
        favoriteDao.insertFavorite("RSSMRA00A00A000A", 1);
        assertEquals(2, favoriteDao.getFavoritesByActivity(1).length);
    }

}
