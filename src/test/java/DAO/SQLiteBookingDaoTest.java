package DAO;

import DomainModel.Activity;
import DomainModel.Booking;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;

public class SQLiteBookingDaoTest {

    private BookingDao bookingDao;

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        // Set up database
        Database.setDatabase("main.db");
        Database.initDatabase();
    }

    @BeforeEach
    public void init() throws Exception {
        Connection connection = Database.getConnection();
        bookingDao=new SQLiteBookingDao();

        connection.prepareStatement("DELETE FROM activities").executeUpdate();
        connection.prepareStatement("DELETE FROM advertisers").executeUpdate();
        connection.prepareStatement("DELETE FROM clients").executeUpdate();
        connection.prepareStatement("DELETE FROM bookings").executeUpdate();

        connection.prepareStatement("INSERT INTO clients VALUES ('RSSMRA00A00A000A', 'Mario', 'Rossi')").executeUpdate();
        connection.prepareStatement("INSERT INTO advertisers VALUES (1, 0, 'Get Your Guide', 100, null, null)").executeUpdate();
        connection.prepareStatement("INSERT INTO activities VALUES (1, 'Test Title', 'Test Description', 'Test Type',1, 'Test Address', 'Test City', 1, 50)").executeUpdate();


    }

    @Test
    public void insertBookingTest() throws Exception {
        Booking bookingTest = new Booking(1, LocalDate.of(2021, 1, 1), LocalTime.of(10, 0), 1, "RSSMRA00A00A000A");
        bookingDao.insert(bookingTest);

        Booking booking = bookingDao.get(1);
        assertEquals(1, booking.getId());
        assertEquals(LocalDate.of(2021, 1, 1), booking.getDate());
        assertEquals(LocalTime.of(10, 0), booking.getTime());
        assertEquals(1, booking.getAdId());
        assertEquals("RSSMRA00A00A000A", booking.getClientId());

    }


}
