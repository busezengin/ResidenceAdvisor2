package com.kotlinegitim.residenceadvisor.classes;

public class Message {
    private String message;
    private String sender;
    private String date;

    public Message(){

    }
    public Message(String message, String sender, String date) {
        this.message = message;
        this.sender = sender;
        this.date = date;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}