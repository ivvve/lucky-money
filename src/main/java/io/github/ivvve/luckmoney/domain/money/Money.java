package io.github.ivvve.luckmoney.domain.money;

import io.github.ivvve.luckmoney.domain.exceptions.InvalidMoney;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter @Setter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "amount")
public class Money {
    private static final int MIN_AMOUNT = 1;

    @Column(name = "amount", nullable = false)
    private int amount;

    public Money(final int amount) {
        validate(amount);

        this.amount = amount;
    }

    private static void validate(final int amount) {
        if (amount < MIN_AMOUNT) {
            throw new InvalidMoney();
        }
    }
}
