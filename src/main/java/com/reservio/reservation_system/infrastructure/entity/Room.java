package com.reservio.reservation_system.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "ROOMS")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROOMS_id_gen")
    @SequenceGenerator(name = "ROOMS_id_gen", sequenceName = "ROOM_SEQ", allocationSize = 1)
    @Column(name = "RM_ID", nullable = false)
    private Long id;

    @Column(name = "RM_NUMBER", nullable = false)
    private Short rmNumber;

    @ColumnDefault("'N'")
    @Column(name = "RM_IS_DELETED")
    private Boolean rmIsDeleted;

    @Column(name = "RM_PRICE_PER_NIGHT", precision = 10, scale = 2)
    private BigDecimal rmPricePerNight;

    @Column(name = "RM_CAPACITY")
    private Short rmCapacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "RT_ID")
    private RoomType rt;

    @OneToMany(mappedBy = "rm")
    private Set<Reservation> reservations = new LinkedHashSet<>();

    @OneToMany(mappedBy = "rm")
    private Set<RoomAmenity> roomAmenities = new LinkedHashSet<>();

}