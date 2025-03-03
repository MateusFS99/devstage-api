package com.nlwconnect.devstage_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubscriptionRankingItem {
  private Long subscribers;
  private Integer userId;
  private String name;
}
