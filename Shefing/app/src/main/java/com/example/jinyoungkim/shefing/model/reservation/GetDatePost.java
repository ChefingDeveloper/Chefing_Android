package com.example.jinyoungkim.shefing.model.reservation;

public class GetDatePost {
    public int shop_id;
    public int seat_count;

    public GetDatePost(int shop_id, int seat_count) {
        this.shop_id = shop_id;
        this.seat_count = seat_count;
    }
}
