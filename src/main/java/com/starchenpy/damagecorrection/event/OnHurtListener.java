package com.starchenpy.damagecorrection.event;

import com.starchenpy.damagecorrection.Config;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber
public class OnHurtListener {
    // 被攻击时
    @SubscribeEvent
    public static void onHurt(LivingDamageEvent event) {
        if (!Config.enabled) {
            return;
        }

        boolean targetIsPlayer = event.getEntity() instanceof Player;
        boolean sourceIsPlayer = event.getSource().getEntity() instanceof Player;

        // 判定是否对非玩家造成的伤害触发效果
        if (Config.onlyPlayerDamage && !sourceIsPlayer) {
            return;
        }

        // 判定能否对玩家触发效果
        if (!Config.excludePlayer && targetIsPlayer) {
            return;
        }

        // 判定能否对 Boss 触发效果
        if (!Config.excludeBoss && isBoss(event.getEntity())) {
            return;
        }

        float targetHealth = event.getEntity().getHealth();
        float targetAmount = event.getAmount();
        float damageDiff = targetHealth - targetAmount;
        if (targetHealth > targetAmount && damageDiff <= targetAmount * Config.thresholdRatio) {
            event.setAmount(event.getAmount() + damageDiff);
        }
    }

    public static boolean isBoss(Entity entity) {
        // 原版 Boss 类型
        EntityType<?> type = entity.getType();
        if (type == EntityType.ENDER_DRAGON ||
                type == EntityType.WITHER ||
                type == EntityType.ELDER_GUARDIAN ||
                type == EntityType.WARDEN) {
            return true;
        }

        // 额外 Boss 类型
        ResourceLocation id = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        if (id != null) {
            return Config.bossStrings.contains(id.toString());
        }

        return false;
    }
}
