package io.github.ivvve.luckmoney.domain;

import io.github.ivvve.luckmoney.domain.token.Token;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SprinkledMoneyRepository {
    SprinkledMoney save(final SprinkledMoney sprinkledMoney);

    Optional<SprinkledMoney> findByToken(final Token token);

    Optional<SprinkledMoney> findAllByTokenAndUserIdAndSprinkledAtAfter(final Token token, final String roomId,
                                                                        final LocalDateTime sprinkledAt);
}
