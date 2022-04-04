package com.example.hcspaymentapp;

public class Counter {
    public final long id;
    public final String view_number;
    public final String made_in;
    public final String when_made;
    public final String number;
    public final String date_view;
    public final int result;
    public final int pay_month;
    public final int color;

    public Counter(long id, String view_number, String made_in, String when_made,
                   String number, String date_view, int result, int pay_month, int color) {
        this.id = id;
        this.view_number = view_number;
        this.made_in = made_in;
        this.when_made = when_made;
        this.number = number;
        this.date_view = date_view;
        this.result = result;
        this.pay_month = pay_month;
        this.color = color;
    }
}
