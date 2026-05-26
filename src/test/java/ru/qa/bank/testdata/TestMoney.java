package ru.qa.bank.testdata;

import ru.qa.bank.model.Currency;
import ru.qa.bank.model.Money;

import java.math.BigDecimal;

public final class TestMoney {
    private TestMoney() {
    }

    public static Money money(long amount, Currency currency) {
        return new Money(BigDecimal.valueOf(amount), currency);
    }

    public static Money money(String amount, Currency currency) {
        return new Money(new BigDecimal(amount), currency);
    }

    public static Money rub(long amount) {
        return money(amount, Currency.RUB);
    }
}

