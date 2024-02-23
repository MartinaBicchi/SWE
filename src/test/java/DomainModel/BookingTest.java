package DomainModel;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import static org.junit.jupiter.api.Assertions.assertThrows;



class BookingTest {
    //Test che controlla che l'inserimento della data sia corretto e in caso contrario lancia un eccezione
    @Test
    public void testInvalidDate() {
        String invalidDate = "2024-02-30"; // Data con giorno non valido per febbraio
        LocalTime validTime = LocalTime.of(12, 0);
        int validAdId = 1;
        String validClientId = "ABC123";

        assertThrows(DateTimeParseException.class, () -> {
            LocalDate.parse(invalidDate);
            new Booking(1, LocalDate.parse(invalidDate), validTime, validAdId, validClientId);
        });
    }
    //Test che controlla che l'inserimento dell'ora sia corretto e in caso contrario lancia un eccezione
    @Test
    public void testInvalidTime() {
        String validDate = "2024-05-16"; // Data valida per maggio
        String invalidTime = "25:00"; // Orario non valido (25 ore)
        int validAdId = 1;
        String validClientId = "ABC123";

        assertThrows(DateTimeParseException.class, () -> {
            LocalDate.parse(validDate);
            LocalTime.parse(invalidTime);
            new Booking(1, LocalDate.parse(validDate), LocalTime.parse(invalidTime), validAdId, validClientId);
        });
    }
}
