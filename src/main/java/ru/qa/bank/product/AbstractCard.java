package ru.qa.bank.product;

import ru.qa.bank.model.Money;

public abstract class AbstractCard extends AbstractBankProduct implements Replenishable, Withdrawable {
    protected AbstractCard(String name, Money balance) {
        super(name, balance);
    }

    @Override
    public void deposit(Money amount) {
        validatePositiveMoney(amount);
        balance = balance.add(amount);
    }
}
