package com.example.parking.service;

import com.example.parking.entity.*;
import com.example.parking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class PaymentService {

  private final PaymentRepository paymentRepo;
  private final TicketRepository ticketRepo;
  private final ParkingSlotRepository slotRepo;

  @Autowired
  public PaymentService(PaymentRepository paymentRepo, TicketRepository ticketRepo, ParkingSlotRepository slotRepo) {
    this.paymentRepo = paymentRepo;
    this.ticketRepo = ticketRepo;
    this.slotRepo = slotRepo;
  }

  @Transactional
  public Payment processPayment(Long ticketId, double amount) {
    Ticket ticket = ticketRepo.findById(ticketId).orElseThrow(() -> new IllegalArgumentException("Ticket not found"));
    if (ticket.getStatus() != Ticket.Status.ACTIVE) throw new IllegalStateException("Ticket not active");

    Payment payment = new Payment();
    payment.setTicket(ticket);
    payment.setAmount(amount);
    payment.setStatus(Payment.Status.PENDING);
    payment.setTimestamp(Instant.now());

    payment = paymentRepo.save(payment);

    boolean success = simulateExternalPaymentProcessor(amount);

    if (!success) {
      payment.setStatus(Payment.Status.FAILED);
      paymentRepo.save(payment);
      throw new RuntimeException("Payment failed");
    }

    payment.setStatus(Payment.Status.SUCCESS);
    paymentRepo.save(payment);

    ParkingSlot slot = ticket.getSlot();
    ParkingSlot locked = slotRepo.findById(slot.getId()).orElseThrow();
    locked.setStatus(ParkingSlot.Status.FREE);
    slotRepo.save(locked);

    ticket.setExitTime(Instant.now());
    ticket.setStatus(Ticket.Status.EXITED);
    ticketRepo.save(ticket);

    return payment;
  }

  private boolean simulateExternalPaymentProcessor(double amt) {
    return true;
  }
}
