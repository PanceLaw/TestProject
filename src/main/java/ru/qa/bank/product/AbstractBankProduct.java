package ru.qa.bank.product;

import ru.qa.bank.exception.InvalidAmountException;
import ru.qa.bank.model.Currency;

import java.math.BigDecimal;
import java.util.Objects;

public abstract class AbstractBankProduct implements BankProduct {
    private final String name;
    private final Currency currency;
    protected BigDecimal balance;

    protected AbstractBankProduct(String name, Currency currency, BigDecimal balance) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name must not be blank");
        }

        this.name = name;
        this.currency = Objects.requireNonNull(currency, "Currency must not be null");
        this.balance = validateInitialBalance(balance);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Currency getCurrency() {
        return currency;
    }

    @Override
    public BigDecimal getBalance() {
        return balance;
    }

    protected void validatePositiveAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be positive");
        }
    }

    private BigDecimal validateInitialBalance(BigDecimal balance) {
        if (balance == null || balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException("Initial balance must be zero or positive");
        }

        return balance;
    }
}
