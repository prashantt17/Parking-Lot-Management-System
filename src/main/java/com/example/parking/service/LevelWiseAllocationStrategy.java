package com.example.parking.service;

import com.example.parking.entity.ParkingSlot;
import com.example.parking.repository.ParkingSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service("levelwise")
public class LevelWiseAllocationStrategy implements AllocationStrategy {

  private final ParkingSlotRepository slotRepo;

  @Autowired
  public LevelWiseAllocationStrategy(ParkingSlotRepository slotRepo) {
    this.slotRepo = slotRepo;
  }

  @Override
  @Transactional
  public Optional<ParkingSlot> allocateSlot(ParkingSlot.SlotType type) {
    List<ParkingSlot> free = slotRepo.findFreeSlotsForTypeForUpdate(type);
    if (free.isEmpty()) return Optional.empty();
    // pick from highest floor first (example variation)
    ParkingSlot chosen = free.stream().sorted((a,b) -> Integer.compare(b.getFloor(), a.getFloor())).findFirst().get();
    chosen.setStatus(ParkingSlot.Status.OCCUPIED);
    chosen.setLastUsed(Instant.now());
    slotRepo.save(chosen);
    return Optional.of(chosen);
  }
}
