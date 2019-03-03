package com.jesulonimi.user.sqlitequizzapp;

public class Category {
    public static final int PROGRAMMING=1;
    public static final int GEOGRAPHY=2;
    public static final int MATH=3;

    public Category() {
    }
    String name;
int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return getName();
    }
}
