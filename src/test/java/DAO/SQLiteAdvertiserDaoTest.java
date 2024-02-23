package DAO;

import DomainModel.Activity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SQLiteAdvertiserDaoTest {
    private AdvertiserDao advertiserDao;
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
        advertiserDao = new SQLiteAdvertiserDao();
        activityDao=new SQLiteActivityDao();

        connection.prepareStatement("DELETE FROM activities").executeUpdate();
        connection.prepareStatement("DELETE FROM advertisers").executeUpdate();
        connection.prepareStatement("DELETE FROM clients").executeUpdate();
        connection.prepareStatement("DELETE FROM bookings").executeUpdate();

        connection.prepareStatement("INSERT INTO clients VALUES ('RSSMRA00A00A000A', 'Mario', 'Rossi')").executeUpdate();
        connection.prepareStatement("INSERT INTO advertisers VALUES (1, 0, 'Get Your Guide', 100, null, null)").executeUpdate();
        connection.prepareStatement("INSERT INTO activities VALUES (1, 'Test Title', 'Test Description', 'Test Type',1, 'Test Address', 'Test City', 1, 50)").executeUpdate();


    }


    //Test per verificare che eliminando un Advertiser vengano eliminati anche le attività ad esso associati dal database
    @Test
    public void deleteAdvertiserAndActivitiesTest() throws Exception {
        advertiserDao.remove(1);
        Activity[] activity = activityDao.getActivitybyAdvertiser(1);
        assertEquals(0, activity.length);
    }

}
