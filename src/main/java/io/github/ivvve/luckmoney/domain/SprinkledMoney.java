package io.github.ivvve.luckmoney.domain;

import io.github.ivvve.luckmoney.domain.exceptions.*;
import io.github.ivvve.luckmoney.domain.money.Money;
import io.github.ivvve.luckmoney.domain.token.Token;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "sprinkled_moneys")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "token")
public class SprinkledMoney {
    public static final int PICK_EXPIRE_MINUTES = 10;
    public static final int READ_EXPIRE_DAYS = 7;

    @EmbeddedId
    private Token token;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "room_id", nullable = false)
    private String roomId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "picked_moneys",
            joinColumns = @JoinColumn(name = "sprinkled_money_token"),
            indexes = @Index(columnList = "sprinkled_money_token")
    )
    @OrderColumn
    private List<PickedMoney> pickedMoneys;

    @Column(name = "sprinkled_At", nullable = false)
    private LocalDateTime sprinkledAt;


    public SprinkledMoney(final Token token, final String userId, final String roomId, final List<Money> moneys) {
        this(token, userId, roomId, moneys, LocalDateTime.now());
    }

    public SprinkledMoney(final Token token, final String userId, final String roomId,
                   final List<Money> moneys, final LocalDateTime sprinkledAt) {
        validate(userId, roomId, moneys);

        this.token = token;
        this.userId = userId;
        this.roomId = roomId;
        this.pickedMoneys = moneys.stream()
                .map(PickedMoney::new)
                .collect(Collectors.toList());
        this.sprinkledAt = sprinkledAt;
    }

    public static LocalDateTime getReadExpireTime() {
        return LocalDateTime.now().minusDays(SprinkledMoney.READ_EXPIRE_DAYS);
    }

    public String getTokenValue() {
        return this.token.getValue();
    }

    public List<PickedMoney> getPickedMoneys() {
        return Collections.unmodifiableList(this.pickedMoneys);
    }

    public void pickedBy(final String pickerUserId) {
        if (Objects.equals(this.userId, pickerUserId)) {
            throw new CannotPickSprinkledMoney();
        }

        if (this.isExpired()) {
            throw new SprinkledMoneyExpired();
        }

        if (this.hasPickerPickedMoney(pickerUserId)) {
            throw new AlreadyPickedSprinkledMoney();
        }

        final PickedMoney unpickedMoney = this.getUnpickedMoney();
        unpickedMoney.pickedBy(pickerUserId);
    }

    private static void validate(final String userId, final String roomId, final List<Money> monies) {
        if (StringUtils.isBlank(userId)) {
            throw new InvalidUserId();
        }

        if (StringUtils.isBlank(roomId)) {
            throw new InvalidRoomId();
        }

        if (Objects.isNull(monies) || monies.isEmpty()) {
            throw new InvalidMoneys();
        }
    }

    private boolean isExpired() {
        final LocalDateTime expiredTime = this.sprinkledAt.plusMinutes(PICK_EXPIRE_MINUTES);
        return LocalDateTime.now().isAfter(expiredTime);
    }

    private boolean hasPickerPickedMoney(final String pickerUserId) {
        return this.pickedMoneys.stream()
                .anyMatch(pickedMoney -> pickedMoney.isPickedBy(pickerUserId));
    }

    private PickedMoney getUnpickedMoney() {
        return this.pickedMoneys.stream()
                .filter(PickedMoney::isNotPicked)
                .findFirst()
                .orElseThrow(CannotPickSprinkledMoney::new);
    }
}
