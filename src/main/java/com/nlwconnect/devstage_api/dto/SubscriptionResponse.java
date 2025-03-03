package com.nlwconnect.devstage_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubscriptionResponse {
  private Integer subscriptionNumber;
  private String designation;
}
