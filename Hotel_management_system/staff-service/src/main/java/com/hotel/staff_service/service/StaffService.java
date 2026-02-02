package com.hotel.staff_service.service;

import com.hotel.staff_service.dto.ReservationDTO;
import com.hotel.staff_service.dto.RoomDTO;
import com.hotel.staff_service.dto.StaffDTO;
import com.hotel.staff_service.exception.StaffNotFoundException;
import com.hotel.staff_service.feign.ReservationServiceClient;
import com.hotel.staff_service.feign.RoomServiceClient;
import com.hotel.staff_service.model.StaffModel;
import com.hotel.staff_service.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StaffService {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private ReservationServiceClient reservationClient;

    @Autowired
    private RoomServiceClient roomClient;

    public List<ReservationDTO> getStaffReservations(Long staffId) {
        return reservationClient.getReservationsByStaffId(staffId);
    }

    public List<RoomDTO> getManagedRooms(Long staffId) {
        return roomClient.getRoomsManagedByStaff(staffId);
    }

    public String addStaff(StaffModel staff) {
        String generatedCode = "ST" + String.format("%04d", (int)(Math.random() * 10000));
        staff.setCode(generatedCode);
        staffRepository.save(staff);
        return "Staff member successfully added.";
    }

    public StaffModel getStaffById(Long id) {
        return staffRepository.findById(id)
                .orElseThrow(() -> new StaffNotFoundException("Staff member not found with ID: " + id));
    }

    public void deleteStaff(Long id) {
        if (!staffRepository.existsById(id)) {
            throw new StaffNotFoundException("Staff member not found with ID: " + id);
        }
        staffRepository.deleteById(id);
    }

    public void updateStaff(Long id, StaffDTO updatedDto) {
        StaffModel existing = getStaffById(id);

        existing.setEmployeeName(updatedDto.getEmployeeName());
        existing.setEmployeeAddress(updatedDto.getEmployeeAddress());
        existing.setPhoneNumber(updatedDto.getPhone());
        existing.setAboutWork(updatedDto.getAboutWork());
        existing.setSalary(updatedDto.getSalary());
        existing.setAge(updatedDto.getAge());
        existing.setEmail(updatedDto.getEmail());

        staffRepository.save(existing);
    }

    public List<StaffModel> getAllStaff() {
        return staffRepository.findAll();
    }

    public StaffModel getStaffByCode(String code) {
        return staffRepository.findByCode(code)
                .orElseThrow(() -> new StaffNotFoundException("Staff not found with code: " + code));
    }

    public List<StaffModel> getStaffByName(String name) {
        return staffRepository.findByEmployeeNameContainingIgnoreCase(name);
    }
}
