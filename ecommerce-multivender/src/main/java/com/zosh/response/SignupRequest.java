package com.zosh.response;

import lombok.Data;

@Data
public class SignupRequest {
    private String fullName;
    private String email;
    private String otp;

}
