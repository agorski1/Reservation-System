package com.reservio.reservation_system.infrastructure.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "RESERVATION_STATUSES")
public class ReservationStatusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RESERVATION_STATUSES_id_gen")
    @SequenceGenerator(name = "RESERVATION_STATUSES_id_gen", sequenceName = "RESERVATION_STATUS_SEQ", allocationSize = 1)
    @Column(name = "RSVS_ID", nullable = false)
    private Long id;

    @Size(max = 30)
    @NotNull
    @Column(name = "RSVS_NAME", nullable = false, length = 30)
    private String rsvsName;

}