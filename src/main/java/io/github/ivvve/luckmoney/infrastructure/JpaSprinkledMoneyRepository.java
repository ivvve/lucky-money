package io.github.ivvve.luckmoney.infrastructure;

import io.github.ivvve.luckmoney.domain.SprinkledMoney;
import io.github.ivvve.luckmoney.domain.SprinkledMoneyRepository;
import io.github.ivvve.luckmoney.domain.exceptions.SprinkledMoneyDataFailed;
import io.github.ivvve.luckmoney.domain.token.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaSprinkledMoneyRepository implements SprinkledMoneyRepository {
    private final InnerJpaSprinkledMoneyRepository repository;

    @Override
    public SprinkledMoney save(final SprinkledMoney sprinkledMoney) {
        try {
            return this.repository.save(sprinkledMoney);
        } catch (final Exception e) {
            throw new SprinkledMoneyDataFailed(e);
        }
    }

    @Override
    public Optional<SprinkledMoney> findByToken(final Token token) {
        try {
            return this.repository.findById(token);
        } catch (final Exception e) {
            throw new SprinkledMoneyDataFailed(e);
        }
    }

    @Override
    public Optional<SprinkledMoney> findAllByTokenAndUserIdAndSprinkledAtAfter(final Token token, final String userId,
                                                                               final LocalDateTime sprinkledAt) {
        try {
            return this.repository.findAllByTokenAndUserIdAndSprinkledAtAfter(token, userId, sprinkledAt);
        } catch (final Exception e) {
            throw new SprinkledMoneyDataFailed(e);
        }
    }
}

interface InnerJpaSprinkledMoneyRepository extends JpaRepository<SprinkledMoney, Token> {
    Optional<SprinkledMoney> findAllByTokenAndUserIdAndSprinkledAtAfter(final Token token, final String userId,
                                                                        final LocalDateTime sprinkledAt);
}