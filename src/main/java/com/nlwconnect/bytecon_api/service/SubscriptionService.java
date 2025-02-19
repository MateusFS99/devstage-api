package com.nlwconnect.bytecon_api.service;

import java.util.List;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nlwconnect.bytecon_api.dto.SubscriptionRankingByUser;
import com.nlwconnect.bytecon_api.dto.SubscriptionRankingItem;
import com.nlwconnect.bytecon_api.dto.SubscriptionResponse;
import com.nlwconnect.bytecon_api.exception.EventNotFoundException;
import com.nlwconnect.bytecon_api.exception.SubscriptionConflictException;
import com.nlwconnect.bytecon_api.exception.UserIndicatorNotFoundException;
import com.nlwconnect.bytecon_api.model.Event;
import com.nlwconnect.bytecon_api.model.Subscription;
import com.nlwconnect.bytecon_api.model.User;
import com.nlwconnect.bytecon_api.repository.EventRepository;
import com.nlwconnect.bytecon_api.repository.SubscriptionRepository;
import com.nlwconnect.bytecon_api.repository.UserRepository;

@Service
public class SubscriptionService {
  @Autowired
  private EventRepository eventRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private SubscriptionRepository subsRepository;

  public SubscriptionResponse createNewSubscription(String eventName, User user, Integer indicationUserId) {
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

    Subscription res = subsRepository.save(subs);

    return new SubscriptionResponse(res.getSubscriptionNumber(),
        "http://bytecon.com/subscription/" + res.getEvent().getPrettyName() + "/" + res.getSubscriber().getId());
  }

  public List<SubscriptionRankingItem> getRankingByEvent(String prettyName) {
    Event evt = eventRepository.findEventByPrettyName(prettyName);

    if (evt == null) {
      throw new EventNotFoundException("Event ranking " + prettyName + " does not exists");
    }

    return subsRepository.generateRanking(evt.getEventId());
  }

  public SubscriptionRankingByUser getRankingByEventAndUser(String prettyName, Integer userId) {
    List<SubscriptionRankingItem> ranking = getRankingByEvent(prettyName);
    SubscriptionRankingItem item = ranking.stream().filter(i -> i.userId().equals(userId)).findFirst().orElse(null);

    if (item == null) {
      throw new UserIndicatorNotFoundException("There are no sign-ups for this user");
    }

    Integer posicao = IntStream.range(0, ranking.size())
        .filter(pos -> ranking.get(pos).userId().equals(userId))
        .findFirst().getAsInt();

    return new SubscriptionRankingByUser(item, posicao + 1);
  }
}
