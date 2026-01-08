package com.starchenpy.damage_correction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    private static final Set<String> defaultBoss = Set.of("minecraft:ender_dragon", "minecraft:wither", "minecraft:elder_guardian", "minecraft:warden");

    public static final ModConfigSpec.BooleanValue ENABLED = BUILDER
            .comment("Enable damage correction?")
            .define("enabled", true);

    public static final ModConfigSpec.DoubleValue THRESHOLD_RATIO = BUILDER
            .comment("Damage correction threshold ratio.")
            .defineInRange("thresholdRatio", 0.5, 0, 10);

    public static final ModConfigSpec.BooleanValue APPLY_TO_BOSS = BUILDER
            .comment("Apply to bosses?")
            .define("applyToBoss", true);

    public static final ModConfigSpec.IntValue BOSS_HP_THRESHOLD = BUILDER
            .comment("Boss HP threshold. Entities with HP above this value are considered bosses. Works together with custom boss settings.")
            .defineInRange("boss_hp_threshold", 200, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.ConfigValue<List<? extends String>> BOSS_STRINGS = BUILDER
            .comment("Custom boss entity identifiers.")
            .defineListAllowEmpty("bosses", new ArrayList<>(defaultBoss), Config::validateBossName);

    public static final ModConfigSpec.BooleanValue APPLY_TO_PLAYER = BUILDER
            .comment("Apply to players?")
            .define("applyToPlayer", false);

    public static final ModConfigSpec.BooleanValue ONLY_PLAYER_DAMAGE = BUILDER
            .comment("Only allow damage correction to be triggered by players?")
            .define("onlyPlayerDamage", true);

    public static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean validateBossName(final Object obj) {
        return obj instanceof final String bossName && BuiltInRegistries.ENTITY_TYPE.containsKey(new ResourceLocation(bossName));
    }
}
