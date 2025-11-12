package com.reservio.reservation_system.domain.service;

import com.reservio.reservation_system.domain.exception.ReservationException;
import com.reservio.reservation_system.domain.repository.ReservationDao;
import com.reservio.reservation_system.domain.repository.ReservationStatusDao;
import com.reservio.reservation_system.domain.repository.RoomDao;
import com.reservio.reservation_system.domain.repository.UserDao;
import com.reservio.reservation_system.infrastructure.entity.ReservationEntity;
import com.reservio.reservation_system.infrastructure.entity.ReservationStatusEntity;
import com.reservio.reservation_system.infrastructure.entity.RoomEntity;
import com.reservio.reservation_system.infrastructure.entity.UserEntity;
import com.reservio.reservation_system.presentation.dto.reservation.ReservationDto;
import com.reservio.reservation_system.presentation.dto.reservation.RoomReservationResponseDto;
import com.reservio.reservation_system.presentation.dto.reservation.UserReservationDto;
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
    private final ReservationMapper reservationMapper;
    private final ReservationStatusDao reservationStatusDao;

    @Transactional
    public RoomReservationResponseDto reserveRoom(Long roomId,
                                                  String usrEmail,
                                                  Short guestCount,
                                                  LocalDateTime from,
                                                  LocalDateTime to
    ) {

        if (reservationDao.existsOverlappingReservation(roomId, from, to)) {
            throw new IllegalStateException("Chosen room has already been reserved");
        }

        ReservationEntity reservation = new ReservationEntity();

        RoomEntity room = roomDao.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Can't find room with ID " + roomId));

        UserEntity user = userDao.findByUsrEmail(usrEmail)
                .orElseThrow(() -> new IllegalArgumentException("Can't find user with email " + usrEmail));

        ReservationStatusEntity status = reservationStatusDao.findByRsvsName("Pending")
                .orElseThrow(() -> new RuntimeException("Reservation status 'Pending' not found"));

        reservation.setRm(room);
        reservation.setUsr(user);
        reservation.setRsvGuestCount(guestCount);
        reservation.setRsvCheckInDate(from);
        reservation.setRsvCheckOutDate(to);
        reservation.setRsvs(status);


        ReservationEntity saved = reservationDao.save(reservation);

        return new RoomReservationResponseDto(
                saved.getId(),
                saved.getRm().getId(),
                saved.getRsvGuestCount(),
                saved.getRsvCheckInDate(),
                saved.getRsvCheckOutDate()
        );
    }

    @Transactional
    public void cancelReservation(String userEmail, Long reservationId) {
        //TODO dopisac ze jest jakis termin na cancel po ktorym nie ma juz zwrotow pieniedzy ...
        // dodatkowo nie delete nie moze byc i nie tez sprawdzenei daty kiedy jest usuwana bo nie moze byc usuwana z przeszlosci
        //  i tyle
        Long userId = getUserIdByUserEmail(userEmail);
        ReservationEntity reservation = reservationDao.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found with ID " + reservationId));

        if (!reservation.getUsr().getId().equals(userId)) {
            throw new SecurityException("You are not allowed to cancel this reservation");
        }
        reservationDao.delete(reservation);
    }

    public List<UserReservationDto> getCurrentUserReservations(String userEmail) {
        Long userId = getUserIdByUserEmail(userEmail);
        LocalDateTime now = LocalDateTime.now();
        List<ReservationEntity> reservations = reservationDao.findAllByUsrIdAndRsvCheckOutDateAfter(userId, now);

        return reservationMapper.toUserReservationDtos(reservations);
    }

    public List<UserReservationDto> getAllUserReservations(String userEmail) {
        Long userId = getUserIdByUserEmail(userEmail);
        List<ReservationEntity> reservations = reservationDao.findAllByUsrId(userId);

        return reservationMapper.toUserReservationDtos(reservations);
    }

    public boolean isDeskAvailable(Long deskId, LocalDateTime from, LocalDateTime to) {
        if (from.isBefore(LocalDateTime.now()) || to.isBefore(LocalDateTime.now())) {
            throw new ReservationException("Rezerwacja nie może być w przeszłości");
        }

        if (!from.isBefore(to)) {
            throw new ReservationException("'from' musi być przed 'to'");
        }

        boolean existsOverlap = reservationDao.existsOverlappingReservation(deskId, from, to);
        return !existsOverlap;
    }


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
}
