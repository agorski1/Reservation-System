package com.reservio.reservation_system.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ROOM_TYPES")
public class RoomTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROOM_TYPES_id_gen")
    @SequenceGenerator(name = "ROOM_TYPES_id_gen", sequenceName = "ROOM_TYPE_SEQ", allocationSize = 1)
    @Column(name = "RT_ID", nullable = false)
    private Long id;

    @Column(name = "RT_NAME", nullable = false, length = 20)
    private String rtName;

}