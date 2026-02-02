package com.hotel.staff_service.repository;

import com.hotel.staff_service.model.StaffModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StaffRepository extends JpaRepository<StaffModel,Long> {
    Optional<StaffModel> findByCode(String code);
    List<StaffModel> findByEmployeeNameContainingIgnoreCase(String name);

}
