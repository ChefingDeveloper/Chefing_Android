package com.example.jinyoungkim.shefing.model.cart;

public class AddCartPost {
    public String menu_name;
    public int menu_count;
    public int menu_total_price;
    public int menu_id;
    public long user_id;
    public String menu_image;

    public AddCartPost(String menu_name, int menu_count, int menu_total_price, int menu_id, long user_id, String menu_image) {
        this.menu_name = menu_name;
        this.menu_count = menu_count;
        this.menu_total_price = menu_total_price;
        this.menu_id = menu_id;
        this.user_id = user_id;
        this.menu_image = menu_image;
    }
}
