package com.reservio.reservation_system.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "USER_ROLES")
public class UserRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_ROLES_id_gen")
    @SequenceGenerator(name = "USER_ROLES_id_gen", sequenceName = "USER_ROLE_SEQ", allocationSize = 1)
    @Column(name = "UR_ID", nullable = false)
    private Long id;

    @Column(name = "UR_NAME", nullable = false, length = 20)
    private String urName;

    @OneToMany(mappedBy = "ur")
    private Set<UserEntity> users = new LinkedHashSet<>();

}