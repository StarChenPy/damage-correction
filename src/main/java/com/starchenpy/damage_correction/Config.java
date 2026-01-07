package com.starchenpy.damage_correction;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

@Mod.EventBusSubscriber(modid = DamageCorrectionMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static final List<String> defaultBoss = List.of("minecraft:ender_dragon", "minecraft:wither", "minecraft:elder_guardian", "minecraft:warden");

    private static final ForgeConfigSpec.BooleanValue ENABLED = BUILDER
            .comment("Enable damage correction?")
            .define("enabled", true);

    private static final ForgeConfigSpec.DoubleValue THRESHOLD_RATIO = BUILDER
            .comment("Damage correction threshold ratio.")
            .defineInRange("thresholdRatio", 0.5, 0, 10);

    private static final ForgeConfigSpec.BooleanValue APPLY_TO_BOSS = BUILDER
            .comment("Apply to bosses?")
            .define("applyToBoss", true);

    private static final ForgeConfigSpec.IntValue BOSS_HP_THRESHOLD = BUILDER
            .comment("Boss HP threshold. Entities with HP above this value are considered bosses. Works together with custom boss settings.")
            .defineInRange("boss_hp_threshold", 200, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> BOSS_STRINGS = BUILDER
            .comment("Custom boss entity identifiers.")
            .defineListAllowEmpty("bosses", defaultBoss, Config::validateBossName);

    private static final ForgeConfigSpec.BooleanValue APPLY_TO_PLAYER = BUILDER
            .comment("Apply to players?")
            .define("applyToPlayer", false);

    private static final ForgeConfigSpec.BooleanValue ONLY_PLAYER_DAMAGE = BUILDER
            .comment("Only allow damage correction to be triggered by players?")
            .define("onlyPlayerDamage", true);

    private static boolean validateBossName(final Object obj) {
        return obj instanceof final String bossName && ForgeRegistries.ENTITY_TYPES.containsKey(ResourceLocation.parse(bossName));
    }

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean enabled;
    public static double thresholdRatio;
    public static boolean applyToBoss;
    public static int boss_hp_threshold;
    public static List<? extends String> bossStrings;
    public static boolean applyToPlayer;
    public static boolean onlyPlayerDamage;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        enabled = ENABLED.get();
        thresholdRatio = THRESHOLD_RATIO.get();
        applyToBoss = APPLY_TO_BOSS.get();
        boss_hp_threshold = BOSS_HP_THRESHOLD.get();
        bossStrings = BOSS_STRINGS.get();
        applyToPlayer = APPLY_TO_PLAYER.get();
        onlyPlayerDamage = ONLY_PLAYER_DAMAGE.get();
    }

    public static void save() {
        ENABLED.set(enabled);
        THRESHOLD_RATIO.set(thresholdRatio);
        APPLY_TO_BOSS.set(applyToBoss);
        BOSS_HP_THRESHOLD.set(boss_hp_threshold);
        BOSS_STRINGS.set(bossStrings);
        APPLY_TO_PLAYER.set(applyToPlayer);
        ONLY_PLAYER_DAMAGE.set(onlyPlayerDamage);

        SPEC.save();
    }
}
