package com.example.parking.controller;

import com.example.parking.dto.EntryRequest;
import com.example.parking.entity.Payment;
import com.example.parking.entity.Ticket;
import com.example.parking.service.ParkingService;
import com.example.parking.service.PaymentService;
import com.example.parking.service.PricingRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {

  //@Autowired private ParkingService parkingService;
  @Autowired private PaymentService paymentService;

    private final ParkingService parkingService;

    // Spring injects ParkingService automatically
    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

  @PostMapping("/entry")
  @PreAuthorize("hasAuthority('ROLE_USER')")
  public ResponseEntity<Ticket> entry(@RequestBody EntryRequest req){
    Ticket t = parkingService.vehicleEntry(req.getPlateNo(), req.getType(), req.getOwnerName());
    return ResponseEntity.ok(t);
  }

  @PostMapping("/exit/{ticketId}/pay")
  @PreAuthorize("hasAuthority('ROLE_USER')")
  public ResponseEntity<Payment> exitAndPay(@PathVariable Long ticketId){
    double amount = parkingService.calculateCharge(ticketId, PricingRules.defaultRules());
    Payment p = paymentService.processPayment(ticketId, amount);
    return ResponseEntity.ok(p);
  }

  @GetMapping("/ticket/{id}")
  //@PreAuthorize("hasAuthority('ROLE_USER')")
  public ResponseEntity<Ticket> getTicket(@PathVariable("id") Long id){
      System.out.println("getTicket XXXXXXXXXXXX");
      Optional<Ticket> ticket = parkingService.getParkingDetailsById(id);
      System.out.println("ticket : "+ ticket.toString());
      return ResponseEntity.of(ticket);
  }
}
