package com.nlwconnect.devstage_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import com.nlwconnect.devstage_api.dto.ErrorMessage;
import com.nlwconnect.devstage_api.dto.SubscriptionResponse;
import com.nlwconnect.devstage_api.exception.EventNotFoundException;
import com.nlwconnect.devstage_api.exception.SubscriptionConflictException;
import com.nlwconnect.devstage_api.exception.UserIndicatorNotFoundException;
import com.nlwconnect.devstage_api.model.User;
import com.nlwconnect.devstage_api.service.SubscriptionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1/subscription")
@Tag(name = "Subscriptions")
public class SubscriptionController {
  @Autowired
  private SubscriptionService subsService;

  @Operation(summary = "Create new subscription")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Subscription created"),
      @ApiResponse(responseCode = "404", description = "Event not found"),
      @ApiResponse(responseCode = "409", description = "Subscription conflict"),
      @ApiResponse(responseCode = "404", description = "User indicator not found")
  })
  @PostMapping({ "/{prettyName}", "/{prettyName}/{userId}" })
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

  @Operation(summary = "Get event ranking")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Ranking generated"),
      @ApiResponse(responseCode = "404", description = "Event not found")
  })
  @GetMapping("/{prettyName}/ranking")
  public ResponseEntity<?> generateRankingByEvent(@PathVariable String prettyName) {
    try {
      return ResponseEntity.ok(subsService.getRankingByEvent(prettyName));
    } catch (EventNotFoundException ex) {
      return ResponseEntity.status(404).body(new ErrorMessage(ex.getMessage()));
    }
  }

  @Operation(summary = "Get user event stats")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Stats generated"),
      @ApiResponse(responseCode = "404", description = "Event not found")
  })
  @GetMapping("/{prettyName}/ranking/{userId}")
  public ResponseEntity<?> getUserEventStats(@PathVariable String prettyName,
      @PathVariable Integer userId) {
    try {
      return ResponseEntity.ok(subsService.getUserEventStats(prettyName, userId));
    } catch (Exception ex) {
      return ResponseEntity.status(404).body(new ErrorMessage(ex.getMessage()));
    }
  }
}
