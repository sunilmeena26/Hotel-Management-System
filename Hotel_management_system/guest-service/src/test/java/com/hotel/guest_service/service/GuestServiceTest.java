package com.hotel.guest_service.service;

import com.hotel.guest_service.model.GuestModel;

import java.util.Optional;

public interface GuestServiceTest {
    GuestModel addGuest(GuestModel guest);
    GuestModel getGuestById(Long id);
    GuestModel getGuestByMemberCode(String memberCode);
    void deleteGuestById(Long id);
    void deleteGuestByMemberCode(String memberCode);
    void updateGuest(Long id, GuestModel updatedGuest);
}

