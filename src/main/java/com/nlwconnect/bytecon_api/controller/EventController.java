package com.nlwconnect.bytecon_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.nlwconnect.bytecon_api.model.Event;
import com.nlwconnect.bytecon_api.service.EventService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class EventController {

  @Autowired
  private EventService eventService;

  @GetMapping("/events")
  public List<Event> getAllEvents() {
    return eventService.getAllEvents();
  }

  @GetMapping("/events/{prettyName}")
  public ResponseEntity<Event> getEventByPrettyName(@PathVariable String prettyName) {
    Event event = eventService.getEventByPrettyName(prettyName);

    if (event == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok().body(event);
  }

  @PostMapping("/events")
  public Event addNewEvent(@RequestBody Event event) {
    return eventService.addNewEvent(event);
  }
}
