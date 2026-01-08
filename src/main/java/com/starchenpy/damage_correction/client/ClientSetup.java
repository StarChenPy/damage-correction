package com.starchenpy.damage_correction.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.ConfigScreenHandler;
import net.neoforged.bus.api.SubscribeEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ConfigScreenHandler.ConfigScreenFactory factory =
                new ConfigScreenHandler.ConfigScreenFactory(ModConfigScreen::new);

        ModLoadingContext.get().registerExtensionPoint(factory.getClass(), () -> factory);
    }
}
