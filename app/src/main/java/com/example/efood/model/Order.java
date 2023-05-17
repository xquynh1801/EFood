package com.example.efood.model;

import java.io.Serializable;

public class Order implements Serializable {
    private int id;
    private String date, status;
    private int isPaid;
    private String note;
    private double total;
    private User user;

    public Order() {
    }

    public Order(int id, String date, String status, int isPaid, String note, double total, User user) {
        this.id = id;
        this.date = date;
        this.status = status;
        this.isPaid = isPaid;
        this.note = note;
        this.total = total;
        this.user=user;
    }

    public Order(String date, String status, int isPaid, String note, double total,User user) {
        this.date = date;
        this.status = status;
        this.isPaid = isPaid;
        this.note = note;
        this.total = total;
        this.user=user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(int isPaid) {
        this.isPaid = isPaid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
