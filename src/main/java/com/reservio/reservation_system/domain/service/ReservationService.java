package com.reservio.reservation_system.domain.service;

import com.reservio.reservation_system.domain.exception.ReservationException;
import com.reservio.reservation_system.domain.repository.*;
import com.reservio.reservation_system.infrastructure.entity.*;
import com.reservio.reservation_system.presentation.dto.reservation.UserReservationDto;
import com.reservio.reservation_system.presentation.dto.reservation.ReservationDto;
import com.reservio.reservation_system.presentation.dto.reservation.RoomReservationResponseDto;
import com.reservio.reservation_system.presentation.mapper.ReservationMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ReservationService {
    private final ReservationDao reservationDao;
    private final RoomDao roomDao;
    private final UserDao userDao;
    private final UserRoleDao userRoleDao;

    private final ReservationMapper reservationMapper;
    private final ReservationStatusDao reservationStatusDao;

    private ReservationEntity reserveRoomInternal(
            UserEntity user,
            Long roomId,
            Short guestCount,
            LocalDateTime from,
            LocalDateTime to,
            String statusName
    ) {
        if (reservationDao.existsOverlappingReservation(roomId, from, to)) {
            throw new ReservationException("Chosen room has already been reserved");
        }

        RoomEntity room = roomDao.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        ReservationStatusEntity status = reservationStatusDao.findByRsvsName(statusName)
                .orElseThrow(() -> new IllegalArgumentException("Reservation status not found"));

        ReservationEntity reservation = new ReservationEntity();
        reservation.setUsr(user);
        reservation.setRm(room);
        reservation.setRsvGuestCount(guestCount);
        reservation.setRsvCheckInDate(from);
        reservation.setRsvCheckOutDate(to);
        reservation.setRsvs(status);

        return reservationDao.save(reservation);
    }

    @Transactional
    public void cancelReservation(String userEmail, Long reservationId) {

        Long userId = getUserIdByUserEmail(userEmail);
        ReservationEntity reservation = reservationDao.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found with ID " + reservationId));

        if (!reservation.getUsr().getId().equals(userId)) {
            throw new SecurityException("You are not allowed to cancel this reservation");
        }

        String currentStatus = reservation.getRsvs().getRsvsName();
        List<String> cancellableStatuses = List.of("Pending", "Partial-Paid", "Paid");

        if (!cancellableStatuses.contains(currentStatus)) {
            throw new SecurityException("You are not allowed to cancel this reservation");
        }

        ReservationStatusEntity cancelledStatus = reservationStatusDao.findByRsvsName("Cancelled")
                .orElseThrow(() -> new SecurityException("Reservation cancelled"));

        reservation.setRsvs(cancelledStatus);

        reservationDao.save(reservation);

    }

    public List<UserReservationDto> getCurrentUserReservations(String userEmail) {
        Long userId = getUserIdByUserEmail(userEmail);
        LocalDateTime now = LocalDateTime.now();
        List<ReservationEntity> reservations = reservationDao.findCurrentReservations(userId, now);

        return reservationMapper.toUserReservationDtos(reservations);
    }

    public List<UserReservationDto> getAllUserReservations(String userEmail) {
        Long userId = getUserIdByUserEmail(userEmail);
        List<ReservationEntity> reservations = reservationDao.findAllByUsrId(userId);

        return reservationMapper.toUserReservationDtos(reservations);
    }

//    public boolean isDeskAvailable(Long deskId, LocalDateTime from, LocalDateTime to) {
//        if (from.isBefore(LocalDateTime.now()) || to.isBefore(LocalDateTime.now())) {
//            throw new ReservationException("Rezerwacja nie może być w przeszłości");
//        }
//
//        if (!from.isBefore(to)) {
//            throw new ReservationException("'from' musi być przed 'to'");
//        }
//
//        boolean existsOverlap = reservationDao.existsOverlappingReservation(deskId, from, to);
//        return !existsOverlap;
//    }


    private Long getUserIdByUserEmail(String userEmail) {
        UserEntity user = userDao.findByUsrEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));
        return user.getId();
    }

    public List<ReservationDto> getFilteredReservations(
            boolean all,
            LocalDateTime from,
            LocalDateTime to,
            String email,
            String phone
    ) {
        List<ReservationEntity> reservations;

        if (all) {
            reservations = reservationDao.findAllFiltered(from, to, email, phone);
        } else {
            reservations = reservationDao.findPendingFiltered(from, to, email, phone);
        }

        return reservationMapper.toReservationDtos(reservations);
    }

    @Transactional
    public void createManualReservation(
            String firstName,
            String lastName,
            String email,
            String phoneNumber,
            Short guestCount,
            Long roomId,
            LocalDateTime checkInDate,
            LocalDateTime checkOutDate
    ) {

        UserEntity user = userDao.findByUsrEmail(email)
                .orElseGet(() -> {
                    UserEntity newUser = new UserEntity();
                    newUser.setUsrEmail(email);
                    newUser.setUsrFirstName(firstName);
                    newUser.setUsrLastName(lastName);
                    newUser.setUsrPhoneNumber(phoneNumber);

                    UserRoleEntity role = userRoleDao.findByUrName("UNREGISTERED")
                            .orElseThrow(() -> new IllegalArgumentException("Role 'UNREGISTERED' not found"));
                    newUser.setUr(role);
                    return userDao.save(newUser);
                });

        RoomEntity room = roomDao.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        ReservationStatusEntity status = reservationStatusDao.findByRsvsName("CONFIRMED")
                .orElseThrow(() -> new IllegalArgumentException("Reservation status not found"));

        ReservationEntity reservation = new ReservationEntity();
        reservation.setUsr(user);
        reservation.setRm(room);
        reservation.setRsvGuestCount(guestCount);
        reservation.setRsvCheckInDate(checkInDate);
        reservation.setRsvCheckOutDate(checkOutDate);
        reservation.setRsvs(status);

        reservationDao.save(reservation);
    }

    @Transactional
    public RoomReservationResponseDto makeReservationForUser(
            Long roomId,
            String email,
            Short guestCount,
            LocalDateTime from,
            LocalDateTime to
    ) {
        UserEntity user = userDao.findByUsrEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        ReservationEntity saved = reserveRoomInternal(
                user,
                roomId,
                guestCount,
                from,
                to,
                "Pending"
        );

        return reservationMapper.toRoomReservationResponseDto(saved);
    }

    @Transactional()
    public UserReservationDto getReservationByIdForUser(Long reservationId, String userEmail) {
        Long userId = getUserIdByUserEmail(userEmail);

        ReservationEntity entity = reservationDao.findByIdAndUserId(reservationId, userId)
                .orElseThrow(() -> new SecurityException(
                        "Nie masz dostępu do tej rezerwacji"
                ));

        return reservationMapper.toUserReservationDto(entity);
    }

    @Transactional
    public void updateReservationStatus(Long reservationId, String newStatus) {

        System.out.println("Backend received updateReservationStatus request: reservationId="
                + reservationId + ", newStatus=" + newStatus);

        ReservationEntity reservation = reservationDao.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        ReservationStatusEntity current = reservation.getRsvs();

        if (current.getRsvsName().equals(newStatus)) {
            return;
        }

        if ("Cancelled".equals(current.getRsvsName())) {
            throw new IllegalStateException("Cannot change status of a cancelled reservation.");
        }

        ReservationStatusEntity newStatusEntity = reservationStatusDao.findByRsvsName(newStatus)
                .orElseThrow(() -> new IllegalArgumentException("Status not found"));

        reservation.setRsvs(newStatusEntity);
        reservationDao.save(reservation);
    }
}
