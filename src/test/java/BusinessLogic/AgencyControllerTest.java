package BusinessLogic;

import DAO.*;
import DomainModel.Agency;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AgencyControllerTest {
    ActivityDao activityDao = new SQLiteActivityDao();
    ClientDao clientDao=new SQLiteClientDao();
    AdvertiserDao advertiserDao=new SQLiteAdvertiserDao();
    BookingDao bookingDao=new SQLiteBookingDao();
    ActivityController activityController;
    ClientController clientController;
    AgencyController agencyController;
    PrivateController privateController;
    BookingController bookingController;

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        // Set up database
        Database.setDatabase("Main.db");
        Database.initDatabase();
    }

    @BeforeEach
    public void init() throws Exception {
        resetDatabase();

        resetDatabase();

        // Controllers
        activityController = new ActivityController(activityDao);
        clientController = new ClientController(clientDao, activityController);
        agencyController = new AgencyController(advertiserDao);
        privateController = new PrivateController(advertiserDao);
        bookingController = new BookingController(bookingDao);

        clientController.addClient("Mario", "Rossi", "RSSMRA00A00A000A");
        clientController.addClient("Stefano", "Bicchi", "BNCMRC00A00A000A");
        clientController.addClient("Antonio", "Neri", "NRIFNC00A00A000A");
        agencyController.addAgency(1, "Get your guide", 100);
        privateController.addPrivate(2,"Giuseppe", "Gialli");
        activityController.createActivity("Test Title", "Test Description", "Test Type",1,  "Test Address", "Test City", 1,10);
        activityController.createActivity("Test Title", "Test Description", "Test Type",2,  "Test Address", "Test City", 1,20);
        activityController.createActivity("Test Title", "Test Description", "Test Type",1,  "Test Address2", "Test City", 1,10);
        activityController.createActivity("Test Title", "Test Description", "Test Type2",2,  "Test Address", "Test City", 1,50);

    }

    private void resetDatabase() throws SQLException {
        Connection connection = Database.getConnection();

        // Delete data from all tables
        List<String> tables = Arrays.asList("activities", "clients", "advertisers", "favorites", "bookings");
        for (String table : tables) {
            try {
                connection.prepareStatement("DELETE FROM " + table).executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        connection.prepareStatement("DELETE FROM activities").executeUpdate();
        Database.closeConnection(connection);
    }

    @Test
    public void insertAgencyTest() throws Exception {
        agencyController.addAgency(6, "Agenzia Test 1", 100);
        assertEquals(6, agencyController.getAgency(6).getId());
    }

    @Test
    public void getEstateAgenciesTest(){
        agencyController.addAgency(6, "Agenzia test 1", 100);
        agencyController.addAgency(7, "Agenzia test 2", 1000);
        agencyController.addAgency(8, "Agenzia test 3", 500);
        Agency agencies = agencyController.getAgency(7);
        assertEquals(7, agencies.getId());
        assertEquals(1000, agencies.getAgencyCosts());
    }

    @Test
    public void getAllAgenciesTest(){
        agencyController.addAgency(6, "Agenzia test 1", 100);
        agencyController.addAgency(7, "Agenzia test 2", 1000);
        agencyController.addAgency(8, "Agenzia test 3", 500);
        assertEquals(4, agencyController.getAllAgencies().length);
    }
}
