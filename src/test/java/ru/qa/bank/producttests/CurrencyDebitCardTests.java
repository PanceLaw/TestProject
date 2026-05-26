package ru.qa.bank.producttests;

import ru.qa.bank.product.*;

import ru.qa.bank.model.Currency;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static ru.qa.bank.testdata.TestMoney.money;
import static ru.qa.bank.testdata.TestMoney.rub;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CurrencyDebitCardTests {
    @ParameterizedTest
    @EnumSource(value = Currency.class, names = {"USD", "EUR"})
    void createCurrencyCardTest(Currency currency) {
        CurrencyDebitCard card = new CurrencyDebitCard(
                "Currency debit card",
                money(100, currency)
        );

        assertEquals("Currency debit card", card.getName());
        assertEquals(currency, card.getCurrency());
        assertEquals(money(100, currency), card.getBalance());
    }

    @Test
    void rubCurrencyThrowsTest() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CurrencyDebitCard("Currency debit card", rub(100))
        );

        assertEquals("Valyutnaya karta ne mozhet byt v RUB", exception.getMessage());
    }
}

