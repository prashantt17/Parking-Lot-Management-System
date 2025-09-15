package com.example.parking.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name="ticket")
public class Ticket {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional=false)
  private Vehicle vehicle;

  @ManyToOne(optional=false)
  private ParkingSlot slot;

  private Instant entryTime;
  private Instant exitTime;

  public enum Status { ACTIVE, PAID, EXITED }
  @Enumerated(EnumType.STRING)
  private Status status = Status.ACTIVE;

  public Ticket() {}

  // getters/setters
  public Long getId() { return id; }
  public Vehicle getVehicle() { return vehicle; }
  public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }
  public ParkingSlot getSlot() { return slot; }
  public void setSlot(ParkingSlot slot) { this.slot = slot; }
  public Instant getEntryTime() { return entryTime; }
  public void setEntryTime(Instant entryTime) { this.entryTime = entryTime; }
  public Instant getExitTime() { return exitTime; }
  public void setExitTime(Instant exitTime) { this.exitTime = exitTime; }
  public Status getStatus() { return status; }
  public void setStatus(Status status) { this.status = status; }
}
