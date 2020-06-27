package io.github.ivvve.luckmoney.domain;

import io.github.ivvve.luckmoney.domain.exceptions.*;
import io.github.ivvve.luckmoney.domain.money.Money;
import io.github.ivvve.luckmoney.domain.token.Token;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;

class SprinkledMoneyTest {
    static final String TEST_TOKEN = "abc";
    static final String TEST_USER_ID = "test_user";
    static final String TEST_ROOM_ID = "test_room";

    static SprinkledMoney newTestSprinkledMoney(final List<Integer> moneys) {
        return newTestSprinkledMoney(moneys, LocalDateTime.now());
    }

    static SprinkledMoney newTestSprinkledMoney(final List<Integer> moneys, final LocalDateTime sprinkledAt) {
        final List<Money> pickedMoneys = moneys.stream().map(Money::new).collect(Collectors.toList());
        return new SprinkledMoney(new Token(TEST_TOKEN), TEST_USER_ID, TEST_ROOM_ID, pickedMoneys, sprinkledAt);
    }

    @Nested @DisplayName("constructor")
    class constructor {

        @Nested @DisplayName("when userId is")
        class when_userId_is {

            @Nested @DisplayName("null")
            class NULL {
                final String userId = null;

                @Test @DisplayName("throws InvalidUserId")
                void throws_InvalidRoomId() {
                    assertThrows(InvalidUserId.class, () ->
                            new SprinkledMoney(new Token(TEST_TOKEN), userId, TEST_ROOM_ID, Arrays.asList(new Money(1))));
                }
            }

            @Nested @DisplayName("empty")
            class empty {
                final String userId = "";

                @Test @DisplayName("throws InvalidUserId")
                void throws_InvalidRoomId() {
                    assertThrows(InvalidUserId.class, () ->
                            new SprinkledMoney(new Token(TEST_TOKEN), userId, TEST_ROOM_ID, Arrays.asList(new Money(1))));
                }
            }

            @Nested @DisplayName("blank")
            class blank {
                final String userId = "  ";

                @Test @DisplayName("throws InvalidUserId")
                void throws_InvalidRoomId() {
                    assertThrows(InvalidUserId.class, () ->
                            new SprinkledMoney(new Token(TEST_TOKEN), userId, TEST_ROOM_ID, Arrays.asList(new Money(1))));
                }
            }

            @Nested @DisplayName("not blank")
            class not_blank {
                final String userId = "user";

                @Test @DisplayName("returns SprinkledMoney instance normally")
                void returns_SprinkledMoney_instance_normally() {
                    final SprinkledMoney sprinkledMoney =
                            new SprinkledMoney(new Token(TEST_TOKEN), userId, TEST_ROOM_ID, Arrays.asList(new Money(1)));
                }
            }
        }

        @Nested @DisplayName("when roomId is")
        class when_roomdId_is {

            @Nested @DisplayName("null")
            class NULL {
                final String roomId = null;

                @Test @DisplayName("throws InvalidRoomId")
                void throws_InvalidRoomId() {
                    assertThrows(InvalidRoomId.class, () ->
                            new SprinkledMoney(new Token(TEST_TOKEN), TEST_USER_ID, roomId, Arrays.asList(new Money(1))));
                }
            }

            @Nested @DisplayName("empty")
            class empty {
                final String roomId = "";

                @Test @DisplayName("throws InvalidRoomId")
                void throws_InvalidRoomId() {
                    assertThrows(InvalidRoomId.class, () ->
                            new SprinkledMoney(new Token(TEST_TOKEN), TEST_USER_ID, roomId, Arrays.asList(new Money(1))));
                }
            }

            @Nested @DisplayName("blank")
            class blank {
                final String roomId = "   ";

                @Test @DisplayName("throws InvalidRoomId")
                void throws_InvalidRoomId() {
                    assertThrows(InvalidRoomId.class, () ->
                            new SprinkledMoney(new Token(TEST_TOKEN), TEST_USER_ID, roomId, Arrays.asList(new Money(1))));
                }
            }

            @Nested @DisplayName("not blank")
            class not_blank {
                final String roomId = "room";

                @Test @DisplayName("returns SprinkledMoney instance normally")
                void returns_SprinkledMoney_instance_normally() {
                    final SprinkledMoney sprinkledMoney =
                            new SprinkledMoney(new Token(TEST_TOKEN), TEST_USER_ID, roomId, Arrays.asList(new Money(1)));
                }
            }
        }


        @Nested @DisplayName("when moneys is")
        class when_moneys_is {

            @Nested @DisplayName("null")
            class NULL {
                final List<Money> moneys = null;

                @Test @DisplayName("throws InvalidMoneys")
                void throws_InvalidMoneys() {
                    assertThrows(InvalidMoneys.class, () ->
                            new SprinkledMoney(new Token(TEST_TOKEN), TEST_USER_ID, TEST_ROOM_ID, moneys));
                }
            }

            @Nested @DisplayName("empty")
            class empty {
                final List<Money> moneys = Collections.emptyList();

                @Test @DisplayName("throws InvalidMoneys")
                void throws_InvalidMoneys() {
                    assertThrows(InvalidMoneys.class, () ->
                            new SprinkledMoney(new Token(TEST_TOKEN), TEST_USER_ID, TEST_ROOM_ID, moneys));
                }
            }

            @Nested @DisplayName("not empty")
            class not_empty {
                final List<Money> moneys = Arrays.asList(new Money(1));

                @Test @DisplayName("returns SprinkledMoney instance normally")
                void returns_SprinkledMoney_instance_normally() {
                    final SprinkledMoney sprinkledMoney =
                            new SprinkledMoney(new Token(TEST_TOKEN), TEST_USER_ID, TEST_ROOM_ID, moneys);
                }
            }
        }
    }

    @Nested @DisplayName("pickedBy method")
    class pickedBy_method {

        @Nested @DisplayName("when sprinkled user tries to pick")
        class when_sprinkled_user_tries_to_pick {
            final SprinkledMoney sprinkledMoney = newTestSprinkledMoney(Arrays.asList(1));

            @Test @DisplayName("throws CannotPickSprinkledMoney")
            void throws_CannotPickSprinkledMoney() {
                assertThrows(CannotPickSprinkledMoney.class, () -> sprinkledMoney.pickedBy(TEST_USER_ID));
            }
        }

        @Nested @DisplayName("when SprinkledMoney is expired to pick")
        class when_sprinkled_money_is_expired_to_pick {
            final SprinkledMoney expiredSprinkledMoney = newTestSprinkledMoney(Arrays.asList(1),
                    LocalDateTime.now().minusMinutes(SprinkledMoney.EXPIRE_MINUTE).minusMinutes(1));

            @Test @DisplayName("throws SprinkledMoneyExpired")
            void throws_CannotPickSprinkledMoney() {
                assertThrows(SprinkledMoneyExpired.class, () -> expiredSprinkledMoney.pickedBy(TEST_USER_ID + "a"));
            }
        }

        @Nested @DisplayName("when picked user tries to pick again")
        class when_picked_user_tries_to_pick_again {
            final SprinkledMoney sprinkledMoney = newTestSprinkledMoney(Arrays.asList(1));

            @Test @DisplayName("throws AlreadyPickedSprinkledMoney")
            void throws_CannotPickSprinkledMoney() {
                final String otherUserId = TEST_USER_ID + "1";
                sprinkledMoney.pickedBy(otherUserId);
                assertThrows(AlreadyPickedSprinkledMoney.class, () -> sprinkledMoney.pickedBy(otherUserId));
            }
        }

        @Nested @DisplayName("when there's no PickedMoney to pick")
        class when_theres_no_PickedMoney_to_pick {
            final SprinkledMoney sprinkledMoney = newTestSprinkledMoney(Arrays.asList(1));

            @Test @DisplayName("throws CannotPickSprinkledMoney")
            void throws_CannotPickSprinkledMoney() {
                sprinkledMoney.pickedBy(TEST_USER_ID + "1");
                assertThrows(CannotPickSprinkledMoney.class, () -> sprinkledMoney.pickedBy(TEST_USER_ID + "2"));
            }
        }
    }
}