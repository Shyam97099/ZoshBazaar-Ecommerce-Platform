package com.zosh.controller;
import com.zosh.model.Cart;
import com.zosh.model.Coupon;
import com.zosh.model.User;
import com.zosh.service.CartService;
import com.zosh.service.CouponService;
import com.zosh.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminCouponController {
    private final CouponService couponService;
    private final UserService userService;
    private final CartService cartService;


@PostMapping("/apply/coupons")
public ResponseEntity<Cart> createCoupon(
        @RequestParam String apply,
        @RequestParam String code,
        @RequestParam double orderValue,
        @RequestHeader("Authorization")
        String jwt) throws Exception{
    User user = userService.findUserByJwtToken(jwt);
    Cart cart;
    if(apply.equals("true")){
        cart = couponService.applyCoupon(code,orderValue,user);
    }
    else {
        cart = couponService.removeCoupon(code,user);
    }
    return ResponseEntity.ok(cart);
}

    @PostMapping("/admin/create")
    public ResponseEntity<Coupon> createCoupon(
            @RequestBody Coupon coupon){
    Coupon createCoupon = couponService.createCoupon(coupon);
    return ResponseEntity.ok(createCoupon);
}
    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<?> deleteCoupon(@PathVariable Long id) throws Exception{
    couponService.deleteCoupon(id);
    return ResponseEntity.ok("Coupon deleted successfully");
    }

    @GetMapping("/admin/all")
    public ResponseEntity<List<Coupon>> getAllCoupons(){
    List<Coupon> coupons = couponService.findAllCoupons();
    return ResponseEntity.ok(coupons);

}

}
