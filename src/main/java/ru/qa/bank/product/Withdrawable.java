package ru.qa.bank.product;

import java.math.BigDecimal;

public interface Withdrawable {
    void withdraw(BigDecimal amount);
}
