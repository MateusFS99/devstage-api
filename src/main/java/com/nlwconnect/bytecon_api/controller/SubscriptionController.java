package com.nlwconnect.bytecon_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import com.nlwconnect.bytecon_api.dto.ErrorMessage;
import com.nlwconnect.bytecon_api.dto.SubscriptionResponse;
import com.nlwconnect.bytecon_api.exception.EventNotFoundException;
import com.nlwconnect.bytecon_api.exception.SubscriptionConflictException;
import com.nlwconnect.bytecon_api.exception.UserIndicatorNotFoundException;
import com.nlwconnect.bytecon_api.model.User;
import com.nlwconnect.bytecon_api.service.SubscriptionService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class SubscriptionController {
  @Autowired
  private SubscriptionService subsService;

  @PostMapping({ "/subscription/{prettyName}", "/subscription/{prettyName}/{userId}" })
  public ResponseEntity<?> createSubscription(@PathVariable String prettyName,
      @PathVariable(required = false) Integer userId,
      @RequestBody User subscriber) {
    try {
      SubscriptionResponse subs = subsService.createNewSubscription(prettyName, subscriber, userId);

      return ResponseEntity.ok().body(subs);
    } catch (EventNotFoundException ex) {
      return ResponseEntity.status(404).body(new ErrorMessage(ex.getMessage()));
    } catch (SubscriptionConflictException ex) {
      return ResponseEntity.status(409).body(new ErrorMessage(ex.getMessage()));
    } catch (UserIndicatorNotFoundException ex) {
      return ResponseEntity.status(404).body(new ErrorMessage(ex.getMessage()));
    }
  }
}
