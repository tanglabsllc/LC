package com.dahutu.samsara;

public class Valuable {
    long id;
    String name;

    public Valuable(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("id=%d, name=%s", id, name);
    }
}
