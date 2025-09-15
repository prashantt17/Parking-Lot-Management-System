package com.example.parking.service;

import com.example.parking.entity.ParkingSlot;

import java.time.Duration;
import java.util.Map;

public class PricingRules {
  private Map<ParkingSlot.SlotType, Double> hourlyRate;
  private Duration freeDuration = Duration.ofHours(2);

  public static PricingRules defaultRules() {
    PricingRules r = new PricingRules();
    r.hourlyRate = Map.of(
      ParkingSlot.SlotType.BIKE, 10.0,
      ParkingSlot.SlotType.CAR, 30.0,
      ParkingSlot.SlotType.TRUCK, 50.0
    );
    return r;
  }

  public double calculateAmount(Duration stay, ParkingSlot.SlotType type) {
    if (stay.compareTo(freeDuration) <= 0) return 0.0;
    long hours = stay.minus(freeDuration).toHours();
    if (stay.minus(freeDuration).toMinutesPart() > 0) hours++;
    return hours * hourlyRate.get(type);
  }
}
