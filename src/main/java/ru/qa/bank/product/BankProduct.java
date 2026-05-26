package ru.qa.bank.product;

import ru.qa.bank.model.Currency;
import ru.qa.bank.model.Money;

public interface BankProduct {
    String getName();

    Currency getCurrency();

    Money getBalance();
}
