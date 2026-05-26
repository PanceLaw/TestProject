package ru.qa.bank.product;

import ru.qa.bank.model.Money;

public interface Replenishable {
    void deposit(Money amount);
}
