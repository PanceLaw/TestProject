package ru.qa.bank.product.card;

import ru.qa.bank.model.Currency;

import java.math.BigDecimal;

public class CurrencyDebitCard extends DebitCard {
    public CurrencyDebitCard(String name, Currency currency, BigDecimal balance) {
        super(name, currency, balance);

        if (currency == Currency.RUB) {
            throw new IllegalArgumentException("Currency debit card cannot use RUB");
        }
    }
}
