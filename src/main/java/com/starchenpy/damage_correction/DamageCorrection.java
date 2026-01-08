package com.starchenpy.damage_correction;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(DamageCorrection.MOD_ID)
public class DamageCorrection {
    public static final String MOD_ID = "damage_correction";

    public DamageCorrection(IEventBus modEventBus) {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
