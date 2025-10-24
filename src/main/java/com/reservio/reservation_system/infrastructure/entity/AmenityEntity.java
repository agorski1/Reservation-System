package com.reservio.reservation_system.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "AMENITIES")
public class AmenityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMENITIES_id_gen")
    @SequenceGenerator(name = "AMENITIES_id_gen", sequenceName = "AMENITY_SEQ", allocationSize = 1)
    @Column(name = "AMN_ID", nullable = false)
    private Long id;

    @Column(name = "AMN_NAME", nullable = false, length = 50)
    private String amnName;

    @Column(name = "AMN_CODE", nullable = false, length = 20)
    private String amnCode;

    @OneToMany(mappedBy = "amn")
    private Set<RoomTypeAmenityEntity> roomAmenities = new LinkedHashSet<>();

}