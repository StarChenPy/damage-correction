package com.starchenpy.damagecorrection;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DamageCorrectionMod.MOD_ID)
public class DamageCorrectionMod {
    public static final String MOD_ID = "damage_correction";

    public DamageCorrectionMod(FMLJavaModLoadingContext context) {
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
