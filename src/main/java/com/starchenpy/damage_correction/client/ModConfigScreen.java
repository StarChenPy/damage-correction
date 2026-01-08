package com.starchenpy.damage_correction.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.starchenpy.damage_correction.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ModConfigScreen extends Screen {
    private static final int PADDING = 6;
    private static final int WIDGET_HEIGHT = 20;

    private final Screen parent;
    private final Minecraft client;

    private Checkbox enabled;
    private Checkbox applyToBoss;
    private Checkbox applyToPlayer;
    private Checkbox onlyPlayerDamage;

    private EditBox thresholdRatio;
    private EditBox bossHpThreshold;
    private MultiLineEditBox bossList;

    public ModConfigScreen(Minecraft client, Screen parent) {
        super(Component.translatable("screen.title.damage_correction.config"));
        this.parent = parent;
        this.client = client;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int x = PADDING;
        int y = PADDING * 2 + 9;
        int spacing = WIDGET_HEIGHT + PADDING * 2;
        int width = 120;

        // 标题
        StringWidget titleString = new StringWidget(title, this.font);
        titleString.setX(centerX - this.font.width(title.getVisualOrderText()) / 2);
        titleString.setY(PADDING);
        this.addRenderableWidget(titleString);

        // 开关区
        enabled = new Checkbox(x, y, width, WIDGET_HEIGHT, Component.translatable("screen.title.damage_correction.enable"), Config.enabled);
        this.addRenderableWidget(enabled);
        y += spacing;

        applyToBoss = new Checkbox(x, y, width, WIDGET_HEIGHT, Component.translatable("screen.title.damage_correction.apply_to_boss"), Config.applyToBoss);
        this.addRenderableWidget(applyToBoss);
        y += spacing;

        applyToPlayer = new Checkbox(x, y, width, WIDGET_HEIGHT, Component.translatable("screen.title.damage_correction.apply_to_player"), Config.applyToPlayer);
        this.addRenderableWidget(applyToPlayer);
        y += spacing;

        onlyPlayerDamage = new Checkbox(x, y, width, WIDGET_HEIGHT, Component.translatable("screen.title.damage_correction.only_player_damage"), Config.onlyPlayerDamage);
        this.addRenderableWidget(onlyPlayerDamage);

        // 输入区
        x = width + PADDING * 2;
        y = PADDING * 2 + 9;

        StringWidget thresholdRatioString = new StringWidget(Component.translatable("screen.title.damage_correction.threshold_ratio"), this.font);
        thresholdRatioString.setX(x);
        thresholdRatioString.setY(y);
        this.addRenderableWidget(thresholdRatioString);
        y += PADDING * 2;

        thresholdRatio = new EditBox(this.font, x, y, width, WIDGET_HEIGHT, Component.empty());
        thresholdRatio.setValue(String.valueOf(Config.thresholdRatio));
        thresholdRatio.setFilter(s -> s.matches("\\d*(\\.\\d*)?"));
        this.addRenderableWidget(thresholdRatio);
        y += spacing;

        StringWidget bossHpThresholdString = new StringWidget(Component.translatable("screen.title.damage_correction.boss_hp_threshold"), this.font);
        bossHpThresholdString.setX(x);
        bossHpThresholdString.setY(y);
        this.addRenderableWidget(bossHpThresholdString);
        y += PADDING * 2;

        bossHpThreshold = new EditBox(this.font, x, y, width, WIDGET_HEIGHT, Component.empty());
        bossHpThreshold.setValue(String.valueOf(Config.bossHpThreshold));
        bossHpThreshold.setFilter(s -> s.matches("\\d*"));
        this.addRenderableWidget(bossHpThreshold);

        // Boss 列表
        width = this.width / 3;
        x = this.width - width - PADDING;
        y = PADDING * 2 + 9;

        StringWidget bossListString = new StringWidget(Component.translatable("screen.title.damage_correction.boss_list"), this.font);
        bossListString.setX(x);
        bossListString.setY(y);
        this.addRenderableWidget(bossListString);
        y += PADDING * 2;

        bossList = new MultiLineEditBox(this.font, x, y, width, this.height - spacing * 2, Component.empty(), Component.empty());
        bossList.setValue(String.join("\n", Config.bossStrings));
        this.addRenderableWidget(bossList);

        // 底部按钮
        this.addRenderableWidget(
                new Button(centerX - 110,
                        this.height - PADDING - WIDGET_HEIGHT,
                        100,
                        WIDGET_HEIGHT,
                        CommonComponents.GUI_CANCEL,
                        b -> this.client.setScreen(parent)));
        this.addRenderableWidget(
                new Button(centerX + 10,
                        this.height - PADDING - WIDGET_HEIGHT,
                        100,
                        WIDGET_HEIGHT,
                        CommonComponents.GUI_DONE,
                        b -> {
                            save();
                            this.client.setScreen(parent);
                        }));
    }

    private void save() {
        Config.enabled = enabled.selected();
        Config.applyToBoss = applyToBoss.selected();
        Config.applyToPlayer = applyToPlayer.selected();
        Config.onlyPlayerDamage = onlyPlayerDamage.selected();

        try {
            Config.thresholdRatio = Double.parseDouble(thresholdRatio.getValue());
        } catch (NumberFormatException ignored) {}

        try {
            Config.bossHpThreshold = Integer.parseInt(bossHpThreshold.getValue());
        } catch (NumberFormatException ignored) {}

        Config.bossStrings = Arrays.stream(bossList.getValue().split("\n"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());

        Config.save();
    }

    @Override
    @ParametersAreNonnullByDefault
    public void m_6305_(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.m_7333_(poseStack);
        super.m_6305_(poseStack, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        this.client.setScreen(parent);
    }
}
