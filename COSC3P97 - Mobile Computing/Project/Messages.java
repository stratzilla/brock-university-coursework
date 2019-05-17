package com.example.b.bc;

public class Messages {
    String author;
    String room;
    String message;
    String timestamp;

    // constructors

    public Messages() {}

    public Messages(String author, String room, String message, String timestamp) {
        this.author = author;
        this.room = room;
        this.message = message;
        this.timestamp = timestamp;
    }

    // getter methods

    public String getAuthor() {
        return author;
    }

    public String getRoom() {
        return room;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() { return timestamp; }
}
