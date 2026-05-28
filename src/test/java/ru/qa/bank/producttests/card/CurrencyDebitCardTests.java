package ru.qa.bank.producttests.card;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ru.qa.bank.model.Currency;
import ru.qa.bank.product.card.CurrencyDebitCard;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class CurrencyDebitCardTests {
    @ParameterizedTest
    @EnumSource(value = Currency.class, names = {"USD", "EUR"})
    void createCurrencyCardTest(Currency currency) {
        CurrencyDebitCard card = new CurrencyDebitCard("Currency debit card", currency, BigDecimal.valueOf(100));

        assertSoftly(softly -> {
            softly.assertThat(card.getName()).isEqualTo("Currency debit card");
            softly.assertThat(card.getCurrency()).isEqualTo(currency);
            softly.assertThat(card.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(100));
        });
    }

    @Test
    void rubCurrencyRejectedTest() {
        assertThatThrownBy(() -> new CurrencyDebitCard("Currency debit card", Currency.RUB, BigDecimal.valueOf(100)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Currency debit card cannot use RUB");
    }
}
