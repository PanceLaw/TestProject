package ru.qa.bank.producttests;

import ru.qa.bank.product.*;

import ru.qa.bank.exception.InsufficientFundsException;
import ru.qa.bank.exception.InvalidAmountException;
import ru.qa.bank.model.Currency;
import ru.qa.bank.model.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static ru.qa.bank.testdata.TestMoney.rub;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DebitCardTests {
    @Test
    void createCardTest() {
        DebitCard card = new DebitCard("Debit card", rub(1_000));

        assertEquals("Debit card", card.getName());
        assertEquals(Currency.RUB, card.getCurrency());
        assertEquals(rub(1_000), card.getBalance());
    }

    @Test
    void depositIncreasesBalanceTest() {
        DebitCard card = new DebitCard("Debit card", rub(1_000));

        card.deposit(rub(500));

        assertEquals(rub(1_500), card.getBalance());
    }

    @Test
    void withdrawDecreasesBalanceTest() {
        DebitCard card = new DebitCard("Debit card", rub(1_000));

        card.withdraw(rub(400));

        assertEquals(rub(600), card.getBalance());
    }

    @Test
    void withdrawOverBalanceThrowsTest() {
        DebitCard card = new DebitCard("Debit card", rub(1_000));

        assertThrows(InsufficientFundsException.class, () -> card.withdraw(rub(1_001)));
    }

    @Test
    void invalidAmountThrowsTest() {
        DebitCard card = new DebitCard("Debit card", rub(1_000));

        assertThrows(InvalidAmountException.class, () -> card.deposit(Money.zero(Currency.RUB)));
        assertThrows(InvalidAmountException.class, () -> card.withdraw(new Money(BigDecimal.valueOf(-100), Currency.RUB)));
    }
}

