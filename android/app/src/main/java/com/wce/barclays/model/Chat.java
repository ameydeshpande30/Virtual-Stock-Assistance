package com.wce.barclays.model;

public class Chat {
    private int side;
    private String text;

    public Chat(int side, String text) {
        this.side = side;
        this.text = text;
    }

    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
