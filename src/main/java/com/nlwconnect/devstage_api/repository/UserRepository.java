package com.nlwconnect.devstage_api.repository;

import org.springframework.data.repository.CrudRepository;

import com.nlwconnect.devstage_api.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {
  public User findUserByEmail(String Email);
}
