package io.github.ivvve.luckmoney.domain.room;

import org.springframework.stereotype.Component;

/**
 * 해당 기능의 구현에 대한 구체적인 언급이 없어 항상 true를 return하게 하였음
 */
@Component
public class AlwaysPassRoomMemberChecker implements RoomMemberChecker {
    @Override
    public boolean isRoomMember(final String userId, final String roomId) {
        return true;
    }
}
