package com.hotel.guest_service.controller;

import com.hotel.guest_service.exception.GlobalExceptionHandler;
import com.hotel.guest_service.exception.GuestNotFoundException;
import com.hotel.guest_service.exception.IdLessThenException;
import com.hotel.guest_service.model.GuestModel;
import com.hotel.guest_service.service.GuestServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/guest")
public class GuestController {

    @Autowired
    private GuestServiceImpl guestService;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @PostMapping("/add")
    public ResponseEntity<String> addGuest(@Valid @RequestBody GuestModel guest) {
      GuestModel guestModel= guestService.addGuest(guest);
        return ResponseEntity.ok("Guest successfully added to the database.\n"+"Your Member code is: "+guestModel.getMemberCode());
    }

    @GetMapping("/get/id/{id}")
    public GuestModel getGuestById(@PathVariable Long id) {
//        if(id>9){
//          throw new IdLessThenException("Id Should be less than 10");
//        }
        GuestModel guest = guestService.getGuestById(id);
        return guest;
    }

    @GetMapping("/get/memberCode/{memberCode}")
    public ResponseEntity<GuestModel> getGuestByMemberCode(@PathVariable String memberCode) {
        GuestModel guest = guestService.getGuestByMemberCode(memberCode);
        return ResponseEntity.ok(guest);
    }

    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<String> deleteGuestById(@PathVariable Long id) {

        guestService.deleteGuestById(id);
        return ResponseEntity.ok("Guest deleted successfully.");
    }

    @DeleteMapping("/delete/memberCode/{memberCode}")
    public ResponseEntity<String> deleteGuestByMemberCode(@PathVariable String memberCode) {
        guestService.deleteGuestByMemberCode(memberCode);
        return ResponseEntity.ok("Guest with member code deleted successfully.");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateGuest(@PathVariable Long id, @Valid @RequestBody GuestModel updatedGuest) {
        guestService.updateGuest(id, updatedGuest);
        return ResponseEntity.ok("Guest updated successfully.");
    }

    @GetMapping("/getAll")
    public List<GuestModel> getAllGuests() {
        return guestService.getAllGuests();
    }

    @GetMapping("/get/name/{name}")
    public ResponseEntity<List<GuestModel>> getGuestsByName(@PathVariable String name) {
        List<GuestModel> guests = guestService.getGuestsByName(name);
        return ResponseEntity.ok(guests);
    }
}
