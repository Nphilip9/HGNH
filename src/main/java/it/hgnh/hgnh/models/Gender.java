package it.hgnh.hgnh.models;

public enum Gender {
    M("Male"),
    F("Female"),
    D("Diverse"),
    O("Other");

    private final String definition;

    Gender(String definition) {
        this.definition = definition;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
