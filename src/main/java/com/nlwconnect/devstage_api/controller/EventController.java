package com.nlwconnect.devstage_api.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import com.nlwconnect.devstage_api.model.Event;
import com.nlwconnect.devstage_api.service.EventService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/events")
@Tag(name = "Events")
public class EventController {
  @Autowired
  private EventService eventService;

  @Operation(summary = "Get all events")
  @GetMapping()
  public List<Event> getAllEvents() {
    return eventService.getAllEvents();
  }

  @Operation(summary = "Get event by pretty name")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Event found"),
      @ApiResponse(responseCode = "404", description = "Event not found")
  })
  @GetMapping("/{prettyName}")
  public ResponseEntity<Event> getEventByPrettyName(@PathVariable String prettyName) {
    Event event = eventService.getEventByPrettyName(prettyName);

    if (event == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok().body(event);
  }

  @Operation(summary = "Add new event")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Event added")
  })
  @PostMapping()
  public Event addNewEvent(@RequestBody Event event) {
    return eventService.addNewEvent(event);
  }
}
