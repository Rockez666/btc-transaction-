package com.example.enums;

import com.example.exception.CryptocurrencyNotFoundException;
import lombok.Data;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Roles {
    USER(),
    ADMIN()
}
