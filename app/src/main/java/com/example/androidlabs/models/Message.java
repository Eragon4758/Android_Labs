package com.example.androidlabs.models;

public class Message {

  private final String text;
  private final Type type;
  private final long ID;

  public Message( Type type,String messageText, long ID) {
    this.text = messageText;
    this.type = type;
    this.ID = ID;
  }

  public String getText() {
    return text;
  }

  public Type getType() {
    return type;
  }

  public long getID() { return ID; }

  public enum Type {
    SENT,
    RECEIVED
  }
}
