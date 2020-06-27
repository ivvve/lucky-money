package io.github.ivvve.luckmoney.domain.money;

import io.github.ivvve.luckmoney.domain.exceptions.CannotDivideMoney;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MoneyDivider class")
class MoneyDividerTest {

    @Nested @DisplayName("divide method")
    class divide_method {

        @Nested @DisplayName("when money amount is less than by")
        class when_money_amount_is_less_than_by {
            final MoneyDivider moneyDivider = new MoneyDivider() {
                @Override
                protected List<Money> doDivide(final Money money, final int by) {
                    return null;
                }
            };

            final Money money = new Money(1);
            final int by = money.getAmount() + 1;

            @Test @DisplayName("throws CannotDivideMoney")
            void throws_CannotDivideMoney() {
                assertThrows(CannotDivideMoney.class, () -> {
                    moneyDivider.divide(money, by);
                });
            }
        }

        @Nested @DisplayName("when implementation returns null")
        class when_implementation_returns_null {
            final MoneyDivider implementation = new MoneyDivider() {
                @Override
                protected List<Money> doDivide(final Money money, final int by) {
                    return null;
                }
            };

            @Test @DisplayName("throws RuntimeException")
            void throws_RuntimeException() {
                assertThrows(RuntimeException.class, () -> implementation.divide(new Money(1), 1));
            }
        }

        @Nested @DisplayName("when implementation returns less moneys than by")
        class when_implementation_returns_less_moneys_than_by {
            final MoneyDivider implementation = new MoneyDivider() {
                @Override
                protected List<Money> doDivide(final Money money, final int by) {
                    return Arrays.asList(new Money(money.getAmount()));
                }
            };

            @Test @DisplayName("throws RuntimeException")
            void throws_RuntimeException() {
                assertThrows(RuntimeException.class, () -> implementation.divide(new Money(2), 2));
            }
        }

        @Nested @DisplayName("when implementation returns less money than original money")
        class when_implementation_returns_less_money_than_original_money {
            final MoneyDivider implementation = new MoneyDivider() {
                @Override
                protected List<Money> doDivide(final Money money, final int by) {
                    return Arrays.asList(new Money(money.getAmount() - 1));
                }
            };

            @Test @DisplayName("throws RuntimeException")
            void throws_RuntimeException() {
                assertThrows(RuntimeException.class, () -> implementation.divide(new Money(2), 1));
            }
        }
    }
}