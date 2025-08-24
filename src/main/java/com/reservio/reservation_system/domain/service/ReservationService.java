package com.reservio.reservation_system.domain.service;

import com.reservio.reservation_system.domain.exception.ReservationException;
import com.reservio.reservation_system.domain.repository.ReservationDao;
import com.reservio.reservation_system.domain.repository.RoomDao;
import com.reservio.reservation_system.domain.repository.UserDao;
import com.reservio.reservation_system.infrastructure.entity.ReservationEntity;
import com.reservio.reservation_system.infrastructure.entity.RoomEntity;
import com.reservio.reservation_system.infrastructure.entity.UserEntity;
import com.reservio.reservation_system.presentation.dto.User.UserDto;
import com.reservio.reservation_system.presentation.dto.reservation.DeskReservationResponseDto;
import com.reservio.reservation_system.presentation.dto.reservation.UserReservationDto;
import com.reservio.reservation_system.presentation.dto.room.RoomDetailsDto;
import com.reservio.reservation_system.presentation.dto.room.RoomSlotDto;
import com.reservio.reservation_system.presentation.mapper.ReservationMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReservationService {
    private final ReservationDao reservationDao;
    private final RoomDao roomDao;
    private final UserDao userDao;
    private final ReservationMapper reservationMapper;
    private final LocalTime openingHour = LocalTime.of(7, 0);
    private final LocalTime closingHour = LocalTime.of(18, 0);

    @Transactional
    public DeskReservationResponseDto reserveRoom(Long roomId, String usrEmail, LocalDateTime from, LocalDateTime to) {
        if (reservationDao.existsOverlappingReservation(roomId, from, to)) {
            throw new IllegalStateException("Chosen room has already been reserved");
        }

        ReservationEntity reservation = new ReservationEntity();

        RoomEntity room = roomDao.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Can't find room with ID " + roomId));

        UserEntity user = userDao.findByUsrEmail(usrEmail)
                .orElseThrow(() -> new IllegalArgumentException("Can't find user with email " + usrEmail));

        reservation.setRm(room);
        reservation.setUsr(user);
        reservation.setRsvCheckInDate(from);
        reservation.setRsvCheckOutDate(to);

        ReservationEntity saved = reservationDao.save(reservation);

        return new DeskReservationResponseDto(
                saved.getId(),
                saved.getRm().getId(),
                saved.getRsvCheckInDate(),
                saved.getRsvCheckOutDate()
        );
    }

    @Transactional
    public void cancelReservation(String userEmail, Long reservationId) {
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


}
