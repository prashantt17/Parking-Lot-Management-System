package com.example.parking.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name="payment")
public class Payment {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  private Ticket ticket;

  private double amount;

  public enum Status { PENDING, SUCCESS, FAILED }
  @Enumerated(EnumType.STRING)
  private Status status;

  private Instant timestamp;

  public Payment() {}

  // getters/setters
  public Long getId() { return id; }
  public Ticket getTicket() { return ticket; }
  public void setTicket(Ticket ticket) { this.ticket = ticket; }
  public double getAmount() { return amount; }
  public void setAmount(double amount) { this.amount = amount; }
  public Status getStatus() { return status; }
  public void setStatus(Status status) { this.status = status; }
  public Instant getTimestamp() { return timestamp; }
  public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}
