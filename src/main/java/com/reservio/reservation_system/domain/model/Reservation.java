package com.reservio.reservation_system.domain.model;

import ch.qos.logback.core.model.INamedModel;
import com.reservio.reservation_system.infrastructure.entity.RoomEntity;

import java.util.Date;

public class Reservation {
    private Long id;
    private Date checkInDate;
    private Date checkOutDate;
    private Integer guestCount;
    private Room room;
    private User user;
}
