package com.example.jinyoungkim.shefing.model.main;

public class MainListData {
    public int menu_id;
    public String menu_name;
    public int menu_price;
    public String menu_image;
    public String shop_location_name;
    public String chef_image;
    public String chef_name;
    public int chef_id;
    public int shop_id;

    public MainListData(int menu_id, String menu_name, int menu_price, String menu_image, String shop_location_name, String chef_image, String chef_name, int chef_id, int shop_id) {
        this.menu_id = menu_id;
        this.menu_name = menu_name;
        this.menu_price = menu_price;
        this.menu_image = menu_image;
        this.shop_location_name = shop_location_name;
        this.chef_image = chef_image;
        this.chef_name = chef_name;
        this.chef_id = chef_id;
        this.shop_id = shop_id;
    }
}
