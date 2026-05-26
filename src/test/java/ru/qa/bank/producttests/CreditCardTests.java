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

class CreditCardTests {
    @Test
    void createCardTest() {
        CreditCard card = creditCard();

        assertEquals("Credit card", card.getName());
        assertEquals(Currency.RUB, card.getCurrency());
        assertEquals(rub(1_000), card.getBalance());
        assertEquals(BigDecimal.valueOf(19.9), card.getInterestRate());
        assertEquals(rub(5_000), card.getCreditLimit());
        assertEquals(Money.zero(Currency.RUB), card.getDebt());
        assertEquals(rub(6_000), card.getAvailableFunds());
    }

    @Test
    void withdrawOwnMoneyTest() {
        CreditCard card = creditCard();

        card.withdraw(rub(300));

        assertEquals(rub(700), card.getBalance());
        assertEquals(Money.zero(Currency.RUB), card.getDebt());
        assertEquals(rub(5_700), card.getAvailableFunds());
    }

    @Test
    void withdrawCreditMoneyTest() {
        CreditCard card = creditCard();

        card.withdraw(rub(1_500));

        assertEquals(Money.zero(Currency.RUB), card.getBalance());
        assertEquals(rub(500), card.getDebt());
        assertEquals(rub(4_500), card.getAvailableFunds());
    }

    @Test
    void depositRepaysDebtFirstTest() {
        CreditCard card = creditCard();

        card.withdraw(rub(1_500));
        card.deposit(rub(700));

        assertEquals(rub(200), card.getBalance());
        assertEquals(Money.zero(Currency.RUB), card.getDebt());
        assertEquals(rub(5_200), card.getAvailableFunds());
    }

    @Test
    void withdrawOverLimitThrowsTest() {
        CreditCard card = creditCard();

        assertThrows(InsufficientFundsException.class, () -> card.withdraw(rub(6_001)));
    }

    @Test
    void zeroInterestRateThrowsTest() {
        assertThrows(InvalidAmountException.class, () -> new CreditCard(
                "Credit card",
                rub(1_000),
                BigDecimal.ZERO,
                rub(5_000)
        ));
    }

    @Test
    void zeroCreditLimitThrowsTest() {
        assertThrows(InvalidAmountException.class, () -> new CreditCard(
                "Credit card",
                rub(1_000),
                BigDecimal.valueOf(19.9),
                Money.zero(Currency.RUB)
        ));
    }

    private CreditCard creditCard() {
        return new CreditCard(
                "Credit card",
                rub(1_000),
                BigDecimal.valueOf(19.9),
                rub(5_000)
        );
    }
}

