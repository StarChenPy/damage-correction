package com.starchenpy.damagecorrection;

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
            .comment("是否启用伤害修正？")
            .define("enabled", true);

    private static final ForgeConfigSpec.DoubleValue THRESHOLD_RATIO = BUILDER
            .comment("伤害修正阈值.")
            .defineInRange("thresholdRatio", 0.5, 0, 10);

    private static final ForgeConfigSpec.BooleanValue EXCLUDE_BOSS = BUILDER
            .comment("是否对Boss启用？")
            .define("excludeBoss", true);

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> BOSS_STRINGS = BUILDER
            .comment("自定义boss.")
            .defineListAllowEmpty("bosses", defaultBoss, Config::validateBossName);

    private static final ForgeConfigSpec.BooleanValue EXCLUDE_PLAYER = BUILDER
            .comment("是否对玩家启用？")
            .define("excludePlayer", false);

    private static final ForgeConfigSpec.BooleanValue ONLY_PLAYER_DAMAGE = BUILDER
            .comment("是否只有玩家可以触发伤害修正？")
            .define("onlyPlayerDamage", true);

    private static boolean validateBossName(final Object obj) {
        return obj instanceof final String bossName && ForgeRegistries.ENTITY_TYPES.containsKey(ResourceLocation.parse(bossName));
    }

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean enabled;
    public static double thresholdRatio;
    public static boolean excludeBoss;
    public static List<? extends String> bossStrings;
    public static boolean excludePlayer;
    public static boolean onlyPlayerDamage;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        enabled = ENABLED.get();
        thresholdRatio = THRESHOLD_RATIO.get();
        excludeBoss = EXCLUDE_BOSS.get();
        bossStrings = BOSS_STRINGS.get();
        excludePlayer = EXCLUDE_PLAYER.get();
        onlyPlayerDamage = ONLY_PLAYER_DAMAGE.get();
    }
}
