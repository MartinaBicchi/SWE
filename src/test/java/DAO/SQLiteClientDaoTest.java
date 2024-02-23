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

public class SQLiteClientDaoTest {
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
        clientDao=new SQLiteClientDao();

        connection.prepareStatement("DELETE FROM activities").executeUpdate();
        connection.prepareStatement("DELETE FROM advertisers").executeUpdate();
        connection.prepareStatement("DELETE FROM clients").executeUpdate();
        connection.prepareStatement("DELETE FROM bookings").executeUpdate();

        connection.prepareStatement("INSERT INTO clients VALUES ('RSSMRA00A00A000A', 'Mario', 'Rossi')").executeUpdate();
        connection.prepareStatement("INSERT INTO advertisers VALUES (1, 0, 'Get Your Guide', 100, null, null)").executeUpdate();
        connection.prepareStatement("INSERT INTO activities VALUES (1, 'Test Title', 'Test Description', 'Test Type',1, 'Test Address', 'Test City', 1, 50)").executeUpdate();


    }

    //Test per estrarre un cliente dal database
    @Test
    public void getClientTest() throws Exception {
        Client client = clientDao.get("RSSMRA00A00A000A");
        assertEquals("RSSMRA00A00A000A", client.getFiscalCode());
        assertEquals("Mario", client.getName());
        assertEquals("Rossi", client.getLastName());
    }


}
