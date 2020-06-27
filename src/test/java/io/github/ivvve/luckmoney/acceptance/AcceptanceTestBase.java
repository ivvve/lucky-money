package io.github.ivvve.luckmoney.acceptance;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.ivvve.luckmoney.domain.SprinkledMoney;
import io.github.ivvve.luckmoney.domain.SprinkledMoneyRepository;
import io.github.ivvve.luckmoney.domain.money.Money;
import io.github.ivvve.luckmoney.domain.token.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AcceptanceTestBase {
    static final Token TEST_TOKEN = new Token("abc");
    static final String TEST_USER_ID1 = "00001";
    static final String TEST_USER_ID2 = "00002";
    static final String TEST_ROOM_ID = "test_room";

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Autowired SprinkledMoneyRepository sprinkledMoneyRepository;

    protected SprinkledMoney saveSprinkledMoney(final String token, final String userId, final String roomId, final List<Integer> moneys) {
        return this.saveSprinkledMoney(token, userId, roomId, moneys, LocalDateTime.now());
    }

    protected SprinkledMoney saveSprinkledMoney(final String token, final String userId, final String roomId,
                                                final List<Integer> moneys, final LocalDateTime localDateTime) {
        return this.sprinkledMoneyRepository.save(
                new SprinkledMoney(new Token(token), userId, roomId, moneys.stream().map(Money::new).collect(Collectors.toList()), localDateTime)
        );
    }

    protected void pickSprinkledMoney(final String token, final String userId) {
        final SprinkledMoney sprinkledMoney = this.sprinkledMoneyRepository.findByToken(new Token(token)).get();
        sprinkledMoney.pickedBy(userId);
        this.sprinkledMoneyRepository.save(sprinkledMoney);
    }
}
