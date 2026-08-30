package dev.damocles.vertigoes;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // Primal Pearl variants requirements

    public static final ModConfigSpec.IntValue ANIMAL_PEARL_REQ = BUILDER
        .comment("How many animals you have to breed to convert a Primal Pearl into Animal Life variant")
        .defineInRange("primalPearlAnimalReq", 20, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue AQUATIC_PEARL_REQ = BUILDER
        .comment("How many Drowned you have to kill to convert a Primal Pearl into Aquatic Life variant")
        .defineInRange("primalPearlAquaticReq", 25, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue DEATH_PEARL_REQ = BUILDER
        .comment("How many Villagers you have to kill to convert a Primal Pearl into Death variant")
        .defineInRange("primalPearlDeathReq", 13, 1, Integer.MAX_VALUE);

    // Primal Pearl variants can be disabled

    public static final ModConfigSpec.BooleanValue ANIMAL_PEARL_CAN_BE_DISABLED = BUILDER
        .comment("Whether Animal Life Primal Pearl variant gets disabled if you kill a Villager or an animal")
        .define("primalPearlAnimalCanBeDisabled", true);

    public static final ModConfigSpec.BooleanValue AQUATIC_PEARL_CAN_BE_DISABLED = BUILDER
        .comment("Whether Aquatic Life Primal Pearl variant gets disabled if you kill any fish/water friendly creature")
        .define("primalPearlAquaticCanBeDisabled", true);

    public static final ModConfigSpec.BooleanValue DEATH_PEARL_CAN_BE_DISABLED = BUILDER
        .comment("Whether Death Primal Pearl variant gets disabled if you cure a Zombie Villager")
        .define("primalPearlDeathCanBeDisabled", true);

    // Heart of Glass area of effect

    public static final ModConfigSpec.IntValue GLASS_HEART_RADIUS = BUILDER
        .comment("The Area of Effect of the Heart of Glass (radius)")
        .defineInRange("glassHeartRadius", 4, 0, 30);

    public static final ModConfigSpec.BooleanValue ENABLE_DEBUGGING = BUILDER
        .comment("Whether to enable debugging")
        .define("enableDebugging", false);

    static final ModConfigSpec SPEC = BUILDER.build();
}
