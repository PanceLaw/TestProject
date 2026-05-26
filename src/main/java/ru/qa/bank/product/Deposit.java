package ru.qa.bank.product;

import ru.qa.bank.exception.ProductClosedException;
import ru.qa.bank.model.Money;

public class Deposit extends AbstractBankProduct implements Replenishable, ClosableProduct {
    private boolean closed;

    public Deposit(String name, Money balance) {
        super(name, balance);
        this.closed = false;
    }

    @Override
    public void deposit(Money amount) {
        if (closed) {
            throw new ProductClosedException("Vklad zakryt");
        }

        validatePositiveMoney(amount);
        balance = balance.add(amount);
    }

    @Override
    public Money close() {
        if (closed) {
            throw new ProductClosedException("Vklad uzhe zakryt");
        }

        Money payout = balance;
        closed = true;
        balance = Money.zero(getCurrency());
        return payout;
    }

    @Override
    public boolean isClosed() {
        return closed;
    }
}
