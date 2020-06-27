package io.github.ivvve.luckmoney.domain.room;

public interface RoomMemberChecker {
    boolean isRoomMember(final String userId, final String roomId);
}
