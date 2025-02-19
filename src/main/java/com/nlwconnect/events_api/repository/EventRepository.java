package com.nlwconnect.events_api.repository;

import org.springframework.data.repository.CrudRepository;

import com.nlwconnect.events_api.model.Event;

public interface EventRepository extends CrudRepository<Event, Integer> {
  public Event findByPrettyName(String prettyName);
}
