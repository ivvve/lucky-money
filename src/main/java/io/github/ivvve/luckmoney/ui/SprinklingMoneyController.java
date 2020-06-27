package io.github.ivvve.luckmoney.ui;

import io.github.ivvve.luckmoney.application.SprinklingMoneyService;
import io.github.ivvve.luckmoney.domain.PickedMoney;
import io.github.ivvve.luckmoney.domain.SprinkledMoney;
import io.github.ivvve.luckmoney.domain.token.Token;
import io.github.ivvve.luckmoney.ui.request.SprinkleMoneyRequest;
import io.github.ivvve.luckmoney.ui.response.PickSprinkledMoneyResponse;
import io.github.ivvve.luckmoney.ui.response.ReadSprinkledMoneyResponse;
import io.github.ivvve.luckmoney.ui.response.SprinkleMoneyResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/sprinkling-money")
@RequiredArgsConstructor
public class SprinklingMoneyController {
    private final SprinklingMoneyService sprinklingMoneyService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SprinkleMoneyResponse sprinkleMoney(
            // TODO header validation not working
            @Valid @RequestHeader("X-USER-ID") @NotBlank @Pattern(regexp = "^[0-9]*$") final String userId,
            @Valid @RequestHeader("X-ROOM-ID") @NotBlank final String roomId,
            @Valid @RequestBody final SprinkleMoneyRequest request
    ) {
        final SprinkledMoney sprinkledMoney =
                this.sprinklingMoneyService.sprinkle(userId, roomId, request.sprinkledMoney, request.numberOfRoomMembers);
        return new SprinkleMoneyResponse(sprinkledMoney.getTokenValue());
    }

    @PatchMapping("/{token}")
    @ResponseStatus(HttpStatus.OK)
    public PickSprinkledMoneyResponse pickSprinkledMoney(
            // TODO header validation not working
            @Valid @RequestHeader("X-USER-ID") @NotBlank @Pattern(regexp = "^[0-9]*$") final String userId,
            @Valid @RequestHeader("X-ROOM-ID") @NotBlank final String roomId,
            @Valid @PathVariable("token") @Length(min = Token.SIZE, max = Token.SIZE) final String token
    ) {
        final SprinkledMoney sprinkledMoney = this.sprinklingMoneyService.pick(userId, roomId, token);
        final PickedMoney pickedMoney = sprinkledMoney.getPickedMoneys().stream()
                .filter(picked -> Objects.equals(picked.getPickerUserId(), userId))
                .findFirst()
                .orElseThrow();

        return new PickSprinkledMoneyResponse(pickedMoney.getMoney().getAmount());
    }

    @GetMapping("/{token}")
    @ResponseStatus(HttpStatus.OK)
    public ReadSprinkledMoneyResponse readSprinkledMoney(
            // TODO header validation not working
            @Valid @RequestHeader("X-USER-ID") @NotBlank @Pattern(regexp = "^[0-9]*$") final String userId,
            @Valid @RequestHeader("X-ROOM-ID") @NotBlank final String roomId,
            @Valid @PathVariable("token") @Length(min = Token.SIZE, max = Token.SIZE) final String token
    ) {
        final SprinkledMoney sprinkledMoney = this.sprinklingMoneyService.read(userId, token);

        int sprinkledMoneyAmount = 0;
        int pickedMoneyAmount = 0;
        final List<ReadSprinkledMoneyResponse.PickedMoney> pickedMoneys = new ArrayList<>();

        for (final PickedMoney pickedMoney : sprinkledMoney.getPickedMoneys()) {
            if (Objects.nonNull(pickedMoney.getPickerUserId())) {
                pickedMoneyAmount += pickedMoney.getAmount();
                pickedMoneys.add(new ReadSprinkledMoneyResponse.PickedMoney(
                        pickedMoney.getPickerUserId(), pickedMoney.getAmount()));
            }

            sprinkledMoneyAmount += pickedMoney.getAmount();
        }

        return new ReadSprinkledMoneyResponse(sprinkledMoneyAmount, pickedMoneyAmount, pickedMoneys,
                sprinkledMoney.getSprinkledAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
}
