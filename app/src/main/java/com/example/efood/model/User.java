package com.example.efood.model;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String phoneNumber;
    private String password;
    private String fullName;
    private int age;
    private String address;
    private String role;

    public User() {
    }

    public User(int id, String phoneNumber, String password, String fullName, int age, String address, String role) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.fullName = fullName;
        this.age = age;
        this.address = address;
        this.role = role;
    }

    public User(String phoneNumber, String password, String fullName, int age, String address, String role) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.fullName = fullName;
        this.age = age;
        this.address = address;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
