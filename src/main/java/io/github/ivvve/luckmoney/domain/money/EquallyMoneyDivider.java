package io.github.ivvve.luckmoney.domain.money;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EquallyMoneyDivider extends MoneyDivider {
    @Override
    protected List<Money> doDivide(final Money money, final int by) {
        final int averageAmount = money.getAmount() / by;

        final List<Money> dividedMonies = new ArrayList<>(by);
        for (int i = 0; i < by - 1; i++) {
            dividedMonies.add(new Money(averageAmount));
        }
        dividedMonies.add(new Money(averageAmount + money.getAmount() % by));

        return dividedMonies;
    }
}
