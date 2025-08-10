package com.reservio.reservation_system.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "PAYMENT_METHODS")
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAYMENT_METHODS_id_gen")
    @SequenceGenerator(name = "PAYMENT_METHODS_id_gen", sequenceName = "PAYMENT_METHOD_SEQ", allocationSize = 1)
    @Column(name = "PMTM_ID", nullable = false)
    private Long id;

    @Column(name = "PMTM_NAME", nullable = false, length = 20)
    private String pmtmName;

    @OneToMany(mappedBy = "pmtm")
    private Set<Payment> payments = new LinkedHashSet<>();

}