package com.reservio.reservation_system.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "USERS")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERS_id_gen")
    @SequenceGenerator(name = "USERS_id_gen", sequenceName = "USER_SEQ", allocationSize = 1)
    @Column(name = "USR_ID", nullable = false)
    private Long id;

    @Column(name = "USR_EMAIL", nullable = false, length = 100)
    private String usrEmail;

    @Column(name = "USR_PASSWORD", nullable = false, length = 50)
    private String usrPassword;

    @Column(name = "USR_FIRST_NAME", length = 100)
    private String usrFirstName;

    @Column(name = "USR_LAST_NAME", length = 100)
    private String usrLastName;

    @Column(name = "USR_PHONE_NUMBER", length = 20)
    private String usrPhoneNumber;

    @ColumnDefault("SYSDATE")
    @Column(name = "USR_REGISTRATION_DATE")
    private LocalDate usrRegistrationDate;

    @Column(name = "USR_STREET")
    private String usrStreet;

    @Column(name = "USR_CITY", length = 100)
    private String usrCity;

    @Column(name = "USR_ZIP_CODE", length = 20)
    private String usrZipCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "UR_ID")
    private UserRoleEntity ur;


    @OneToMany(mappedBy = "usr")
    private Set<ReservationEntity> reservations = new LinkedHashSet<>();

}