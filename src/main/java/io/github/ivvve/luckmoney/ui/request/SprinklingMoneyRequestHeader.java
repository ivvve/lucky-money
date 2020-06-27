package io.github.ivvve.luckmoney.ui.request;

import io.github.ivvve.luckmoney.common.exceptions.ValidationFailedException;
import org.apache.commons.lang3.StringUtils;

// TODO Controller에 custom annotation을 사용하여 처리
public class SprinklingMoneyRequestHeader {
    public final String userId;
    public final String roomId;

    public SprinklingMoneyRequestHeader(final String userId, final String roomId) {
        validate(userId, roomId);

        this.userId = userId;
        this.roomId = roomId;
    }

    private static void validate(final String userId, final String roomId) {
        if (StringUtils.isBlank(userId) || !StringUtils.isNumeric(userId)) {
            throw new ValidationFailedException();
        }

        if (StringUtils.isBlank(roomId)) {
            throw new ValidationFailedException();
        }
    }
}
