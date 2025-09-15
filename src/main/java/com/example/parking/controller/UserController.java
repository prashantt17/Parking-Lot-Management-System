package com.example.parking.controller;

import com.example.parking.entity.Ticket;
import com.example.parking.service.ParkingService;
import com.example.parking.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private ParkingService parkingService;
    @Autowired private PaymentService paymentService;


    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(principal.getAttributes());
    }



    @GetMapping("/slots")
    public ResponseEntity<String> addSlot() {
        return ResponseEntity.ok("TEST");
    }


    /*@GetMapping("/ticket/{id}")
    //@PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Ticket> getTicket(@PathVariable("id") Long id){
        System.out.println("getTicket XXXXXXXXXXXX");
        return ResponseEntity.of(parkingService.ticketRepo.findById(id));
    }*/
}
