package io.github.ivvve.luckmoney.application;

import io.github.ivvve.luckmoney.domain.SprinkledMoney;
import io.github.ivvve.luckmoney.domain.SprinkledMoneyRepository;
import io.github.ivvve.luckmoney.domain.exceptions.CannotPickSprinkledMoney;
import io.github.ivvve.luckmoney.domain.exceptions.SprinkledMoneyNotFound;
import io.github.ivvve.luckmoney.domain.money.Money;
import io.github.ivvve.luckmoney.domain.money.MoneyDivider;
import io.github.ivvve.luckmoney.domain.room.RoomMemberChecker;
import io.github.ivvve.luckmoney.domain.token.Token;
import io.github.ivvve.luckmoney.domain.token.TokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SprinklingMoneyService {
    private final SprinkledMoneyRepository sprinkledMoneyRepository;
    private final TokenGenerator tokenGenerator;
    private final MoneyDivider moneyDivider;
    private final RoomMemberChecker roomMemberChecker;

    @Transactional
    public SprinkledMoney sprinkle(final String userId, final String roomId,
                                   final int sprinkledMoney, final int numberOfRoomMembers) {
        final Token token = this.tokenGenerator.generate(); // TODO duplicated token check!
        final List<Money> dividedMonies = this.moneyDivider.divide(new Money(sprinkledMoney), numberOfRoomMembers);
        return this.sprinkledMoneyRepository.save(new SprinkledMoney(token, userId, roomId, dividedMonies));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public SprinkledMoney pick(final String userId, final String roomId, final String token) {
        final SprinkledMoney sprinkledMoney = this.sprinkledMoneyRepository
                .findByToken(new Token(token))
                .orElseThrow(SprinkledMoneyNotFound::new);

        if (!this.roomMemberChecker.isRoomMember(userId, roomId)) {
            throw new CannotPickSprinkledMoney();
        }

        sprinkledMoney.pickedBy(userId);
        return sprinkledMoney;
    }

    @Transactional(readOnly = true)
    public SprinkledMoney read(final String userId, final String token) {
        return this.sprinkledMoneyRepository
                .findAllByTokenAndUserIdAndSprinkledAtAfter(new Token(token), userId, SprinkledMoney.getReadExpireTime())
                .orElseThrow(SprinkledMoneyNotFound::new);
    }
}
