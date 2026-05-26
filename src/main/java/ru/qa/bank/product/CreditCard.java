package ru.qa.bank.product;

import ru.qa.bank.exception.InsufficientFundsException;
import ru.qa.bank.exception.InvalidAmountException;
import ru.qa.bank.model.Money;

import java.math.BigDecimal;

public class CreditCard extends AbstractCard {
    private final BigDecimal interestRate;
    private final Money creditLimit;
    private Money debt;

    public CreditCard(
            String name,
            Money balance,
            BigDecimal interestRate,
            Money creditLimit
    ) {
        super(name, balance);
        validatePositiveRate(interestRate);
        validatePositiveMoney(creditLimit);
        balance.requireSameCurrency(creditLimit);
        this.interestRate = interestRate;
        this.creditLimit = creditLimit;
        this.debt = Money.zero(getCurrency());
    }

    @Override
    public void deposit(Money amount) {
        validatePositiveMoney(amount);

        if (debt.isGreaterThanZero()) {
            Money debtPayment = amount.min(debt);
            debt = debt.subtract(debtPayment);
            amount = amount.subtract(debtPayment);
        }

        if (amount.isGreaterThanZero()) {
            balance = balance.add(amount);
        }
    }

    @Override
    public void withdraw(Money amount) {
        validatePositiveMoney(amount);

        if (!balance.isLessThan(amount)) {
            balance = balance.subtract(amount);
            return;
        }

        Money debtIncrease = amount.subtract(balance);
        Money newDebt = debt.add(debtIncrease);
        if (newDebt.isGreaterThan(creditLimit)) {
            throw new InsufficientFundsException("Previshen kreditny limit");
        }

        balance = Money.zero(getCurrency());
        debt = newDebt;
    }

    public Money getDebt() {
        return debt;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public Money getCreditLimit() {
        return creditLimit;
    }

    public Money getAvailableFunds() {
        return balance.add(creditLimit).subtract(debt);
    }

    private void validatePositiveRate(BigDecimal interestRate) {
        if (interestRate == null || interestRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Procentnaya stavka dolzhna byt bolshe nulya");
        }
    }
}
