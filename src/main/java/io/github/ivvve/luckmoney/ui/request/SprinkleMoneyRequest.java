package io.github.ivvve.luckmoney.ui.request;

import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Value
public class SprinkleMoneyRequest {
    @NotNull
    @Min(1)
    public final Integer sprinkledMoney;

    @NotNull
    @Min(1)
    public final Integer numberOfRoomMembers;
}
