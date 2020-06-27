package io.github.ivvve.luckmoney.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Read Sprinkled Money Acceptance Test")
public class ReadSprinkledMoneyAcceptanceTest extends AcceptanceTestBase {

    @Nested @DisplayName("when user request `Read Sprinkled Money`")
    class when_user_request_Sprinkle_Money {

        @Test @DisplayName("api application responses sprinkled money data")
        void api_application_responses_token_with_its_length_is_3() throws Exception {
            // given
            final int moneyAmount = 1;
            saveSprinkledMoney(TEST_TOKEN.getValue(), TEST_USER_ID1, TEST_ROOM_ID, Arrays.asList(moneyAmount));

            final String pickerUserId = "picker";
            pickSprinkledMoney(TEST_TOKEN.getValue(), pickerUserId);

            // when
            final MvcResult response = mockMvc
                    .perform(get("/api/sprinkling-money/" + TEST_TOKEN.getValue())
                            .header("X-USER-ID", TEST_USER_ID1)
                            .header("X-ROOM-ID", TEST_ROOM_ID)
                    )
                    .andDo(print())
                    // then
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.sprinkledMoney").value(moneyAmount))
                    .andExpect(jsonPath("$.pickedMoney").value(moneyAmount))
                    .andExpect(jsonPath("$.pickedMoneys").isArray())
                    .andExpect(jsonPath("$.sprinkledAt").exists())
                    .andReturn();

            assertThat(response.getResponse().getContentAsString()).contains("\"pickedMoneys\":[{\"userId\":\"picker\",\"money\":1}]");
        }
    }

    @Test @DisplayName("only sprinkled user can read")
    void only_sprinkled_user_can_read() throws Exception {
        // given
        final int moneyAmount = 1;
        saveSprinkledMoney(TEST_TOKEN.getValue(), TEST_USER_ID1, TEST_ROOM_ID, Arrays.asList(moneyAmount));

        final String pickerUserId = TEST_USER_ID2;
        pickSprinkledMoney(TEST_TOKEN.getValue(), pickerUserId);

        // when
        mockMvc
                .perform(get("/api/sprinkling-money/" + TEST_TOKEN.getValue())
                        .header("X-USER-ID", pickerUserId)
                        .header("X-ROOM-ID", TEST_ROOM_ID)
                )
                .andDo(print())
                // then
                .andExpect(status().isNotFound());
    }

    @Test @DisplayName("user can't read sprinkled money after a week")
    void user_cant_read_sprinkled_money_after_a_week() throws Exception {
        // given
        final int moneyAmount = 1;
        saveSprinkledMoney(TEST_TOKEN.getValue(), TEST_USER_ID1, TEST_ROOM_ID, Arrays.asList(moneyAmount), LocalDateTime.now().minusWeeks(1).minusSeconds(1));

        // when
        mockMvc
                .perform(get("/api/sprinkling-money/" + TEST_TOKEN.getValue())
                        .header("X-USER-ID", TEST_USER_ID1)
                        .header("X-ROOM-ID", TEST_ROOM_ID)
                )
                .andDo(print())
                // then
                .andExpect(status().isNotFound());
    }
}
