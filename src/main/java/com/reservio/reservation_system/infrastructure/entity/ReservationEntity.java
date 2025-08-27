package com.reservio.reservation_system.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "RESERVATIONS")
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RESERVATIONS_id_gen")
    @SequenceGenerator(name = "RESERVATIONS_id_gen", sequenceName = "RESERVATION_SEQ", allocationSize = 1)
    @Column(name = "RSV_ID", nullable = false)
    private Long id;

    @Column(name = "RSV_CHECK_IN_DATE", nullable = false)
    private LocalDateTime  rsvCheckInDate;

    @Column(name = "RSV_CHECK_OUT_DATE", nullable = false)
    private LocalDateTime  rsvCheckOutDate;

    @Column(name = "RSV_GUEST_COUNT")
    private Short rsvGuestCount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "RM_ID", nullable = false)
    private RoomEntity rm;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "USR_ID", nullable = false)
    private UserEntity usr;


}