package DomainModel.search;

import BusinessLogic.ActivityController;
import BusinessLogic.AgencyController;
import BusinessLogic.ClientController;
import DAO.*;
import DomainModel.Activity;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DecoratorSearchTest {
    ActivityController activityController;
    ClientController clientController;
    AgencyController agencyController;

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        // Set up database
        Database.setDatabase("Main.db");
        Database.initDatabase();
    }

    @BeforeEach //metodo di inizializzazione che viene inserito prima di tutti i test
    public void init() throws Exception {
        resetDatabase();

        // DAOs
        ActivityDao activityDao = new SQLiteActivityDao();
        ClientDao clientDao=new SQLiteClientDao();
        AdvertiserDao advertiserDao=new SQLiteAdvertiserDao();

        // Controllers
        ActivityController activityController = new ActivityController(activityDao);
        ClientController clientController = new ClientController(clientDao, activityController);
        AgencyController agencyController = new AgencyController(advertiserDao);

        clientController.addClient("Gianni", "Rossi", "RSSMRA00A00A000A");
        agencyController.addAgency(1, "Get Your Guide", 100);
        activityController.createActivity("Test Title", "Test Description", "Test Type",1, "Test Address", "Test City", 1, 50 );
        activityController.createActivity("Test Title", "Test Description", "Test Type",1, "Test Address", "Test City3", 1, 100 );
        activityController.createActivity("Test Title", "Test Description", "Test Type",1, "Test Address3", "Test City", 1, 200 );
        activityController.createActivity("Test Title", "Test Description", "Test Type2",1, "Test Address", "Test City", 1, 300 );

    }

    private void resetDatabase() throws SQLException {
        Connection connection = Database.getConnection("Main.db");

        // Delete data from all tables
        List<String> tables = Arrays.asList("activities", "clients", "advertisers");
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

    //Test per verificare il corretto funzionamento dei decoratori
    @Test
    public void testSearchActivityForPrice() {
        SearchConcrete baseSearch = new SearchConcrete("Test Type");
        DecoratorSearchPrice search = new DecoratorSearchPrice(baseSearch, 500);

        Activity[] activities = search.searchActivity();

        assertEquals(3, activities.length);
        assertEquals(50, activities[0].getPrice());
        assertEquals(100, activities[1].getPrice());
        assertEquals(200, activities[2].getPrice());
    }

    @Test
    public void testSearchActivityForAddress() {
        SearchConcrete baseSearch = new SearchConcrete("Test Type");
        DecoratorSearchAddress search = new DecoratorSearchAddress(baseSearch, "Test Address");

        Activity[] activities = search.searchActivity();

        assertEquals(2, activities.length);
        assertEquals(50, activities[0].getPrice());
        assertEquals(100, activities[1].getPrice());
    }

    @Test
    public void testSearchActivityForCity() {
        SearchConcrete baseSearch = new SearchConcrete("Test Type");
        DecoratorSearchCity search = new DecoratorSearchCity(baseSearch, "Test City");

        Activity[] activities = search.searchActivity();

        assertEquals(2, activities.length);
        assertEquals("Test Address", activities[0].getAddress());
        assertEquals("Test Address3", activities[1].getAddress());
    }

    @Test
    public void testSearchActivityForAll() {
        Activity[] activities=new DecoratorSearchPrice(
                new DecoratorSearchCity(
                        new DecoratorSearchAddress(
                                new SearchConcrete("Test Type"), "Test Address"),
                        "Test City3"), 100).searchActivity();

        assertEquals(1, activities.length);
        assertEquals("Test Title", activities[0].getTitle());
        assertEquals("Test Description", activities[0].getDescription());
        assertEquals(1, activities[0].getAdvertiserId());
        assertEquals(1, activities[0].getDuration());
        assertEquals(100, activities[0].getPrice());

    }
}
