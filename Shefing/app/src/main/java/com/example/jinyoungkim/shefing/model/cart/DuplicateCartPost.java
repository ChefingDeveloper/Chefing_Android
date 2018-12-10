package com.example.jinyoungkim.shefing.model.cart;

public class DuplicateCartPost {
    public int menu_id;
    public long user_id;

    public DuplicateCartPost(int menu_id, long user_id) {
        this.menu_id = menu_id;
        this.user_id = user_id;
    }
}
