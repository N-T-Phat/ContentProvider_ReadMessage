package com.example.danhbatinnhan.model;

public class Message {
    private String sender;
    private String body;

    public Message(String body, String sender) {
        this.body = body;
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Từ: "+sender+"\nTin nhắn: "+body;
    }
}
