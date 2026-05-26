package ru.qa.bank.model;

import ru.qa.bank.exception.CurrencyMismatchException;
import ru.qa.bank.exception.InvalidAmountException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(BigDecimal amount, Currency currency) {
    public Money {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException("Summa ne mozhet byt otricatelnoy");
        }

        amount = amount.setScale(2, RoundingMode.HALF_UP);
        Objects.requireNonNull(currency, "Valyuta ne dolzhna byt null");
    }

    public static Money zero(Currency currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    public Money add(Money other) {
        requireSameCurrency(other);
        return new Money(amount.add(other.amount), currency);
    }

    public Money subtract(Money other) {
        requireSameCurrency(other);
        return new Money(amount.subtract(other.amount), currency);
    }

    public Money min(Money other) {
        requireSameCurrency(other);
        return amount.compareTo(other.amount) <= 0 ? this : other;
    }

    public boolean isLessThan(Money other) {
        requireSameCurrency(other);
        return amount.compareTo(other.amount) < 0;
    }

    public boolean isGreaterThan(Money other) {
        requireSameCurrency(other);
        return amount.compareTo(other.amount) > 0;
    }

    public boolean isGreaterThanZero() {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public void requireSameCurrency(Money other) {
        Objects.requireNonNull(other, "Denezhnaya summa ne dolzhna byt null");

        if (currency != other.currency) {
            throw new CurrencyMismatchException("Valyuty denezhnyh summ dolzhny sovpadat");
        }
    }
}
