package com.reservio.reservation_system.domain.repository;

import com.reservio.reservation_system.infrastructure.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationDao extends JpaRepository<ReservationEntity, Long> {
    @Query("""
       SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
       FROM ReservationEntity r
       WHERE r.rm.id = :roomId
         AND r.rsvCheckInDate < :endDate
         AND r.rsvCheckOutDate > :startDate
       """)
    boolean existsOverlappingReservation(@Param("roomId") Long roomId,
                                         @Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);
    List<ReservationEntity> findAllByRmIdAndRsvCheckOutDateAfter(Long roomId, LocalDateTime now);

    List<ReservationEntity> findAllByRmIdAndRsvCheckInDateLessThanEqualAndRsvCheckOutDateGreaterThanEqual
            (Long roomId, LocalDateTime start, LocalDateTime end);

    List<ReservationEntity> findAllByRsvCheckInDateLessThanEqualAndRsvCheckOutDateGreaterThanEqual(
            LocalDateTime end, LocalDateTime start);

    List<ReservationEntity> findAllByUsrIdAndRsvCheckOutDateAfter(Long usrId, LocalDateTime now);

    List<ReservationEntity> findAllByUsrId(Long usrId);

    @Query("SELECT r FROM ReservationEntity r " +
            "JOIN r.payments p " +
            "JOIN p.pmts s " +
            "WHERE r.rsvCheckOutDate > :startDate " +
            "AND r.rsvCheckInDate < :endDate " +
            "AND s.pmtsName = 'PAID'")
    List<ReservationEntity> findReservaitonsWithApprovedPaymentsInPeriod(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
            );

    @Query("""
        SELECT r FROM ReservationEntity r
        WHERE (:from IS NULL OR r.rsvCheckInDate >= :from)
          AND (:to IS NULL OR r.rsvCheckOutDate <= :to)
          AND (:email IS NULL OR LOWER(r.usr.usrEmail) LIKE LOWER(CONCAT('%', :email, '%')))
          AND (:phone IS NULL OR r.usr.usrPhoneNumber LIKE CONCAT('%', :phone, '%'))
    """)
    List<ReservationEntity> findAllFiltered(@Param("from") LocalDateTime from,
                                            @Param("to") LocalDateTime to,
                                            @Param("email") String email,
                                            @Param("phone") String phone);

    @Query("""
        SELECT r FROM ReservationEntity r
        WHERE r.rsvs.rsvsName = 'PENDING'
          AND (:from IS NULL OR r.rsvCheckInDate >= :from)
          AND (:to IS NULL OR r.rsvCheckOutDate <= :to)
          AND (:email IS NULL OR LOWER(r.usr.usrEmail) LIKE LOWER(CONCAT('%', :email, '%')))
          AND (:phone IS NULL OR r.usr.usrPhoneNumber LIKE CONCAT('%', :phone, '%'))
    """)
    List<ReservationEntity> findPendingFiltered(@Param("from") LocalDateTime from,
                                                @Param("to") LocalDateTime to,
                                                @Param("email") String email,
                                                @Param("phone") String phone);


    @Query("""
    SELECT r FROM ReservationEntity r
    WHERE r.rsvCheckInDate < :end
      AND r.rsvCheckOutDate > :start
""")
    List<ReservationEntity> findAllByCheckInOutDates(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}

