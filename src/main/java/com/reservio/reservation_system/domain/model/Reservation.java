package com.reservio.reservation_system.domain.model;

import java.util.Date;

public class Reservation {
    private Long id;
    private Date checkInDate;
    private Date checkOutDate;
    private Integer guestCount;
    private Room room;
    private User user;
}
