package com.nespresso.sofa.interview.cart.services;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import com.nespresso.sofa.interview.cart.model.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    @Autowired
    private PromotionEngine promotionEngine;
    @Autowired
    private CartStorage cartStorage;
    /**
     * Add a quantity of a product to the cart and store the cart
     *
     * @param cartId      The cart ID
     * @param productCode The product code
     * @param quantity    Quantity must be added
     * @return True if card has been modified
     */
    public boolean add(UUID cartId, String productCode, int quantity) {
        final Cart cart = cartStorage.loadCart(cartId);
        if (quantity <= 0) {
            return false;
        }
        if (cart.getProducts().containsKey(productCode)) {
            int total = cart.getProducts().get(productCode) + quantity;
            cart.getProducts().put(productCode, total);
            cartStorage.saveCart(cart);
            return true;
        }
        cart.getProducts().put(productCode, quantity);
        cartStorage.saveCart( promotionEngine.apply(cart));
        return true;
    }

    /**
     * Set a quantity of a product to the cart and store the cart
     *
     * @param cartId      The cart ID
     * @param productCode The product code
     * @param quantity    The new quantity
     * @return True if card has been modified
     */
    public boolean set(UUID cartId, String productCode, int quantity) {
        final Cart cart = cartStorage.loadCart(cartId);
        if (cart.getProducts().containsKey(productCode) && cart.getProducts().get(productCode) == quantity) {
            return false;
        }
        if (quantity <= 0) {
            Map<String, Integer> produits = cart.getProducts();
            produits.remove(productCode);
            return true;
        }
        cart.getProducts().put(productCode, quantity);
        cartStorage.saveCart(cart);
        return true;
    }

    /**
     * Return the card with the corresponding ID
     *
     * @param cartId
     * @return
     */
    public Cart get(UUID cartId) {
        return cartStorage.loadCart(cartId);
    }

    @Override
    public String toString() {
        return "CartService {" +
            "promotionEngine: " + promotionEngine +
            ", cartStorage: " + cartStorage +
            '}';
    }
}