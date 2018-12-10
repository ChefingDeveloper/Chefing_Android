package com.example.jinyoungkim.shefing.ui.cart;

public class CartItem {


    public int cart_image_menu;
    public String cart_name_menu;
    public int cart_count_menu;
    public int cart_price_menu;

    public CartItem(int cart_image_menu, String cart_name_menu, int cart_count_menu, int cart_price_menu) {
        this.cart_image_menu = cart_image_menu;
        this.cart_name_menu = cart_name_menu;
        this.cart_count_menu = cart_count_menu;
        this.cart_price_menu = cart_price_menu;
    }
}
