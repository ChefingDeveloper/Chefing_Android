package com.example.jinyoungkim.shefing.model.cart;

public class GetCartData {
    public int cart_id;
    public String cart_menu_image;
    public String cart_menu_name;
    public int cart_menu_count;
    public int cart_menu_total_price;
    public int menu_id;
    public int user_id;

    public GetCartData(int cart_id, String cart_menu_image, String cart_menu_name, int cart_menu_count, int cart_menu_total_price, int menu_id, int user_id) {
        this.cart_id = cart_id;
        this.cart_menu_image = cart_menu_image;
        this.cart_menu_name = cart_menu_name;
        this.cart_menu_count = cart_menu_count;
        this.cart_menu_total_price = cart_menu_total_price;
        this.menu_id = menu_id;
        this.user_id = user_id;
    }
}
