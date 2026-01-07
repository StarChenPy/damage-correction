package com.starchenpy.damage_correction.common.event;

import com.starchenpy.damage_correction.Config;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber
public class OnHurtListener {
    // 被攻击时
    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }

        if (!Config.enabled) {
            return;
        }

        // 判定是否对非玩家造成的伤害触发效果
        boolean sourceIsPlayer = event.getSource().getEntity() instanceof Player;
        if (Config.onlyPlayerDamage && !sourceIsPlayer) {
            return;
        }

        // 判定能否对玩家触发效果
        boolean targetIsPlayer = event.getEntity() instanceof Player;
        if (!Config.applyToPlayer && targetIsPlayer) {
            return;
        }

        // 判定能否对 Boss 触发效果
        if (!Config.applyToBoss && isBoss(event.getEntity())) {
            return;
        }

        float targetHealth = event.getEntity().getHealth();
        float targetAmount = event.getAmount();
        float damageDiff = targetHealth - targetAmount;
        if (targetHealth > targetAmount && damageDiff <= targetAmount * Config.thresholdRatio) {
            event.setAmount(event.getAmount() + damageDiff);
        }
    }

    public static boolean isBoss(LivingEntity entity) {
        ResourceLocation id = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        if (id != null && Config.bossStrings.contains(id.toString())) {
            return true;
        }

        if (Config.boss_hp_threshold != 0) {
            return entity.getMaxHealth() >= Config.boss_hp_threshold;
        }

        return false;
    }
}
