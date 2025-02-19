package com.nlwconnect.bytecon_api.repository;

import org.springframework.data.repository.CrudRepository;

import com.nlwconnect.bytecon_api.model.Event;
import com.nlwconnect.bytecon_api.model.Subscription;
import com.nlwconnect.bytecon_api.model.User;

public interface SubscriptionRepository extends CrudRepository<Subscription, Integer> {
  public Subscription findSubscriptionByEventAndSubscriber(Event event, User user);
}
