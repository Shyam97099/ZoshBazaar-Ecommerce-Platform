package com.zosh.service.serviceImpl;

import com.zosh.model.Cart;
import com.zosh.model.Coupon;
import com.zosh.model.User;
import com.zosh.repository.CartRepository;
import com.zosh.repository.CouponRepository;
import com.zosh.repository.UserRepository;
import com.zosh.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Override
    public Cart applyCoupon(String code, double orderValue, User user) throws Exception {
    Coupon coupon = couponRepository.findByCode(code);
    Cart cart = cartRepository.findByUserId(user.getId());
    if(coupon==null){
        throw new Exception("Coupon code not valid...");
    }
    if(user.getUsedCoupons().contains(coupon)){
        throw new Exception("Coupon already used...");
    }
    if(orderValue< coupon.getMinimumOrderValue()){
        throw new Exception("Minimum Order Value is less than coupon Minimum Order Value... "+coupon.getMinimumOrderValue());
    }
    if(coupon.isActive()
            && LocalDate.now().isAfter(coupon.getValidityStartDate())
            && LocalDate.now().isBefore(coupon.getValidityEndDate())){
        user.getUsedCoupons().add(coupon);
        userRepository.save(user);

        double discountedPrice = (cart.getTotalSellingPrice()*coupon.getDiscountPercentage())/100;
        cart.setTotalSellingPrice(cart.getTotalSellingPrice()-discountedPrice);
        cart.setCouponCode(code);
        cartRepository.save(cart);
        return cart;
    }
       throw new Exception("Coupon code not valid...");
    }

    @Override
    public Cart removeCoupon(String code, User user) throws Exception {
        Coupon coupon = couponRepository.findByCode(code);
        if(coupon==null){
            throw new Exception("coupon not found.");
        }
        Cart cart = cartRepository.findByUserId(user.getId());

        double discountedPrice = (cart.getTotalSellingPrice()*coupon.getDiscountPercentage())/100;
        cart.setTotalSellingPrice(cart.getTotalSellingPrice()+discountedPrice);
        cart.setCouponCode(null);
        cartRepository.save(cart);
        return cart;
    }

    @Override
    public Coupon findCouponById(Long id) throws Exception {

        return couponRepository.findById(id).orElseThrow(()->
                new Exception("coupon not found..") );
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public List<Coupon> findAllCoupons() {
        return couponRepository.findAll();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCoupon(Long id) throws Exception {
        findCouponById(id);
        couponRepository.deleteById(id);

    }
}
