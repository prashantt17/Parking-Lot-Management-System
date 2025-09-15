package com.example.parking.repository;

import com.example.parking.entity.ParkingSlot;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.List;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select s from ParkingSlot s where s.type = :type and s.status = 'FREE' order by s.floor asc, s.slotNumber asc")
  List<ParkingSlot> findFreeSlotsForTypeForUpdate(@Param("type") ParkingSlot.SlotType type);
}
