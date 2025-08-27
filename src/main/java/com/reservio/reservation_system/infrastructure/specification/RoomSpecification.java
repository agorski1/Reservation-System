package com.reservio.reservation_system.infrastructure.specification;

import com.reservio.reservation_system.infrastructure.entity.RoomEntity;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public class RoomSpecification {

    public static Specification<RoomEntity> filterRooms(Set<String> amenities, Set<String> roomTypes, Set<Short> capacities) {
        return (root, query, criteriaBuilder) -> {
            root.fetch("rt", JoinType.LEFT);
            root.fetch("roomAmenities", JoinType.LEFT);

            var predicates = criteriaBuilder.conjunction();

            if (amenities != null && !amenities.isEmpty()) {
                var amenityJoin = root.joinSet("roomAmenities");
                predicates = criteriaBuilder.and(predicates, amenityJoin.get("code").in(amenities));
            }

            if (roomTypes != null && !roomTypes.isEmpty()) {
                predicates = criteriaBuilder.and(predicates, root.get("rt").get("name").in(roomTypes));
            }

            if (capacities != null && !capacities.isEmpty()) {
                predicates = criteriaBuilder.and(predicates, root.get("rmCapacity").in(capacities));
            }

            predicates = criteriaBuilder.and(predicates, criteriaBuilder.equal(root.get("rmIsDeleted"), false));

            query.distinct(true);
            return predicates;
        };
    }
}