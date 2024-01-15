package com.tms.skv.registration_platform.domain;

public enum Sex {
    MAN("Мужской"),
    WOMAN("Женский");

    private final String russianName;

    Sex(String russianName) {
        this.russianName = russianName;
    }

    public String getRussianName() {
        return russianName;
    }
}
