package ru.qa.bank.producttests;

import ru.qa.bank.product.*;

import ru.qa.bank.exception.InvalidAmountException;
import ru.qa.bank.exception.ProductClosedException;
import ru.qa.bank.model.Currency;
import ru.qa.bank.model.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static ru.qa.bank.testdata.TestMoney.rub;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DepositTests {
    @Test
    void createDepositTest() {
        Deposit deposit = new Deposit("Deposit", rub(10_000));

        assertEquals("Deposit", deposit.getName());
        assertEquals(Currency.RUB, deposit.getCurrency());
        assertEquals(rub(10_000), deposit.getBalance());
        assertFalse(deposit.isClosed());
    }

    @Test
    void depositIncreasesBalanceTest() {
        Deposit deposit = new Deposit("Deposit", rub(10_000));

        deposit.deposit(rub(5_000));

        assertEquals(rub(15_000), deposit.getBalance());
    }

    @Test
    void closeReturnsPayoutTest() {
        Deposit deposit = new Deposit("Deposit", rub(10_000));

        Money payout = deposit.close();

        assertTrue(deposit.isClosed());
        assertEquals(rub(10_000), payout);
        assertEquals(Money.zero(Currency.RUB), deposit.getBalance());
    }

    @Test
    void depositToClosedThrowsTest() {
        Deposit deposit = new Deposit("Deposit", rub(10_000));
        deposit.close();

        assertThrows(ProductClosedException.class, () -> deposit.deposit(rub(1_000)));
    }

    @Test
    void closeClosedDepositThrowsTest() {
        Deposit deposit = new Deposit("Deposit", rub(10_000));
        deposit.close();

        ProductClosedException exception = assertThrows(ProductClosedException.class, deposit::close);

        assertEquals("Vklad uzhe zakryt", exception.getMessage());
    }

    @Test
    void negativeBalanceThrowsTest() {
        assertThrows(InvalidAmountException.class, () -> new Deposit(
                "Deposit",
                new Money(BigDecimal.valueOf(-1), Currency.RUB)
        ));
    }
}

