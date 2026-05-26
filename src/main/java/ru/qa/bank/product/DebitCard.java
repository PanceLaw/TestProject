package ru.qa.bank.product;

import ru.qa.bank.exception.InsufficientFundsException;
import ru.qa.bank.model.Money;

public class DebitCard extends AbstractCard {
    public DebitCard(String name, Money balance) {
        super(name, balance);
    }

    @Override
    public void withdraw(Money amount) {
        validatePositiveMoney(amount);

        if (balance.isLessThan(amount)) {
            throw new InsufficientFundsException("Nedostatochno sredstv");
        }

        balance = balance.subtract(amount);
    }
}
