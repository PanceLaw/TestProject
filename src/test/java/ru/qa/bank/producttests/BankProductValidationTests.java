package ru.qa.bank.producttests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.qa.bank.exception.InvalidAmountException;
import ru.qa.bank.model.Currency;
import ru.qa.bank.product.card.DebitCard;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BankProductValidationTests {
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    void blankNameRejectedTest(String name) {
        assertThatThrownBy(() -> new DebitCard(name, Currency.RUB, BigDecimal.TEN))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Product name must not be blank");
    }

    @Test
    void nullCurrencyRejectedTest() {
        assertThatThrownBy(() -> new DebitCard("Debit card", null, BigDecimal.TEN))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Currency must not be null");
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1", "-100.50"})
    void negativeInitialBalanceRejectedTest(String balance) {
        assertThatThrownBy(() -> new DebitCard("Debit card", Currency.RUB, new BigDecimal(balance)))
                .isInstanceOf(InvalidAmountException.class)
                .hasMessage("Initial balance must be zero or positive");
    }

    @Test
    void zeroDepositAmountRejectedTest() {
        DebitCard card = new DebitCard("Debit card", Currency.RUB, BigDecimal.TEN);

        assertThatThrownBy(() -> card.deposit(BigDecimal.ZERO))
                .isInstanceOf(InvalidAmountException.class)
                .hasMessage("Amount must be positive");
    }

    @Test
    void nullDepositAmountRejectedTest() {
        DebitCard card = new DebitCard("Debit card", Currency.RUB, BigDecimal.TEN);

        assertThatThrownBy(() -> card.deposit(null))
                .isInstanceOf(InvalidAmountException.class)
                .hasMessage("Amount must be positive");
    }
}
