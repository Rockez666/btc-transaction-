package com.example.enums;

import lombok.Getter;

@Getter
public enum Role {
    USER,
    ADMIN;

    public String getNameOfRole() {
        return this.name();
    }
}
