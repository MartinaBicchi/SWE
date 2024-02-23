package DAO;

import DomainModel.Booking;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public interface BookingDao extends DAO <Booking, Integer> {

    Booking[] getBookingByAdvertiser(int idAdvertiser);

    Booking[] getBookingByActivity(int idAdvertiser);

    void updateBookingDate(Booking booking, LocalDate newDate);

    void updateBookingTime(Booking booking, LocalTime newTime);

    public Integer getNextBookingId() throws SQLException;


}