package io.github.ivvve.luckmoney.domain;

import io.github.ivvve.luckmoney.domain.exceptions.InvalidMoney;
import io.github.ivvve.luckmoney.domain.money.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("PickedMoney class")
class PickedMoneyTest {

    @Nested @DisplayName("constructor")
    class constructor {

        @Nested @DisplayName("when money is null")
        class when_money_is_null {
            final Money money = null;

            @Test @DisplayName("throws InvalidMoney")
            void throws_InvalidMoney() {
                assertThrows(InvalidMoney.class, () -> new PickedMoney(money));
            }
        }

        @Nested @DisplayName("when money is not null")
        class when_money_is_not_null {
            final Money money = new Money(1);

            @Test @DisplayName("returns PickedMoney normally")
            void returns_PickedMoney_normally() {
                final PickedMoney pickedMoney = new PickedMoney(money);
            }
        }
    }

    @Nested @DisplayName("pickedBy method")
    class pickedBy_method {
        final String pickerUserId = "00001";

        @Nested @DisplayName("when PickMoney is picked")
        class when_already_picked {
            final PickedMoney pickedMoney = new PickedMoney(new Money(1));

            @Test @DisplayName("throws AlreadyPickedSprinkledMoney")
            void returns_true() {

            }
        }

        @Nested @DisplayName("when PickMoney is not picked")
        class when_PickMoney_is_not_picked {
            final String pickerUserId = "00001";

            @Test @DisplayName("sets pickerUserId")
            void setsPickerUserId() {
                final PickedMoney pickedMoney = new PickedMoney(new Money(1));
                pickedMoney.pickedBy(pickerUserId);

                assertThat(pickedMoney.getPickerUserId()).isEqualTo(pickerUserId);
            }

            @Test @DisplayName("sets pickedAt now")
            void sets_pickedAt_now() {
                final LocalDateTime beforePicked = LocalDateTime.now();

                final PickedMoney pickedMoney = new PickedMoney(new Money(1));
                pickedMoney.pickedBy(pickerUserId);

                final LocalDateTime afterPicked = LocalDateTime.now();

                assertThat(pickedMoney.getPickedAt()).isBetween(beforePicked, afterPicked);
            }
        }
    }

    @Nested @DisplayName("isPickedBy method")
    class isPickedBy_method {
        final String pickerUserId = "00001";

        @Nested @DisplayName("when there's no user picked")
        class when_theres_no_user_picked {

            @Test @DisplayName("returns false")
            void returns_false() {
                final PickedMoney unpickedMoney = new PickedMoney(new Money(1));
                assertThat(unpickedMoney.isPickedBy(pickerUserId)).isFalse();
            }
        }

        @Nested @DisplayName("when the same user picked")
        class when_the_same_user_picked {

            @Test @DisplayName("returns true")
            void returns_true() {
                final PickedMoney pickedMoney = new PickedMoney(new Money(1));
                pickedMoney.pickedBy(pickerUserId);
                assertThat(pickedMoney.isPickedBy(pickerUserId)).isTrue();
            }
        }

        @Nested @DisplayName("when other user picked")
        class when_other_user_picked {
            final String otherPickerUserId = pickerUserId + "a";

            @Test @DisplayName("returns false")
            void returns_false() {
                final PickedMoney pickedMoney = new PickedMoney(new Money(1));
                pickedMoney.pickedBy(otherPickerUserId);
                assertThat(pickedMoney.isPickedBy(isPickedBy_method.this.pickerUserId)).isFalse();
            }
        }
    }

    @Nested @DisplayName("isPicked method")
    class isNotPicked_method {
        @Nested @DisplayName("when user picked")
        class when_user_picked {
            final PickedMoney pickedMoney = new PickedMoney(new Money(1));
            { pickedMoney.pickedBy("picker"); }

            @Test @DisplayName("returns true")
            void returns_true() {
                assertThat(pickedMoney.isPicked()).isTrue();
            }
        }

        @Nested @DisplayName("when there's no user picked")
        class when_theres_no_user_picked {

            @Test @DisplayName("returns false")
            void returns_true() {
                final PickedMoney unpickedMoney = new PickedMoney(new Money(1));
                assertThat(unpickedMoney.isPicked()).isFalse();
            }
        }
    }
}