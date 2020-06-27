package io.github.ivvve.luckmoney.acceptance;

import com.jayway.jsonpath.JsonPath;
import io.github.ivvve.luckmoney.domain.SprinkledMoney;
import io.github.ivvve.luckmoney.domain.token.Token;
import io.github.ivvve.luckmoney.ui.request.SprinkleMoneyRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Sprinkle Money Acceptance Test")
public class SprinkleMoneyAcceptanceTest extends AcceptanceTestBase {

    @Nested @DisplayName("when user request `Sprinkle Money`")
    class when_user_request_Sprinkle_Money {

        @Test @DisplayName("api application responses token with its length is 3")
        void api_application_responses_token_with_its_length_is_3() throws Exception {
            // when
            final MvcResult response = mockMvc
                    .perform(post("/api/sprinkling-money")
                            .header("X-USER-ID", TEST_USER_ID)
                            .header("X-ROOM-ID", TEST_ROOM_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new SprinkleMoneyRequest(100, 5)))
                    )
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.token").exists())
                    .andReturn();

            // then
            final String token = JsonPath.read(response.getResponse().getContentAsString(), "$.token");
            assertThat(token.length()).isEqualTo(3);
        }

        @Test @DisplayName("api application saves SprinkledMoney data with response token")
        void api_application_saves_sprinkledMoney_data_with_response_token() throws Exception {
            // given
            final int numberOfRoomMembers = 5;

            // when
            final MvcResult response = mockMvc
                    .perform(post("/api/sprinkling-money")
                            .header("X-USER-ID", TEST_USER_ID)
                            .header("X-ROOM-ID", TEST_ROOM_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new SprinkleMoneyRequest(100, numberOfRoomMembers)))
                    )
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andReturn();

            final String token = JsonPath.read(response.getResponse().getContentAsString(), "$.token");

            // then
            final SprinkledMoney sprinkledMoney = sprinkledMoneyRepository.findByToken(new Token(token)).get();
            assertThat(sprinkledMoney.getToken().getValue()).isEqualTo(token);
            assertThat(sprinkledMoney.getUserId()).isEqualTo(TEST_USER_ID);
            assertThat(sprinkledMoney.getRoomId()).isEqualTo(TEST_ROOM_ID);
            assertThat(sprinkledMoney.getPickedMoneys()).hasSize(numberOfRoomMembers);
        }
    }
}
