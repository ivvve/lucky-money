package io.github.ivvve.luckmoney.domain;

import io.github.ivvve.luckmoney.domain.exceptions.CannotPickSprinkledMoney;
import io.github.ivvve.luckmoney.domain.exceptions.InvalidMoney;
import io.github.ivvve.luckmoney.domain.money.Money;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PickedMoney {
    @Embedded
    private Money money;

    @Column(name = "picker_user_id", nullable = true)
    private String pickerUserId;

    @Column(name = "picked_at", nullable = true)
    private LocalDateTime pickedAt;

    PickedMoney(final Money money) {
        validate(money);

        this.money = money;
    }

    public int getAmount() {
        return this.money.getAmount();
    }

    private static void validate(final Money money) {
        if (Objects.isNull(money)) {
            throw new InvalidMoney();
        }
    }

    void pickedBy(final String pickerUserId) {
        if (this.isPicked()) {
            throw new CannotPickSprinkledMoney();
        }

        this.pickerUserId = pickerUserId;
        this.pickedAt = LocalDateTime.now();
    }

    boolean isPickedBy(final String pickerUserId) {
        return Objects.equals(this.pickerUserId, pickerUserId);
    }

    boolean isPicked() {
        return Objects.nonNull(this.pickerUserId);
    }

    boolean isNotPicked() {
        return !this.isPicked();
    }
}
