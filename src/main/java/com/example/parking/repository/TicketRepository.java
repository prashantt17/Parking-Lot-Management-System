package com.example.parking.repository;

import com.example.parking.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

  @Query("select case when count(t)>0 then true else false end from Ticket t where t.vehicle.plateNo = ?1 and t.status = 'ACTIVE'")
  boolean existsByVehiclePlateNoAndStatus(String plateNo);

  Optional<Ticket> findById(Long id);
}
