package com.nespresso.sofa.interview.cart.services;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.nespresso.sofa.interview.cart.model.Cart;

/**
 * If one or more product with code "1000" is purchase, ONE product with code 9000 is offer
 * For each 10 products purchased a gift with code 7000 is offer.
 */
@Service
public class PromotionEngine {
    public Cart apply(Cart cart) {
        Map<String, Integer> products = new HashMap<>(cart.getProducts());
        int nbOfProducts = 0;
        for (int value: products.values()) {
            nbOfProducts += value;
        }
        if(products.containsKey("1000")){
            products.put("9000", 1);
        }
        if(products.size() == 1 && products.containsKey("9000")){
            products.remove("9000");
        }
        if(nbOfProducts >= 10){
            int quantity = nbOfProducts / 10;
            products.put("7000", quantity);
        }
        if(nbOfProducts < 10 && products.containsKey("7000")){
            products.remove("7000");
        }
        cart.setProducts(products);
        return cart;
    }
}
