package BusinessLogic;

import DAO.BookingDao;
import DomainModel.Booking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class BookingController {
    private BookingDao bookingdao;

    public BookingController(BookingDao dao) {
        this.bookingdao = dao;
    }

    public boolean checkAvailability(Booking booking) {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        if (booking.getDate().isBefore(currentDate) || (booking.getDate().isEqual(currentDate) && booking.getTime().isBefore(currentTime))) {
            System.out.println("La data o l'ora dell'appuntamento sono precedenti a quelli attuali");
            return false;
        }
        List<Booking> allBookings=new ArrayList<>();
        try {
            allBookings = bookingdao.getAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (Booking existingBooking : allBookings) {
            if (existingBooking.getDate().equals(booking.getDate()) &&
                    existingBooking.getTime().equals(booking.getTime()) &&
                    existingBooking.getAdId() == booking.getAdId() ) {
                return false; // Prenotazione già esistente per la stessa data, orario e annuncio
            }
        }
        return true; // Nessuna prenotazione esistente per la stessa data e orario
    }

    public Booking createBooking(LocalDate date, LocalTime time, int adId, String clientFiscalCode) throws Exception {
        // Implement the logic to create a new booking if it's available and save it to the database
        Booking newBooking = new Booking(bookingdao.getNextBookingId(), date, time, adId, clientFiscalCode);
        if (checkAvailability(newBooking)) {
            bookingdao.insert(newBooking);
            return newBooking;
        } else {
            // Handle unavailable booking
            System.out.println("Prenotazione non disponibile. Selezionare un'altra data e/o orario.");
            return null;
        }

    }

    public void deleteBooking(int bookingId) {
        // Delete the booking with the given ID from the database
        try {
            bookingdao.remove(bookingId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("Prenotazione rimossa");
    }

    public Booking[] getAllBookingsForAdvertiser(int idAdvertiser) {
        // Get all bookings associated with the given advertiser ID from the database
        return bookingdao.getBookingByAdvertiser(idAdvertiser);
    }

    public List<Booking> getAllBookings() {
        // Get all bookings from the database
        List<Booking> all=new ArrayList<>();
        try {
            all= bookingdao.getAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return all;
    }

    public void modifyDate(Booking booking, LocalDate date) {
        // Modify the date of the given booking
        booking.setDate(date);
        bookingdao.updateBookingDate(booking, date);
        System.out.println("Prenotazione modificata. Nuova data: " + date.toString() );
    }

    public void modifyTime(Booking booking, LocalTime time) {
        // Modify the time of the given booking
        booking.setTime(time);
        bookingdao.updateBookingTime(booking, time);
        System.out.println("Prenotazione modificata. Nuovo orario: " + time.toString() );
    }
}