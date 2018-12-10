package com.example.jinyoungkim.shefing.ui.mypage.reservation_tab;

public class ReservationItem {
    public int image_reservation;
    public String name_reservation;
    public String chef_reservation;
    public int confirm_reservation;
    public String date_reservation;
    public String time_reservation;

    public ReservationItem(int image_reservation, String name_reservation, String chef_reservation, int confirm_reservation, String date_reservation, String time_reservation) {
        this.image_reservation = image_reservation;
        this.name_reservation = name_reservation;
        this.chef_reservation = chef_reservation;
        this.confirm_reservation = confirm_reservation;
        this.date_reservation = date_reservation;
        this.time_reservation = time_reservation;
    }
}
