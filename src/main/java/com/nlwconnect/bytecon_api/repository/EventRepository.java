package com.nlwconnect.bytecon_api.repository;

import org.springframework.data.repository.CrudRepository;

import com.nlwconnect.bytecon_api.model.Event;

public interface EventRepository extends CrudRepository<Event, Integer> {
  public Event findByPrettyName(String prettyName);
}
