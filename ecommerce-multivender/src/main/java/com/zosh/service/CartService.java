package com.zosh.service;

import com.zosh.model.Cart;
import com.zosh.model.CartItem;
import com.zosh.model.Product;
import com.zosh.model.User;
import org.springframework.stereotype.Service;


public interface CartService {
    public CartItem addCartItem(User user, Product product, String size, int quantity);
public Cart findUserCart(User user);




}
