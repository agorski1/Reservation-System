package com.reservio.reservation_system.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "PAYMENT_STATUSES")
public class PaymentStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAYMENT_STATUSES_id_gen")
    @SequenceGenerator(name = "PAYMENT_STATUSES_id_gen", sequenceName = "PAYMENT_STATUS_SEQ", allocationSize = 1)
    @Column(name = "PMTS_ID", nullable = false)
    private Long id;

    @Column(name = "PMTS_NAME", nullable = false, length = 20)
    private String pmtsName;

    @OneToMany(mappedBy = "pmts")
    private Set<Payment> payments = new LinkedHashSet<>();

}