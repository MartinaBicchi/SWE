package DomainModel.search;


import BusinessLogic.ActivityController;
import BusinessLogic.AgencyController;
import BusinessLogic.ClientController;
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

import static org.junit.jupiter.api.Assertions.*;


class SearchConcreteTest {

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        // Set up database
        Database.setDatabase("main.db");
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
        Connection connection = Database.getConnection("main.db");

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

    //Test che verifica il corretto funzionamento del metodo SearchActivity()
    @Test
    void searchActivity() throws SQLException {
        Activity[] activities=new SearchConcrete("Test Type2").searchActivity();
        assertEquals(1, activities.length);
        assertEquals("Test Title", activities[0].getTitle());
        assertEquals("Test Description", activities[0].getDescription());
        assertEquals("Test Address", activities[0].getAddress());
        assertEquals("Test City", activities[0].getCity());
        assertEquals(1, activities[0].getDuration());
        assertEquals(300, activities[0].getPrice());
        assertEquals(1, activities[0].getAdvertiserId());
    }

}

