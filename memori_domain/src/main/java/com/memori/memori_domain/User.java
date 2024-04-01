package com.memori.memori_domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "app_user")
public class User {

    @Id
    private String id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isEmailVerified = false;

    public enum Tier {
        BASIC(1),
        PRO(2);

        private final int value;

        Tier(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static Tier fromValue(int value) {
            for (Tier tier : Tier.values()) {
                if (tier.getValue() == value) {
                    return tier;
                }
            }
            throw new IllegalArgumentException("No matching tier for value: " + value);
        }

        public static boolean isValidCode(int value) {
            for (Tier tier : Tier.values())
                if (tier.getValue() == value)
                    return true;
            return false;
        }
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    @Builder.Default
    private Tier tier = Tier.BASIC;

    public void setTier(int tier) {
        this.tier = Tier.fromValue(tier);
    }

    public int getTier() {
        return tier.getValue();
    }

    @Column(nullable = false)
    @Builder.Default
    private Long storageSizeInByte = 0L;

    @Column(nullable = false)
    @Builder.Default
    private String timezone = "Asia/Singapore";

    @Column(nullable = false)
    @Builder.Default
    private Integer dailyResetTime = 2;
}
