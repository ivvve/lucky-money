package io.github.ivvve.luckmoney.acceptance;

import com.jayway.jsonpath.JsonPath;
import io.github.ivvve.luckmoney.domain.PickedMoney;
import io.github.ivvve.luckmoney.domain.SprinkledMoney;
import io.github.ivvve.luckmoney.domain.room.RoomMemberChecker;
import io.github.ivvve.luckmoney.domain.token.Token;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Pick Sprinkled Money Acceptance Test")
public class PickSprinkledMoneyAcceptanceTest extends AcceptanceTestBase {
    @MockBean private RoomMemberChecker roomMemberChecker;

    @Nested @DisplayName("when user request `Pick Sprinkled Money`")
    class when_user_request_Pick_Sprinkled_Money {

        @Test @DisplayName("api application responses pickedMoney")
        void itResponsesToken() throws Exception {
            // given
            final int moneyAmount = 1;
            saveSprinkledMoney(TEST_TOKEN.getValue(), TEST_USER_ID1, TEST_ROOM_ID, Arrays.asList(moneyAmount));

            final String pickerUserId = TEST_USER_ID2;
            given(roomMemberChecker.isRoomMember(pickerUserId, TEST_ROOM_ID)).willReturn(true);

            // when
            final MvcResult response = mockMvc
                    .perform(patch("/api/sprinkling-money/" + TEST_TOKEN.getValue())
                            .header("X-USER-ID", pickerUserId)
                            .header("X-ROOM-ID", TEST_ROOM_ID)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.pickedMoney").exists())
                    .andReturn();

            // then
            final int pickedMoney = JsonPath.read(response.getResponse().getContentAsString(), "$.pickedMoney");
            assertThat(pickedMoney).isEqualTo(moneyAmount);
        }

        @Test @DisplayName("api application saves user money picked data")
        void api_application_saves_user_money_picked_data() throws Exception {
            // given
            final int moneyAmount = 1;
            saveSprinkledMoney(TEST_TOKEN.getValue(), TEST_USER_ID1, TEST_ROOM_ID, Arrays.asList(moneyAmount));

            final String pickerUserId = TEST_USER_ID2;
            given(roomMemberChecker.isRoomMember(pickerUserId, TEST_ROOM_ID)).willReturn(true);

            // when
            mockMvc
                    .perform(patch("/api/sprinkling-money/" + TEST_TOKEN.getValue())
                            .header("X-USER-ID", pickerUserId)
                            .header("X-ROOM-ID", TEST_ROOM_ID)
                    )
                    .andDo(print())
                    .andExpect(status().isOk());

            // then
            final SprinkledMoney sprinkledMoney = sprinkledMoneyRepository.findByToken(TEST_TOKEN).get();
            final PickedMoney pickedMoney = sprinkledMoney.getPickedMoneys().get(0);
            assertThat(pickedMoney.getPickerUserId()).isEqualTo(pickerUserId);
            assertThat(pickedMoney.getMoney().getAmount()).isEqualTo(moneyAmount);
        }
    }

    @Test @DisplayName("sprinkled user can't pick sprinkled money")
    void sprinkled_user_cant_pick_sprinkled_money() throws Exception {
        // given
        saveSprinkledMoney(TEST_TOKEN.getValue(), TEST_USER_ID1, TEST_ROOM_ID, Arrays.asList(1));

        given(roomMemberChecker.isRoomMember(TEST_USER_ID1, TEST_ROOM_ID)).willReturn(true);

        // when
        final MvcResult response = mockMvc
                .perform(patch("/api/sprinkling-money/" + TEST_TOKEN.getValue())
                        .header("X-USER-ID", TEST_USER_ID1)
                        .header("X-ROOM-ID", TEST_ROOM_ID)
                )
                .andDo(print())
                .andReturn();

        // then
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.CONFLICT.value());

        final SprinkledMoney sprinkledMoney = sprinkledMoneyRepository.findByToken(TEST_TOKEN).get();
        assertThat(sprinkledMoney.getPickedMoneys().get(0).getPickerUserId()).isNull();
    }

    @Test @DisplayName("user can't pick sprinkled money more than once")
    void user_cant_pick_sprinkled_money_more_than_once() throws Exception {
        // given
        saveSprinkledMoney(TEST_TOKEN.getValue(), TEST_USER_ID1, TEST_ROOM_ID, Arrays.asList(1, 1));

        final String pickerUserId = TEST_USER_ID2;
        pickSprinkledMoney(TEST_TOKEN.getValue(), pickerUserId);
        given(roomMemberChecker.isRoomMember(pickerUserId, TEST_ROOM_ID)).willReturn(true);

        // when
        final MvcResult response = mockMvc
                .perform(patch("/api/sprinkling-money/" + TEST_TOKEN.getValue())
                        .header("X-USER-ID", pickerUserId)
                        .header("X-ROOM-ID", TEST_ROOM_ID)
                )
                .andDo(print())
                .andReturn();

        // then
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.CONFLICT.value());

        final SprinkledMoney sprinkledMoney = sprinkledMoneyRepository.findByToken(TEST_TOKEN).get();
        final long pickerPickedMoneyCount = sprinkledMoney.getPickedMoneys().stream()
                .filter(pickedMoney -> Objects.equals(pickedMoney.getPickerUserId(), pickerUserId))
                .count();
        assertThat(pickerPickedMoneyCount).isEqualTo(1);
    }

    @Test @DisplayName("user can't pick expired sprinkled money")
    void user_cant_pick_expired_sprinkled_money() throws Exception {
        // given
        saveSprinkledMoney(TEST_TOKEN.getValue(), TEST_USER_ID1, TEST_ROOM_ID, Arrays.asList(1),
                LocalDateTime.now().minusMinutes(SprinkledMoney.PICK_EXPIRE_MINUTES).minusSeconds(1));

        final String pickerUserId = TEST_USER_ID2;
        given(roomMemberChecker.isRoomMember(pickerUserId, TEST_ROOM_ID)).willReturn(true);

        // when
        final MvcResult response = mockMvc
                .perform(patch("/api/sprinkling-money/" + TEST_TOKEN.getValue())
                        .header("X-USER-ID", pickerUserId)
                        .header("X-ROOM-ID", TEST_ROOM_ID)
                )
                .andDo(print())
                .andReturn();

        // then
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.REQUEST_TIMEOUT.value());

        final SprinkledMoney sprinkledMoney = sprinkledMoneyRepository.findByToken(TEST_TOKEN).get();
        assertThat(sprinkledMoney.getPickedMoneys().get(0).getPickerUserId()).isNull();
    }

    @Test @DisplayName("user who isn't in the room can't pick sprinkled money")
    void user_who_isnt_in_the_room_cant_pick_sprinkled_money() throws Exception {
        // given
        saveSprinkledMoney(TEST_TOKEN.getValue(), TEST_USER_ID1, TEST_ROOM_ID, Arrays.asList(1),
                LocalDateTime.now().minusMinutes(SprinkledMoney.PICK_EXPIRE_MINUTES).minusSeconds(1));

        final String otherRoomUserId = TEST_USER_ID2;
        given(roomMemberChecker.isRoomMember(otherRoomUserId, TEST_ROOM_ID)).willReturn(false);

        // when
        final MvcResult response = mockMvc
                .perform(patch("/api/sprinkling-money/" + TEST_TOKEN.getValue())
                        .header("X-USER-ID", otherRoomUserId)
                        .header("X-ROOM-ID", TEST_ROOM_ID)
                )
                .andDo(print())
                .andReturn();

        // then
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.CONFLICT.value());

        final SprinkledMoney sprinkledMoney = sprinkledMoneyRepository.findByToken(TEST_TOKEN).get();
        assertThat(sprinkledMoney.getPickedMoneys().get(0).getPickerUserId()).isNull();
    }
}