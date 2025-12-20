package com.detective.game.model;

public class Statement {
    private final String id;
    private final String text;

    public Statement(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public String getId() { return id; }
    public String getText() { return text; }

    @Override
    public String toString() { return text; }
}
