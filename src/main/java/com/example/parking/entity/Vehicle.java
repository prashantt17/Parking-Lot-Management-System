package com.example.parking.entity;

import jakarta.persistence.*;

@Entity
@Table(name="vehicle", uniqueConstraints = @UniqueConstraint(columnNames = {"plate_no"}))
public class Vehicle {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name="plate_no", nullable=false)
  private String plateNo;

  @Enumerated(EnumType.STRING)
  private ParkingSlot.SlotType type;

  private String ownerName;

  public Vehicle() {}

  public Vehicle(String plateNo, ParkingSlot.SlotType type, String ownerName) {
    this.plateNo = plateNo; this.type = type; this.ownerName = ownerName;
  }

  // getters/setters
  public Long getId() { return id; }
  public String getPlateNo() { return plateNo; }
  public void setPlateNo(String plateNo) { this.plateNo = plateNo; }
  public ParkingSlot.SlotType getType() { return type; }
  public void setType(ParkingSlot.SlotType type) { this.type = type; }
  public String getOwnerName() { return ownerName; }
  public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
}
