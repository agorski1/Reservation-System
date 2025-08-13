package com.reservio.reservation_system.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "PAYMENTS")
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAYMENTS_id_gen")
    @SequenceGenerator(name = "PAYMENTS_id_gen", sequenceName = "PAYMENT_SEQ", allocationSize = 1)
    @Column(name = "PMT_ID", nullable = false)
    private Long id;

    @Column(name = "PMT_AMOUNT", nullable = false, precision = 10, scale = 2)
    private BigDecimal pmtAmount;

    @ColumnDefault("SYSDATE")
    @Column(name = "PMT_DATE")
    private LocalDate pmtDate;

    @Column(name = "PMT_ACCOUNT_NUMBER", length = 34)
    private String pmtAccountNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "RSV_ID", nullable = false)
    private ReservationEntity rsv;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "PMTM_ID", nullable = false)
    private PaymentMethodEntity pmtm;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "PMTS_ID", nullable = false)
    private PaymentStatusEntity pmts;

}