package com.reservio.reservation_system.domain.repository;

import com.reservio.reservation_system.infrastructure.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationDao extends JpaRepository<ReservationEntity, Long> {
    @Query("""
            
                    SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
            FROM ReservationEntity r
                WHERE r.rm.id = :roomId
                        AND r.rsvCheckInDate < :endDate
                        AND r.rsvCheckOutDate > :startDate
                        AND r.rsvs.rsvsName IN ('Pending', 'Confirmed', 'Partial-paid', 'Paid')    \s""")
    boolean existsOverlappingReservation(@Param("roomId") Long roomId,
                                         @Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);

    List<ReservationEntity> findAllByRmIdAndRsvCheckOutDateAfter(Long roomId, LocalDateTime now);

    List<ReservationEntity> findAllByRmIdAndRsvCheckInDateLessThanEqualAndRsvCheckOutDateGreaterThanEqual
            (Long roomId, LocalDateTime start, LocalDateTime end);

    List<ReservationEntity> findAllByRsvCheckInDateLessThanEqualAndRsvCheckOutDateGreaterThanEqual(
            LocalDateTime end, LocalDateTime start);

    List<ReservationEntity> findAllByUsrIdAndRsvCheckOutDateAfter(Long usrId, LocalDateTime now);

    @Query("""
            SELECT r FROM ReservationEntity r
            WHERE r.usr.id = :usrId
                    AND r.rsvCheckOutDate > :now
                    AND r.rsvs.rsvsName NOT IN ('Cancelled', 'Rejected', 'Completed')
            """)
    List<ReservationEntity> findCurrentReservations(Long usrId, LocalDateTime now);

    List<ReservationEntity> findAllByUsrId(Long usrId);

    @Query("SELECT r FROM ReservationEntity r " +
            "JOIN r.payments p " +
            "JOIN p.pmts s " +
            "WHERE r.rsvCheckOutDate > :startDateTime " +
            "AND r.rsvCheckInDate < :endDateTime " +
            "AND s.pmtsName = 'PAID'")
    List<ReservationEntity> findReservaitonsWithApprovedPaymentsInPeriod(
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime
    );

    @Query("""
            SELECT r FROM ReservationEntity r
            WHERE (:from IS NULL OR r.rsvCheckInDate >= :from OR :from IS NULL)
              AND (:to IS NULL OR r.rsvCheckOutDate <= :to OR :to IS NULL)
              AND (:email IS NULL OR LOWER(r.usr.usrEmail) LIKE LOWER(CONCAT('%', :email, '%')))
              AND (:phone IS NULL OR r.usr.usrPhoneNumber LIKE CONCAT('%', :phone, '%'))
            """)
    List<ReservationEntity> findAllFiltered(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("phone") String phone,
            @Param("email") String email);

    @Query("""
                SELECT r FROM ReservationEntity r
                WHERE r.rsvs.rsvsName IN ('Pending', 'Partial-Paid', 'Paid')
                  AND (:from IS NULL OR r.rsvCheckInDate >= :from OR :from IS NULL)
                  AND (:to IS NULL OR r.rsvCheckOutDate <= :to OR :to IS NULL)
                  AND (:email IS NULL OR LOWER(r.usr.usrEmail) LIKE LOWER(CONCAT('%', :email, '%')))
                  AND (:phone IS NULL OR r.usr.usrPhoneNumber LIKE CONCAT('%', :phone, '%'))
            """)
    List<ReservationEntity> findPendingFiltered(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("phone") String phone,
            @Param("email") String email);


    @Query("""
                SELECT r FROM ReservationEntity r
                WHERE r.rsvCheckInDate < :end
                  AND r.rsvCheckOutDate > :start
            """)
    List<ReservationEntity> findAllByCheckInOutDates(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
                SELECT COUNT(r) FROM ReservationEntity r
                WHERE r.createdAt >= :start
                  AND r.createdAt < :end
            """)
    long countCreatedToday(@Param("start") LocalDateTime start,
                           @Param("end") LocalDateTime end);

    @Query("SELECT r FROM ReservationEntity r " +
            "WHERE r.id = :reservationId AND r.usr.id = :userId")
    Optional<ReservationEntity> findByIdAndUserId(
            @Param("reservationId") Long reservationId,
            @Param("userId") Long userId
    );
}
