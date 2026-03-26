package com.zosh.controller;

//import com.zosh.response.ApiResponse;
import com.zosh.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping
   public ApiResponse HomeMicrocontroller(){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Welcome to the Multi - vender");
       return apiResponse;

}}
