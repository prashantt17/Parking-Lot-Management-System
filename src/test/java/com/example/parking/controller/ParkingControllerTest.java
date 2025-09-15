package com.example.parking.controller;

import com.example.parking.dto.EntryRequest;
import com.example.parking.entity.ParkingSlot;
import com.example.parking.entity.Ticket;
import com.example.parking.service.ParkingService;
import com.example.parking.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ParkingController.class)
public class ParkingControllerTest {

  @Autowired MockMvc mockMvc;
  @MockBean ParkingService parkingService;
  @MockBean PaymentService paymentService;

  @Autowired ObjectMapper objectMapper;

  @Test
  public void entrySuccess() throws Exception {
    EntryRequest req = new EntryRequest();
    req.setPlateNo("MH12AB1234");
    req.setOwnerName("Prashant"); req.setType(ParkingSlot.SlotType.CAR);

    Ticket t = new Ticket();
    t.setEntryTime(Instant.now());

    when(parkingService.vehicleEntry(anyString(), any(), anyString())).thenReturn(t);

    mockMvc.perform(post("/api/parking/entry")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(req)))
      .andExpect(status().isOk());
  }
}
