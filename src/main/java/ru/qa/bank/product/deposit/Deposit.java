package ru.qa.bank.product.deposit;

import ru.qa.bank.exception.ProductClosedException;
import ru.qa.bank.model.Currency;
import ru.qa.bank.product.AbstractBankProduct;
import ru.qa.bank.product.ClosableProduct;
import ru.qa.bank.product.Replenishable;

import java.math.BigDecimal;

public class Deposit extends AbstractBankProduct implements Replenishable, ClosableProduct {
    private boolean closed;

    public Deposit(String name, Currency currency, BigDecimal balance) {
        super(name, currency, balance);
        this.closed = false;
    }

    @Override
    public void deposit(BigDecimal amount) {
        if (closed) {
            throw new ProductClosedException("Deposit is closed");
        }

        validatePositiveAmount(amount);
        increaseBalance(amount);
    }

    @Override
    public BigDecimal close() {
        if (closed) {
            throw new ProductClosedException("Deposit is already closed");
        }

        BigDecimal payout = getBalance();
        closed = true;
        setBalance(BigDecimal.ZERO);
        return payout;
    }

    @Override
    public boolean isClosed() {
        return closed;
    }
}
