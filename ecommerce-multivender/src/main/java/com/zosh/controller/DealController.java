package com.zosh.controller;

import com.zosh.model.Deal;
import com.zosh.response.ApiResponse;
import com.zosh.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/deals")
public class DealController {
    private final DealService dealService;

    @PostMapping()
    public ResponseEntity<Deal> createDeals(@RequestBody Deal deal)
            throws Exception{
        Deal newDeal = dealService.createDeal(deal);
        return new  ResponseEntity<>(newDeal,HttpStatus.ACCEPTED);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Deal>  updateDeal(
            @PathVariable Long id,
            @RequestBody Deal deal)
            throws Exception {
        Deal newDeal = dealService.updateDeal(deal, id);
        return  ResponseEntity.ok(newDeal);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse>  deleteDeal(@PathVariable Long id)
            throws Exception {
        dealService.deleteDeal(id);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("deal deleted");
        return ResponseEntity.ok(apiResponse);
    }
}
