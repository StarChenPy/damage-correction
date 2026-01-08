package com.starchenpy.damage_correction.common.event;

import com.starchenpy.damage_correction.Config;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber
public class OnHurtListener {

    // 被攻击时
    @SubscribeEvent
    public static void onHurt(LivingDamageEvent.Pre event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }

        if (!Config.ENABLED.get()) {
            return;
        }

        // 判定是否对非玩家造成的伤害触发效果
        boolean sourceIsPlayer = event.getSource().getEntity() instanceof Player;
        if (Config.ONLY_PLAYER_DAMAGE.get() && !sourceIsPlayer) {
            return;
        }

        // 判定能否对玩家触发效果
        boolean targetIsPlayer = event.getEntity() instanceof Player;
        if (!Config.APPLY_TO_PLAYER.get() && targetIsPlayer) {
            return;
        }

        // 判定能否对 Boss 触发效果
        if (!Config.APPLY_TO_BOSS.get() && isBoss(event.getEntity())) {
            return;
        }

        float targetHealth = event.getEntity().getHealth();
        float targetAmount = event.getNewDamage();
        float damageDiff = targetHealth - targetAmount;
        if (targetHealth > targetAmount && damageDiff <= targetAmount * Config.THRESHOLD_RATIO.get()) {
            event.setNewDamage(event.getNewDamage() + damageDiff);
        }
    }

    public static boolean isBoss(LivingEntity entity) {
        ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        if (Config.BOSS_STRINGS.get().contains(id.toString())) {
            return true;
        }

        int bossHpThreshold = Config.BOSS_HP_THRESHOLD.get();
        if (bossHpThreshold != 0) {
            return entity.getMaxHealth() >= bossHpThreshold;
        }

        return false;
    }
}
