package ru.qa.bank.producttests;

import ru.qa.bank.product.*;

import ru.qa.bank.exception.CurrencyMismatchException;
import ru.qa.bank.exception.InvalidAmountException;
import ru.qa.bank.model.Currency;
import ru.qa.bank.model.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.qa.bank.testdata.TestMoney.money;
import static ru.qa.bank.testdata.TestMoney.rub;

class BankProductValidationTests {
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    void blankNameThrowsTest(String name) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DebitCard(name, rub(10))
        );

        assertEquals("Nazvanie produkta ne dolzhno byt pustym", exception.getMessage());
    }

    @Test
    void nullCurrencyThrowsTest() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Money(BigDecimal.TEN, null)
        );

        assertEquals("Valyuta ne dolzhna byt null", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1", "-100.50"})
    void negativeMoneyThrowsTest(String amount) {
        InvalidAmountException exception = assertThrows(
                InvalidAmountException.class,
                () -> new Money(new BigDecimal(amount), Currency.RUB)
        );

        assertEquals("Summa ne mozhet byt otricatelnoy", exception.getMessage());
    }

    @Test
    void zeroDepositAmountThrowsTest() {
        DebitCard card = new DebitCard("Debit card", rub(10));

        InvalidAmountException exception = assertThrows(
                InvalidAmountException.class,
                () -> card.deposit(new Money(BigDecimal.ZERO, Currency.RUB))
        );

        assertEquals("Summa dolzhna byt bolshe nulya", exception.getMessage());
    }

    @Test
    void moneyScaleIsNormalizedTest() {
        assertEquals(
                money("10.00", Currency.RUB),
                money("10.0", Currency.RUB)
        );
    }

    @Test
    void nullDepositAmountThrowsTest() {
        DebitCard card = new DebitCard("Debit card", rub(10));

        InvalidAmountException exception = assertThrows(
                InvalidAmountException.class,
                () -> card.deposit(null)
        );

        assertEquals("Summa dolzhna byt bolshe nulya", exception.getMessage());
    }

    @Test
    void wrongCurrencyThrowsTest() {
        DebitCard card = new DebitCard("Debit card", rub(10));

        CurrencyMismatchException exception = assertThrows(
                CurrencyMismatchException.class,
                () -> card.deposit(new Money(BigDecimal.ONE, Currency.USD))
        );

        assertEquals("Valyuty denezhnyh summ dolzhny sovpadat", exception.getMessage());
    }
}

