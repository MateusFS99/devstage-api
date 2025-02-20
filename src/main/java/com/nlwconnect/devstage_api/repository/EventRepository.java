package com.nlwconnect.devstage_api.repository;

import org.springframework.data.repository.CrudRepository;

import com.nlwconnect.devstage_api.model.Event;

public interface EventRepository extends CrudRepository<Event, Integer> {
  public Event findEventByPrettyName(String prettyName);
}
