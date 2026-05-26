package ru.qa.bank.product;

import ru.qa.bank.model.Money;

public interface ClosableProduct {
    Money close();

    boolean isClosed();
}
