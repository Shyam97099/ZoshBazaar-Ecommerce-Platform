package com.zosh.service;

import com.zosh.domain.USER_ROLE;
import com.zosh.response.AuthResponse;
import com.zosh.response.SignupRequest;
import com.zosh.request.LoginRequest;

public interface AuthService {
    void sentLoginOtp(String email, USER_ROLE role) throws Exception;
    String createUser(SignupRequest req) throws Exception;
    AuthResponse signing(LoginRequest req) throws Exception;
}
