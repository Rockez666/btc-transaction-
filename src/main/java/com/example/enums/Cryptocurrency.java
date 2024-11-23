package com.example.enums;

import com.example.exception.CryptocurrencyNotFoundException;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Arrays;

@Getter
public enum Cryptocurrency {
    BTC("Bitcoin"),
    ETH("Ethereum"),
    PEPE("Pepe"),
    LADYS("Ladys meme coin"),
    WIF("Wif");

    private final String cryptoName;


    Cryptocurrency(String cryptoName) {
        this.cryptoName = cryptoName;
    }

    public static Cryptocurrency getCryptocurrency(String cryptoName) {
        return Arrays.stream(Cryptocurrency.values())
                .filter(crypto -> crypto.getCryptoName().equalsIgnoreCase(cryptoName))
                .findFirst()
                .orElseThrow(() -> new CryptocurrencyNotFoundException("Crypto is not found"));
    }
}
