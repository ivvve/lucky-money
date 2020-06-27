package io.github.ivvve.luckmoney.ui.response;

import lombok.Value;

import java.util.List;

@Value
public class ReadSprinkledMoneyResponse {
    public final long sprinkledMoney;
    public final long pickedMoney;
    public final List<PickedMoney> pickedMoneys;
    public final String sprinkledAt;

    @Value
    public static class PickedMoney {
        public final String userId;
        public final long money;
    }
}