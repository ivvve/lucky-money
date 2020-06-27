package io.github.ivvve.luckmoney.domain.token;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("RandomAlphanumericTokenGenerator")
class RandomAlphanumericTokenGeneratorTest {
    final RandomAlphanumericTokenGenerator tokenGenerator = new RandomAlphanumericTokenGenerator();

    @Nested @DisplayName("generate method")
    class generateMethod {

        @Test @DisplayName("returns Token instance normally")
        void returns_token_instance_normally() {
            final Token token = tokenGenerator.generate();
        }
    }
}