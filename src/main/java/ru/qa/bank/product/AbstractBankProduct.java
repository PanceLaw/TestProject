package ru.qa.bank.product;

import ru.qa.bank.exception.InvalidAmountException;
import ru.qa.bank.model.Currency;
import ru.qa.bank.model.Money;

import java.util.Objects;

public abstract class AbstractBankProduct implements BankProduct {
    private final String name;
    protected Money balance;

    protected AbstractBankProduct(String name, Money balance) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nazvanie produkta ne dolzhno byt pustym");
        }

        this.name = name;
        this.balance = Objects.requireNonNull(balance, "Balans ne dolzhen byt null");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Currency getCurrency() {
        return balance.currency();
    }

    @Override
    public Money getBalance() {
        return balance;
    }

    protected void validatePositiveMoney(Money amount) {
        if (amount == null || !amount.isGreaterThanZero()) {
            throw new InvalidAmountException("Summa dolzhna byt bolshe nulya");
        }
    }
}
