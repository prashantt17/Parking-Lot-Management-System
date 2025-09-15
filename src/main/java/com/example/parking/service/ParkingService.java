package com.example.parking.service;

import com.example.parking.entity.*;
import com.example.parking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
public class ParkingService {


  private final VehicleRepository vehicleRepo;
  public final TicketRepository ticketRepo;
  private final ParkingSlotRepository slotRepo;
  private final AllocationStrategy allocationStrategy;

  @Autowired
  public ParkingService(VehicleRepository vehicleRepo, TicketRepository ticketRepo,
                        ParkingSlotRepository slotRepo, @Qualifier("nearest") AllocationStrategy allocationStrategy) {
    this.vehicleRepo = vehicleRepo;
    this.ticketRepo = ticketRepo;
    this.slotRepo = slotRepo;
    this.allocationStrategy = allocationStrategy;
  }

  @Transactional
  public Ticket vehicleEntry(String plateNo, ParkingSlot.SlotType type, String ownerName) {
    Optional<Vehicle> vopt = vehicleRepo.findByPlateNo(plateNo);
    Vehicle vehicle = vopt.orElseGet(() -> vehicleRepo.save(new Vehicle(plateNo, type, ownerName)));

    boolean alreadyInside = ticketRepo.existsByVehiclePlateNoAndStatus(plateNo);
    if (alreadyInside) {
      throw new IllegalStateException("Vehicle with plate " + plateNo + " is already parked");
    }

    Optional<ParkingSlot> slotOpt = allocationStrategy.allocateSlot(type);
    if (slotOpt.isEmpty()) throw new RuntimeException("No slot available for type " + type);

    ParkingSlot slot = slotOpt.get();

    Ticket ticket = new Ticket();
    ticket.setVehicle(vehicle);
    ticket.setSlot(slot);
    ticket.setEntryTime(Instant.now());
    ticket.setStatus(Ticket.Status.ACTIVE);

    ticketRepo.save(ticket);
    return ticket;
  }

  @Transactional(readOnly = true)
  public double calculateCharge(Long ticketId, PricingRules pricingRules) {
    Ticket ticket = ticketRepo.findById(ticketId).orElseThrow(() -> new IllegalArgumentException("Ticket not found"));
    if (ticket.getExitTime() != null)
      throw new IllegalStateException("Ticket already closed");
    Instant now = Instant.now();
    Duration duration = Duration.between(ticket.getEntryTime(), now);
    return pricingRules.calculateAmount(duration, ticket.getVehicle().getType());
  }

    public Optional<Ticket> getParkingDetailsById(Long id) {
        System.out.println("ticketRepo : " + ticketRepo);
        return ticketRepo.findById(id);
    }
}
