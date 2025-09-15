package com.example.parking.controller;

import com.example.parking.entity.ParkingSlot;
import com.example.parking.repository.ParkingSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

  @Autowired private ParkingSlotRepository slotRepo;


    @GetMapping("/me")
    public Map<String, Object> me(@AuthenticationPrincipal OAuth2User principal) {
        return principal.getAttributes();
    }



  @PostMapping("/slots")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<ParkingSlot> addSlot(@RequestBody ParkingSlot slot) {
    ParkingSlot s = slotRepo.save(slot);
    return ResponseEntity.ok(s);
  }

  @DeleteMapping("/slot/{id}")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<Void> removeSlot(@PathVariable Long id) {
    slotRepo.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
