package com.example.androidlabs.models;

public class Message {

    private final Long id;
    private final String text;
    private final Type type;

    /**
     * Create a new message without an id. Usually this means that the object is not persisted yet.
     *
     * @param text text
     * @param type type
     */
    public Message(String text, Type type) {
        this.id = null;
        this.text = text;
        this.type = type;
    }

    /**
     * Create a new message with an id. Usually this means that it was retrieved from the database.
     *
     * @param id   id
     * @param text text
     * @param type type
     */
    public Message(Long id, String text, Type type) {
        this.id = id;
        this.text = text;
        this.type = type;
    }

    /**
     * Create a new message from a previous message, with an id. Usually this means we just inserted
     * the object into the db, and we want to assign an id.
     *
     * @param id      id
     * @param message message
     */
    public Message(Long id, Message message) {
        this.id = id;
        this.text = message.getText();
        this.type = message.getType();
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        SENT,
        RECEIVED
    }
}
