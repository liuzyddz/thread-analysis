package com.haotian.analysis.ui.config;

public class Item {
    private String name;
    private int value;

    public Item() {}

    public Item(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode() + this.value;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Item && this.name.equals(((Item) obj).name) && this.value == ((Item) obj).value;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
