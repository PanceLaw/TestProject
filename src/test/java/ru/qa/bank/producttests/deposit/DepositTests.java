package ru.qa.bank.producttests.deposit;

import org.junit.jupiter.api.Test;
import ru.qa.bank.exception.InvalidAmountException;
import ru.qa.bank.exception.ProductClosedException;
import ru.qa.bank.model.Currency;
import ru.qa.bank.product.deposit.Deposit;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class DepositTests {
    @Test
    void createDepositTest() {
        Deposit deposit = new Deposit("Deposit", Currency.RUB, BigDecimal.valueOf(10_000));

        assertSoftly(softly -> {
            softly.assertThat(deposit.getName()).isEqualTo("Deposit");
            softly.assertThat(deposit.getCurrency()).isEqualTo(Currency.RUB);
            softly.assertThat(deposit.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(10_000));
            softly.assertThat(deposit.isClosed()).isFalse();
        });
    }

    @Test
    void depositIncreasesBalanceTest() {
        Deposit deposit = new Deposit("Deposit", Currency.RUB, BigDecimal.valueOf(10_000));

        deposit.deposit(BigDecimal.valueOf(5_000));

        assertSoftly(softly ->
                softly.assertThat(deposit.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(15_000))
        );
    }

    @Test
    void closeReturnsPayoutTest() {
        Deposit deposit = new Deposit("Deposit", Currency.RUB, BigDecimal.valueOf(10_000));

        BigDecimal payout = deposit.close();

        assertSoftly(softly -> {
            softly.assertThat(deposit.isClosed()).isTrue();
            softly.assertThat(payout).isEqualByComparingTo(BigDecimal.valueOf(10_000));
            softly.assertThat(deposit.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
        });
    }

    @Test
    void depositToClosedRejectedTest() {
        Deposit deposit = new Deposit("Deposit", Currency.RUB, BigDecimal.valueOf(10_000));
        deposit.close();

        assertThatThrownBy(() -> deposit.deposit(BigDecimal.valueOf(1_000)))
                .isInstanceOf(ProductClosedException.class)
                .hasMessage("Deposit is closed");
    }

    @Test
    void closeClosedDepositRejectedTest() {
        Deposit deposit = new Deposit("Deposit", Currency.RUB, BigDecimal.valueOf(10_000));
        deposit.close();

        assertThatThrownBy(deposit::close)
                .isInstanceOf(ProductClosedException.class)
                .hasMessage("Deposit is already closed");
    }

    @Test
    void negativeBalanceRejectedTest() {
        assertThatThrownBy(() -> new Deposit("Deposit", Currency.RUB, BigDecimal.valueOf(-1)))
                .isInstanceOf(InvalidAmountException.class)
                .hasMessage("Initial balance must be zero or positive");
    }
}
