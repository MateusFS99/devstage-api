package com.nlwconnect.devstage_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nlwconnect.devstage_api.model.Event;
import com.nlwconnect.devstage_api.repository.EventRepository;

@Service
public class EventService {
  @Autowired
  private EventRepository eventRepository;

  public List<Event> getAllEvents() {
    return (List<Event>) eventRepository.findAll();
  }

  public Event getEventByPrettyName(String prettyName) {
    return eventRepository.findEventByPrettyName(prettyName);
  }

  public Event addNewEvent(Event event) {
    event.setPrettyName(event.getTitle().toLowerCase().replaceAll(" ", "-"));
    return eventRepository.save(event);
  }
}
