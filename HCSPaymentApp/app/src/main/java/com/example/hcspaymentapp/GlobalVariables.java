package com.example.hcspaymentapp;

public class GlobalVariables {
    private static String id_user;
    private static String last_name;
    private static String first_name;
    private static String phone;
    private static String passport;
    private static String patronymic;
    private static String password;
    private static String email;

    public GlobalVariables() {}

    public GlobalVariables(String last_name, String first_name, String patronymic,
                           String phone, String passport, String email, String password) {
        this.last_name = last_name;
        this.first_name = first_name;
        this.patronymic = patronymic;
        this.phone = phone;
        this.passport = passport;
        this.password = password;
        this.email = email;
    }

    public static String getId_user() {
        return id_user;
    }

    public static void setId_user(String id_user) {
        GlobalVariables.id_user = id_user;
    }

    public static String getLast_name() {
        return last_name;
    }

    public static void setLast_name(String last_name) {
        GlobalVariables.last_name = last_name;
    }

    public static String getFirst_name() {
        return first_name;
    }

    public static void setFirst_name(String first_name) {
        GlobalVariables.first_name = first_name;
    }

    public static String getPhone() {
        return phone;
    }

    public static void setPhone(String phone) {
        GlobalVariables.phone = phone;
    }

    public static String getPassport() {
        return passport;
    }

    public static void setPassport(String passport) {
        GlobalVariables.passport = passport;
    }

    public static String getPatronymic() {
        return patronymic;
    }

    public static void setPatronymic(String patronymic) {
        GlobalVariables.patronymic = patronymic;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        GlobalVariables.password = password;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        GlobalVariables.email = email;
    }
}
