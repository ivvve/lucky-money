package io.github.ivvve.luckmoney.domain.token;

import io.github.ivvve.luckmoney.domain.exceptions.InvalidToken;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "value")
public class Token implements Serializable {
    public static final int SIZE = 3;

    @Column(name = "token", nullable = false)
    private String value;

    public Token(final String value) {
        validateTokenValue(value);
        this.value = value;
    }

    private static void validateTokenValue(final String tokenValue) {
        if (removeWhiteSpaces(tokenValue).length() != SIZE) {
            throw new InvalidToken();
        }
    }

    private static String removeWhiteSpaces(final String tokenValue) {
        return tokenValue.replaceAll("\\s+", "");
    }
}
