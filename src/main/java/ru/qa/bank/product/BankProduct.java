package ru.qa.bank.product;

import ru.qa.bank.model.Currency;

import java.math.BigDecimal;

public interface BankProduct {
    String getName();

    Currency getCurrency();

    BigDecimal getBalance();
}
