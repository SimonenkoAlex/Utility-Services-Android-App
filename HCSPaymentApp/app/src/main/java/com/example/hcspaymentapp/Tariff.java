package com.example.hcspaymentapp;

public class Tariff {
    public final long tariff_id;
    public final String tariff_name;
    public final double tariff_tariff;

    public Tariff(long tariff_id, String tariff_name, double tariff_tariff) {
        this.tariff_id = tariff_id;
        this.tariff_name = tariff_name;
        this.tariff_tariff = tariff_tariff;
    }
}
