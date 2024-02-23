package BusinessLogic;

import DAO.*;
import DomainModel.Booking;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BookingControllerTest {
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

    //Test verifica la corretta gestione nel caso sia presente una sovrapposizione delle prenotazioni
    @Test
    public void checkOverlapBookingTest() throws Exception {
        LocalDate booking1= LocalDate.parse("2023-09-12");
        LocalTime time1= LocalTime.parse("15:00");
        LocalTime time2 = LocalTime.parse("15:10");

        bookingController.createBooking(booking1, time1, 1, "RSSMRA00A00A000A");
        Booking over= bookingController.createBooking(booking1, time2, 1, "BNCMRC00A00A000A");

        assertNull(over);
    }

    //Test che verifichi la corretta eliminazione di una prenotazione
    @Test
    public void deleteBookingTest() throws Exception {
        LocalDate booking1= LocalDate.parse("2023-09-12");
        LocalTime time1= LocalTime.parse("15:00");

        bookingController.createBooking(booking1, time1, 1, "RSSMRA00A00A000A");
        bookingController.deleteBooking(1);

        Booking[] bookings = bookingController.getAllBookings().toArray(new Booking[0]);
        assertEquals(0, bookings.length);
    }



    @Test
    public void invalidBookingDateOrTime() throws Exception {
        LocalDate booking1= LocalDate.parse("1996-07-27");
        LocalTime time1= LocalTime.parse("15:00");

        Booking testBooking = bookingController.createBooking(booking1, time1, 1, "RSSMRA00A00A000A");
        assertNull(testBooking);
    }

}
