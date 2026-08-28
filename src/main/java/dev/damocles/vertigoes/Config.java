package dev.damocles.vertigoes;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue ENABLE_DEBUGGING = BUILDER
            .comment("Whether to enable debugging")
            .define("enableDebugging", true);

    static final ModConfigSpec SPEC = BUILDER.build();
}
