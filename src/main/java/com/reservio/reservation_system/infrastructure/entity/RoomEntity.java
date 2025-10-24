package com.reservio.reservation_system.infrastructure.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "ROOMS")
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROOMS_id_gen")
    @SequenceGenerator(name = "ROOMS_id_gen", sequenceName = "ROOM_SEQ", allocationSize = 1)
    @Column(name = "RM_ID", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "RM_NUMBER", nullable = false)
    private Short rmNumber;

    @Size(max = 20)
    @ColumnDefault("'ACTIVE'")
    @Column(name = "RM_STATUS", length = 20)
    private String rmStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "RT_ID")
    private RoomTypeEntity rt;

    @OneToMany
    @JoinColumn(name = "RM_ID")
    private Set<ReservationEntity> reservations = new LinkedHashSet<>();

}