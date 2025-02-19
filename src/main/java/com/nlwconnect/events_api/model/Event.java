package com.nlwconnect.events_api.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbl_event")
@Getter
@Setter
public class Event {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "event_id")
  private Integer eventId;

  @Column(name = "title", length = 255, nullable = false)
  private String title;

  @Column(name = "pretty_name", length = 50, nullable = false, unique = true)
  private String prettyName;

  @Column(name = "location", length = 255, nullable = false)
  private String location;

  @Column(name = "price", nullable = false)
  private Double price;

  @Column(name = "start_date")
  private LocalDate startDate;

  @Column(name = "endDate")
  private LocalDate endDate;

  @Column(name = "startTime")
  private LocalTime startTime;

  @Column(name = "endTime")
  private LocalTime endTime;
}
