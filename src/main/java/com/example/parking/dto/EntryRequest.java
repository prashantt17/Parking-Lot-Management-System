package com.example.parking.dto;

import com.example.parking.entity.ParkingSlot;

public class EntryRequest {
  private String plateNo;
  private String ownerName;
  private ParkingSlot.SlotType type;

  public EntryRequest() {}
  public String getPlateNo() { return plateNo; }
  public void setPlateNo(String plateNo) { this.plateNo = plateNo; }
  public String getOwnerName() { return ownerName; }
  public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
  public ParkingSlot.SlotType getType() { return type; }
  public void setType(ParkingSlot.SlotType type) { this.type = type; }
}
