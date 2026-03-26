package com.zosh.service;

import com.zosh.model.User;

public interface UserService {
  User findUserByJwtToken(String jwt) throws Exception;
  User findUserByEmail(String email) throws Exception;
}
