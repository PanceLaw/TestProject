package ru.qa.bank.product;

import java.math.BigDecimal;

public interface ClosableProduct {
    BigDecimal close();

    boolean isClosed();
}
