package com.kotlinegitim.residenceadvisor.classes;

public class User {
    private String firstname;
    private String lastname;
    private String email;
    private String apartmentID;
    private String apartmentNumber;
    private String role;
    private Integer budget;
    private String lastDuesPaymentDate;

    public User(String firstname, String lastname, String email, String apartmentID, String apartmentNumber, String role, Integer budget, String lastDuesPaymentDate) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.apartmentID = apartmentID;
        this.apartmentNumber = apartmentNumber;
        this.role = role;
        this.budget = budget;
        this.lastDuesPaymentDate = lastDuesPaymentDate;
    }

    public User(){}

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getApartmentID() {
        return apartmentID;
    }

    public void setApartmentID(String apartmentID) {
        this.apartmentID = apartmentID;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public String getLastDuesPaymentDate() {
        return lastDuesPaymentDate;
    }

    public void setLastDuesPaymentDate(String lastDuesPaymentDate) {
        this.lastDuesPaymentDate = lastDuesPaymentDate;
    }
}
