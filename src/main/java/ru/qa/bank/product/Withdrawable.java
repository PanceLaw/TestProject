package ru.qa.bank.product;

import ru.qa.bank.model.Money;

public interface Withdrawable {
    void withdraw(Money amount);
}
