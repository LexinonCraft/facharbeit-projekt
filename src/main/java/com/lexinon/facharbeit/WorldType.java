package com.lexinon.facharbeit;

public enum WorldType {
    TERRAIN("Terrain"),
    EMPTY("Empty"),
    FLAT("Flat");

    private String string;

    WorldType(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }
}
