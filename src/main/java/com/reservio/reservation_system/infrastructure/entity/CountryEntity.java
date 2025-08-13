package com.reservio.reservation_system.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "COUNTRIES")
public class CountryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COUNTRIES_id_gen")
    @SequenceGenerator(name = "COUNTRIES_id_gen", sequenceName = "COUNTRY_SEQ", allocationSize = 1)
    @Column(name = "CT_ID", nullable = false)
    private Long id;

    @Column(name = "CT_NAME", nullable = false, length = 50)
    private String ctName;

    @OneToMany(mappedBy = "ct")
    private Set<UserEntity> users = new LinkedHashSet<>();

}