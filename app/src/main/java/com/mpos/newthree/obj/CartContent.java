package com.mpos.newthree.obj;

/**
 * Created by HP on 21/11/2017.
 */

public class CartContent {

    private Cart cart;

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public class Cart{
        String name, amount;

        public Cart(String name, String amount) {
            this.name = name;
            this.amount = amount;
        }

        public String getName() {
            return name;
        }

        public String getAmount() {
            return amount;
        }
    }
}
