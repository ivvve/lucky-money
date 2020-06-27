package io.github.ivvve.luckmoney.domain.token;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class RandomAlphanumericTokenGenerator implements TokenGenerator {
    @Override
    public Token generate() {
        return new Token(RandomStringUtils.randomAlphabetic(Token.SIZE));
    }
}
