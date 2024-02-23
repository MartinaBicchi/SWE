package DAO;

import DomainModel.Activity;
import DomainModel.Client;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class SQLiteActivityDaoTest {
    private ActivityDao activityDao;

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        // Set up database
        Database.setDatabase("main.db");
        Database.initDatabase();
    }

    @BeforeEach
    public void init() throws Exception {
        Connection connection = Database.getConnection();
        activityDao = new SQLiteActivityDao();

        connection.prepareStatement("DELETE FROM activities").executeUpdate();
        connection.prepareStatement("DELETE FROM advertisers").executeUpdate();
        connection.prepareStatement("DELETE FROM clients").executeUpdate();
        connection.prepareStatement("DELETE FROM bookings").executeUpdate();

        connection.prepareStatement("INSERT INTO clients VALUES ('RSSMRA00A00A000A', 'Mario', 'Rossi')").executeUpdate();
        connection.prepareStatement("INSERT INTO advertisers VALUES (1, 0, 'Get Your Guide', 100, null, null)").executeUpdate();
        connection.prepareStatement("INSERT INTO activities VALUES (1, 'Test Title', 'Test Description', 'Test Type',1, 'Test Address', 'Test City', 1, 50)").executeUpdate();


    }

    @Test
    public void insertActivityTest() throws Exception {
        Activity activity = new Activity(2, "Test Title", "Test Description2", "Test Type", 1, "Test Address", "Test City2", 1, 1);
        activityDao.insert(activity);

        assertEquals(2, activityDao.get(2).getId());
        assertEquals("Test Title", activityDao.get(2).getTitle());
        assertEquals("Test City2", activityDao.get(2).getCity());
        assertEquals(1, activityDao.get(2).getPrice());
        assertEquals(1, activityDao.get(2).getAdvertiserId());
    }

    //Test per verificare che il prezzo di un'attività sia correttamente aggiornato nel database
    @Test
    public void updateActivityPriceTest() throws Exception {
        Activity activity = activityDao.get(1);
        activityDao.updateActivity(activity,  200);
        assertEquals(200, activityDao.get(1).getPrice());
    }
}
