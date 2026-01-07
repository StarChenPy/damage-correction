package com.starchenpy.damage_correction;

import com.starchenpy.damage_correction.client.ModConfigScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod(DamageCorrectionMod.MOD_ID)
public class DamageCorrectionMod {
    public static final String MOD_ID = "damage_correction";

    public DamageCorrectionMod(FMLJavaModLoadingContext context) {
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        if (FMLLoader.getDist() != Dist.DEDICATED_SERVER) {
            ConfigScreenHandler.ConfigScreenFactory factory = new ConfigScreenHandler.ConfigScreenFactory(ModConfigScreen::new);
            context.registerExtensionPoint(factory.getClass(), () -> factory);
        }
    }
}
