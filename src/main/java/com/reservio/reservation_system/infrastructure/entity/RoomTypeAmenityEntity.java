package com.reservio.reservation_system.infrastructure.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "ROOM_TYPE_AMENITIES")
public class RoomTypeAmenityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROOM_TYPE_AMENITIES_id_gen")
    @SequenceGenerator(name = "ROOM_TYPE_AMENITIES_id_gen", sequenceName = "ROOM_TYPE_AMENITY_SEQ", allocationSize = 1)
    @Column(name = "RTA_ID", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "RT_ID", nullable = false)
    private RoomTypeEntity rt;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "AMN_ID", nullable = false)
    private AmenityEntity amn;

}