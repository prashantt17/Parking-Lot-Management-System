package com.example.parking.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name="parking_slot", uniqueConstraints = @UniqueConstraint(columnNames = {"floor", "slot_number"}))
public class ParkingSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public enum SlotType { BIKE, CAR, TRUCK }
    public enum Status { FREE, OCCUPIED, RESERVED }

    @Enumerated(EnumType.STRING)
    private SlotType type;

    @Enumerated(EnumType.STRING)
    private Status status = Status.FREE;

    private int floor;

    @Column(name="slot_number")
    private String slotNumber;

    private Instant lastUsed;

    public ParkingSlot() {}

    public ParkingSlot(SlotType type, int floor, String slotNumber) {
        this.type = type; this.floor = floor; this.slotNumber = slotNumber;
        this.status = Status.FREE;
    }

    // getters and setters
    public Long getId() { return id; }
    public SlotType getType() { return type; }
    public void setType(SlotType type) { this.type = type; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public int getFloor() { return floor; }
    public void setFloor(int floor) { this.floor = floor; }
    public String getSlotNumber() { return slotNumber; }
    public void setSlotNumber(String slotNumber) { this.slotNumber = slotNumber; }
    public Instant getLastUsed() { return lastUsed; }
    public void setLastUsed(Instant lastUsed) { this.lastUsed = lastUsed; }
}
