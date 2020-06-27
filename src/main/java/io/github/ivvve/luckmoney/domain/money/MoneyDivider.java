package io.github.ivvve.luckmoney.domain.money;

import io.github.ivvve.luckmoney.domain.exceptions.CannotDivideMoney;

import java.util.List;
import java.util.Objects;

public abstract class MoneyDivider {
    private static final String LOGIC_ERROR_MESSAGE = "MoneyDivider sub class' logic is wrong";

    public List<Money> divide(final Money money, final int by) {
        validateInput(money, by);

        final List<Money> dividedMoneys = this.doDivide(money, by);
        validateOutput(money, by, dividedMoneys);

        return dividedMoneys;
    }

    private static void validateInput(final Money money, final int by) {
        if (money.getAmount() < by) {
            throw new CannotDivideMoney();
        }
    }

    protected abstract List<Money> doDivide(final Money money, final int by);

    private static void validateOutput(final Money money, final int by, final List<Money> dividedMoneys) {
        if (Objects.isNull(dividedMoneys)) {
            throw new RuntimeException(LOGIC_ERROR_MESSAGE);
        }

        final int sumOfDividedMoneys = dividedMoneys.stream().mapToInt(Money::getAmount).sum();

        if (sumOfDividedMoneys != money.getAmount()) {
            throw new RuntimeException(LOGIC_ERROR_MESSAGE);
        }

        if (dividedMoneys.size() != by) {
            throw new RuntimeException(LOGIC_ERROR_MESSAGE);
        }
    }
}
