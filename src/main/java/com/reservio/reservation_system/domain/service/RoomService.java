package com.reservio.reservation_system.domain.service;

import com.reservio.reservation_system.domain.repository.ReservationDao;
import com.reservio.reservation_system.domain.repository.RoomDao;
import com.reservio.reservation_system.domain.repository.RoomTypeDao;
import com.reservio.reservation_system.infrastructure.entity.ReservationEntity;
import com.reservio.reservation_system.infrastructure.entity.RoomEntity;
import com.reservio.reservation_system.presentation.dto.room.RoomDto;
import com.reservio.reservation_system.presentation.dto.user.UserDto;
import com.reservio.reservation_system.presentation.dto.reservation.RoomReservationDto;
import com.reservio.reservation_system.presentation.dto.room.RoomDetailsDto;
import com.reservio.reservation_system.presentation.dto.room.AvailableRoomDto;
import com.reservio.reservation_system.presentation.dto.room.RoomSlotDto;
import com.reservio.reservation_system.presentation.mapper.ReservationMapper;
import com.reservio.reservation_system.presentation.mapper.RoomMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class RoomService {
    private final RoomDao roomDao;
    private final ReservationDao reservationDao;
    private final RoomMapper roomMapper;
    private final ReservationMapper reservationMapper;
    private final RoomTypeDao roomTypeDao;

    public List<RoomDto> getAllRoomsWithDetails() {
        List<RoomEntity> rooms = roomDao.findAll();

        return roomMapper.toRoomDtoList(rooms); }

    public List<RoomReservationDto> getCurrentReservationsForRoom(Long roomId) {
        LocalDateTime now = LocalDateTime.now();
        List<ReservationEntity> currentReservations = reservationDao.findAllByRmIdAndRsvCheckOutDateAfter(roomId, now);

        return reservationMapper.toRoomReservationDtos(currentReservations);
    }

    public List<RoomDetailsDto> getRoomsAvailability(LocalDateTime from, LocalDateTime to) {

        List<ReservationEntity> reservations = reservationDao
                .findAllByRsvCheckInDateLessThanEqualAndRsvCheckOutDateGreaterThanEqual(to, from);

        Map<Long, List<ReservationEntity>> reservationsByRoom = reservations.stream()
                .collect(Collectors.groupingBy(r -> r.getRm().getId()));

        return roomDao.findAll().stream().map(room -> {
            List<ReservationEntity> roomReservations = reservationsByRoom.getOrDefault(room.getId(), List.of());

            List<RoomSlotDto> occupiedSlots = new ArrayList<>(roomReservations.stream().map(r ->
                    new RoomSlotDto(
                            r.getRsvCheckInDate(),
                            r.getRsvCheckOutDate(),
                            new UserDto(
                                    r.getUsr().getId(),
                                    r.getUsr().getUsrFirstName(),
                                    r.getUsr().getUsrLastName(),
                                    r.getUsr().getUsrEmail()
                            )
                    )
            ).toList());

            String activityStatus;
            if (occupiedSlots.isEmpty()) {
                activityStatus = "FREE";
            } else if (isRoomFullyOccupied(occupiedSlots, from, to)) {
                activityStatus = "BUSY";
            } else {
                activityStatus = "PARTIAL";
            }

            return new RoomDetailsDto(
                    room.getId(),
                    room.getRmNumber(),
                    activityStatus,
                    occupiedSlots
            );
        }).toList();
    }

    public List<RoomReservationDto> getRoomReservations(Long deskId, LocalDateTime from, LocalDateTime to) {
        List<ReservationEntity> reservations = reservationDao
                .findAllByRsvCheckInDateLessThanEqualAndRsvCheckOutDateGreaterThanEqual(to, from);
        System.out.println();
        return reservationMapper.toRoomReservationDtos(reservations);
    }

    public void updateRoomStatus(Long roomId, String newStatus) {
        RoomEntity room = roomDao.findFirstById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Can't find room with id " + roomId));

        room.setRmStatus(newStatus);
        roomDao.save(room);
    }

    private boolean isRoomFullyOccupied(List<RoomSlotDto> slots, LocalDateTime from, LocalDateTime to) {
        slots.sort(Comparator.comparing(RoomSlotDto::getStart));

        LocalDateTime current = from;

        for (RoomSlotDto slot : slots) {
            if (slot.getStart().isAfter(current)) {
                return false;
            }
            if (slot.getEnd().isAfter(current)) {
                current = slot.getEnd();
            }
        }
        return !current.isBefore(to);
    }

    public List<AvailableRoomDto> getAvailableRooms(Long roomTypeId,
                                                    LocalDateTime from,
                                                    LocalDateTime to) {
        if (roomTypeId == null || !roomTypeDao.existsById(roomTypeId)) {
            throw new IllegalArgumentException("Room type with id " + roomTypeId + " does not exist");
        }

        if (from == null || to == null || from.isAfter(to) || from.equals(to)) {
            throw new IllegalArgumentException("Invalid date range: from must be before to and not equal");
        }

        List<RoomEntity> rooms = roomDao.findAvailableRooms(roomTypeId, from, to);

        return roomMapper.toAvailableRoomDtoList(rooms);
    }
}

