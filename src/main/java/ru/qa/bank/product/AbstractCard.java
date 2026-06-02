package ru.qa.bank.product;

import ru.qa.bank.model.Currency;

import java.math.BigDecimal;

public abstract class AbstractCard extends AbstractBankProduct implements Replenishable, Withdrawable {
    protected AbstractCard(String name, Currency currency, BigDecimal balance) {
        super(name, currency, balance);
    }

    @Override
    public void deposit(BigDecimal amount) {
        increaseBalance(amount);
    }
}
