package com.reservio.reservation_system.infrastructure.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "ROOM_TYPE")
public class RoomTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROOM_TYPE_id_gen")
    @SequenceGenerator(name = "ROOM_TYPE_id_gen", sequenceName = "ROOM_TYPE_SEQ", allocationSize = 1)
    @Column(name = "RT_ID", nullable = false)
    private Long id;

    @Size(max = 40)
    @NotNull
    @Column(name = "RT_NAME", nullable = false, length = 40)
    private String rtName;

    @Column(name = "RT_CAPACITY")
    private Long rtCapacity;

    @Column(name = "RT_PRICE_PER_NIGHT", precision = 10, scale = 2)
    private BigDecimal rtPricePerNight;

    @Size(max = 200)
    @Column(name = "RT_DESCRIPTION", length = 200)
    private String rtDescription;

    @OneToMany(mappedBy = "rt")
    private Set<RoomTypeAmenityEntity> roomTypeAmenities = new LinkedHashSet<>();

}