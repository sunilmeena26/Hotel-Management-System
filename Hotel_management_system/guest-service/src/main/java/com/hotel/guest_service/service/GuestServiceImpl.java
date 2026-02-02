package com.hotel.guest_service.service;

import com.hotel.guest_service.exception.GuestNotFoundException;
import com.hotel.guest_service.model.GuestModel;
import com.hotel.guest_service.repository.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class GuestServiceImpl implements GuestService{

    @Autowired
    private GuestRepository guestRepository;


    public GuestModel addGuest(GuestModel guest) {
        guest.setMemberCode(generateSixDigitId());
        return guestRepository.save(guest);
    }

    public static String generateSixDigitId() {
        LocalDateTime now = LocalDateTime.now();
        String timeStamp = now.format(DateTimeFormatter.ofPattern("MMddHHmmss")); // MonthDayHourMinuteSecond

        // Use the last 6 digits to keep it within limit
        return timeStamp.substring(timeStamp.length() - 6);
    }


    @Override
    public GuestModel getGuestById(Long id) {

        return guestRepository.findById(id)
                .orElseThrow(() -> new GuestNotFoundException("Guest not found with ID: " + id));
    }

    @Override
    public GuestModel getGuestByMemberCode(String memberCode) {
        return guestRepository.findByMemberCode(memberCode)
                .orElseThrow(() -> new GuestNotFoundException("Guest not found with member code: " + memberCode));
    }

    @Override
    public void deleteGuestById(Long id) {
        if (!guestRepository.existsById(id)) {
            throw new GuestNotFoundException("Guest not found with ID: " + id);
        }
        guestRepository.deleteById(id);
    }

    @Override
    public void deleteGuestByMemberCode(String memberCode) {
        GuestModel guest = getGuestByMemberCode(memberCode);
        guestRepository.delete(guest);
    }

    @Override
    public void updateGuest(Long id, GuestModel updatedGuest) {
        GuestModel existingGuest = getGuestById(id);


        existingGuest.setPhoneNumber(updatedGuest.getPhoneNumber());

        existingGuest.setName(updatedGuest.getName());
        existingGuest.setEmail(updatedGuest.getEmail());
        existingGuest.setGender(updatedGuest.getGender());
        existingGuest.setAddress(updatedGuest.getAddress());

        guestRepository.save(existingGuest);
    }

    public List<GuestModel> getAllGuests() {
        return guestRepository.findAll();
    }

    public List<GuestModel> getGuestsByName(String name) {
        return guestRepository.findByNameContainingIgnoreCase(name);
    }
}
