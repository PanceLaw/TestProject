package ru.qa.bank.product;

import ru.qa.bank.model.Currency;
import ru.qa.bank.model.Money;

public class CurrencyDebitCard extends DebitCard {
    public CurrencyDebitCard(String name, Money balance) {
        super(name, balance);

        if (balance.currency() == Currency.RUB) {
            throw new IllegalArgumentException("Valyutnaya karta ne mozhet byt v RUB");
        }
    }
}
