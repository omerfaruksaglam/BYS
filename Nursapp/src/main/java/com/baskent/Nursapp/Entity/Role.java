package com.baskent.Nursapp.Entity;

public enum Role {
    TEACHER(0),
    STUDENT(1),
    SECOND_GRADER(2),
    ADMIN(3);

    private final int value;

    Role(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Role fromValue(int value) {
        for (Role role : Role.values()) {
            if (role.getValue() == value) {
                return role;
            }
        }
        throw new IllegalArgumentException("Geçersiz rol ID: " + value);
    }
}
