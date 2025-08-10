package com.reservio.reservation_system.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "ROOM_AMENITIES")
public class RoomAmenity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROOM_AMENITIES_id_gen")
    @SequenceGenerator(name = "ROOM_AMENITIES_id_gen", sequenceName = "ROOM_AMENITY_SEQ", allocationSize = 1)
    @Column(name = "RA_ID", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "RM_ID", nullable = false)
    private Room rm;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "AMN_ID", nullable = false)
    private Amenity amn;

}