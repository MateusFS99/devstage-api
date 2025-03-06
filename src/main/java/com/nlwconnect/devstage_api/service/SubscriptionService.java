package com.nlwconnect.devstage_api.service;

import java.util.List;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nlwconnect.devstage_api.dto.RankingStats;
import com.nlwconnect.devstage_api.dto.SubscriptionRankingItem;
import com.nlwconnect.devstage_api.exception.EventNotFoundException;
import com.nlwconnect.devstage_api.exception.SubscriptionConflictException;
import com.nlwconnect.devstage_api.exception.UserIndicatorNotFoundException;
import com.nlwconnect.devstage_api.model.Event;
import com.nlwconnect.devstage_api.model.Subscription;
import com.nlwconnect.devstage_api.model.User;
import com.nlwconnect.devstage_api.repository.EventRepository;
import com.nlwconnect.devstage_api.repository.SubscriptionRepository;
import com.nlwconnect.devstage_api.repository.UserRepository;

@Service
public class SubscriptionService {
  @Autowired
  private EventRepository eventRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private SubscriptionRepository subsRepository;

  public Subscription createNewSubscription(String eventName, User user, Integer indicationUserId) {
    Event evt = eventRepository.findEventByPrettyName(eventName);

    if (evt == null) { // Event not exists case
      throw new EventNotFoundException("Event " + eventName + " not exists");
    }

    User indicationUser = null;

    if (indicationUserId != null) {
      indicationUser = userRepository.findById(indicationUserId).orElse(null);
      if (indicationUser == null) { // User indication does not exists case
        throw new UserIndicatorNotFoundException("Invalid indication");
      }
    }

    User userRec = userRepository.findUserByEmail(user.getEmail());

    if (userRec == null) { // User is not already included case
      userRec = userRepository.save(user);
    }

    Subscription subsRec = subsRepository.findSubscriptionByEventAndSubscriber(evt, userRec);

    if (subsRec != null) { // User is already subscibed case
      throw new SubscriptionConflictException(
          "User " + userRec.getName() + " is already subscribed on event " + evt.getTitle());
    }

    Subscription subs = new Subscription();

    subs.setEvent(evt);
    subs.setSubscriber(userRec);
    subs.setIndication(indicationUser);

    return subsRepository.save(subs);
  }

  public List<SubscriptionRankingItem> getRankingByEvent(String prettyName) {
    Event evt = eventRepository.findEventByPrettyName(prettyName);

    if (evt == null) {
      throw new EventNotFoundException("Event ranking " + prettyName + " does not exists");
    }

    return subsRepository.generateRanking(evt.getEventId());
  }

  public RankingStats getUserEventStats(String prettyName, Integer userId) {
    List<SubscriptionRankingItem> ranking = getRankingByEvent(prettyName);
    SubscriptionRankingItem item = ranking.stream().filter(i -> i.getUserId().equals(userId)).findFirst().orElse(null);

    if (item == null) {
      throw new UserIndicatorNotFoundException("There are no sign-ups for this user");
    }

    Integer posicao = IntStream.range(0, ranking.size())
        .filter(pos -> ranking.get(pos).getUserId().equals(userId))
        .findFirst().getAsInt();

    return new RankingStats(item.getSubscribers(), posicao + 1);
  }
}
