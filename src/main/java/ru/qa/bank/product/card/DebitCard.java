package ru.qa.bank.product.card;

import ru.qa.bank.exception.InsufficientFundsException;
import ru.qa.bank.model.Currency;
import ru.qa.bank.product.AbstractCard;

import java.math.BigDecimal;

public class DebitCard extends AbstractCard {
    public DebitCard(String name, Currency currency, BigDecimal balance) {
        super(name, currency, balance);
    }

    @Override
    public void withdraw(BigDecimal amount) {
        validatePositiveAmount(amount);

        if (balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        balance = balance.subtract(amount);
    }
}
