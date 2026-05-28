package ru.qa.bank.product.card;

import ru.qa.bank.exception.InsufficientFundsException;
import ru.qa.bank.exception.InvalidAmountException;
import ru.qa.bank.model.Currency;
import ru.qa.bank.product.AbstractCard;

import java.math.BigDecimal;

public class CreditCard extends AbstractCard {
    private final BigDecimal interestRate;
    private final BigDecimal creditLimit;
    private BigDecimal debt;

    public CreditCard(
            String name,
            Currency currency,
            BigDecimal balance,
            BigDecimal interestRate,
            BigDecimal creditLimit
    ) {
        super(name, currency, balance);
        validatePositiveRate(interestRate);
        validatePositiveAmount(creditLimit);
        this.interestRate = interestRate;
        this.creditLimit = creditLimit;
        this.debt = BigDecimal.ZERO;
    }

    @Override
    public void deposit(BigDecimal amount) {
        validatePositiveAmount(amount);

        if (debt.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal debtPayment = amount.min(debt);
            debt = debt.subtract(debtPayment);
            amount = amount.subtract(debtPayment);
        }

        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            balance = balance.add(amount);
        }
    }

    @Override
    public void withdraw(BigDecimal amount) {
        validatePositiveAmount(amount);

        if (balance.compareTo(amount) >= 0) {
            balance = balance.subtract(amount);
            return;
        }

        BigDecimal debtIncrease = amount.subtract(balance);
        BigDecimal newDebt = debt.add(debtIncrease);

        if (newDebt.compareTo(creditLimit) > 0) {
            throw new InsufficientFundsException("Credit limit exceeded");
        }

        balance = BigDecimal.ZERO;
        debt = newDebt;
    }

    public BigDecimal getDebt() {
        return debt;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public BigDecimal getAvailableFunds() {
        return balance.add(creditLimit).subtract(debt);
    }

    private void validatePositiveRate(BigDecimal interestRate) {
        if (interestRate == null || interestRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Interest rate must be positive");
        }
    }
}
