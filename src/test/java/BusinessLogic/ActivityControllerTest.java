package BusinessLogic;

import DAO.*;
import DomainModel.Activity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ActivityControllerTest {
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
        Database.setDatabase("main.db");
        Database.initDatabase();
    }

    @BeforeEach
    public void init() throws Exception {
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
        Connection connection = Database.getConnection("main.db");

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

    //Test per verificare che un'attività sia effettivamente aggiunto ai preferiti del cliente
    @Test
    public void addFavouriteTest() throws SQLException {
        activityController.addToFavourites("RSSMRA00A00A000A", 1);
        activityController.addToFavourites("RSSMRA00A00A000A", 2);

        Activity[] activities = activityController.getFavouriteAds("RSSMRA00A00A000A");
        assertEquals(2, activities.length);
        assertEquals(10, activities[0].getPrice());
        assertEquals(20, activities[1].getPrice());
    }

    //Test per verificare che un'attività sia correttamente eliminata dai preferiti di un cliente
    @Test
    public void removeFavouriteTest() throws SQLException {
        activityController.addToFavourites("RSSMRA00A00A000A", 1);
        activityController.addToFavourites("RSSMRA00A00A000A", 2);
        activityController.removeFromFavourites("RSSMRA00A00A000A", 2);

        Activity[] activities = activityController.getFavouriteAds("RSSMRA00A00A000A");
        assertEquals(1, activities.length);
        assertEquals(10, activities[0].getPrice());
    }

    //Test per verificare che mi restituisca correttamente la lista di attività associati a uno specifico inserzionista
    @Test
    public void getActivitiesByAdvertiserTest() throws SQLException {
        Activity[] activities = activityController.getActivityForAdvertiser(2);
        assertEquals(2, activities.length);
        assertEquals(20, activities[0].getPrice());
        assertEquals(50, activities[1].getPrice());
    }

    @Test
    public void observerNotificationTest (){
        activityController . addToFavourites ( " RSSMRA00A00A000A " , 1);
        activityController . addToFavourites ( " RSSMRA00A00A000A " , 2);
        activityController . deleteActivity (2);
        assertEquals (1 , activityController . getFavouriteAds

                ( " RSSMRA00A00A000A " ). length );

    }



}
