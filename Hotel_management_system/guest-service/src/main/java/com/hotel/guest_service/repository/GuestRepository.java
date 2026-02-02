package com.hotel.guest_service.repository;

import com.hotel.guest_service.model.GuestModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuestRepository extends JpaRepository<GuestModel,Long> {
    Optional<GuestModel> findByMemberCode(String memberCode);

    List<GuestModel> findByNameContainingIgnoreCase(String name);
}
