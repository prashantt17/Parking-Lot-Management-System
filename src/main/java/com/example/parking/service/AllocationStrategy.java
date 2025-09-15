package com.example.parking.service;

import com.example.parking.entity.ParkingSlot;
import java.util.Optional;

public interface AllocationStrategy {
    Optional<ParkingSlot> allocateSlot(ParkingSlot.SlotType type);
}
