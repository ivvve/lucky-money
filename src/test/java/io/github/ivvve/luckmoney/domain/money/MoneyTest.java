package io.github.ivvve.luckmoney.domain.money;

import io.github.ivvve.luckmoney.domain.exceptions.InvalidMoney;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Money class")
class MoneyTest {

    @Nested @DisplayName("constructor")
    class constructor {

        @Nested @DisplayName("when amount is negative")
        class when_amount_is_negative {
            final int amount = -1;

            @Test @DisplayName("throws InvalidMoney exception")
            void throwsInvalidMoneyException() {
                assertThrows(InvalidMoney.class, () -> new Money(amount));
            }
        }

        @Nested @DisplayName("when amount is zero")
        class when_amount_is_zero {
            final int amount = 0;

            @Test @DisplayName("throws InvalidMoney exception")
            void throwsInvalidMoneyException() {
                assertThrows(InvalidMoney.class, () -> new Money(amount));
            }
        }

        @Nested @DisplayName("when amount is greater than zero")
        class when_amount_is_greater_than_zero {
            final int amount = 1;

            @Test @DisplayName("returns Money instance normally")
            void throwsInvalidMoneyException() {
                final Money money = new Money(amount);
            }
        }
    }
}