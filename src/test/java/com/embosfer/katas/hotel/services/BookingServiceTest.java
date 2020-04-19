package com.embosfer.katas.hotel.services;

import com.embosfer.katas.hotel.model.Booking;
import com.embosfer.katas.hotel.model.BookingFailure;
import com.embosfer.katas.hotel.model.EmployeeId;
import com.embosfer.katas.hotel.model.HotelId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static com.embosfer.katas.hotel.model.BookingFailure.Reason.BAD_DATES;
import static com.embosfer.katas.hotel.model.BookingFailure.Reason.UNKNOWN_HOTEL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock HotelService hotelService;
    @Mock DatesValidator datesValidator;
    BookingService bookingService;

    LocalDate checkIn = LocalDate.EPOCH;
    LocalDate checkOut = LocalDate.EPOCH;
    HotelId hotelId = HotelId.of("a-hotel");

    @BeforeEach
    void setUp() {
        bookingService = new BookingService(hotelService, datesValidator);
    }

    @Test
    void returnsBookingFailureIfHotelDoesNotExist() {

        when(datesValidator.validate(any(), any())).thenReturn(true);
        when(hotelService.findHotelBy(hotelId)).thenReturn(Optional.empty());

        Booking bookingResult = bookingService.book(EmployeeId.of(123), hotelId, null, checkIn, checkOut);

        assertThatIsBookingFailureOf(bookingResult, UNKNOWN_HOTEL);
    }

    @Test
    void returnsBookingFailureForIncorrectDates() {

        when(datesValidator.validate(checkIn, checkOut)).thenReturn(false);

        Booking bookingResult = bookingService.book(null, hotelId, null, checkIn, checkIn);

        assertThatIsBookingFailureOf(bookingResult, BAD_DATES);
    }

    private void assertThatIsBookingFailureOf(Booking bookingResult, BookingFailure.Reason unknownHotel) {
        assertThat(bookingResult).isInstanceOf(BookingFailure.class);
        BookingFailure failure = (BookingFailure) bookingResult;
        assertThat(failure.reason()).isEqualTo(unknownHotel);
    }

}