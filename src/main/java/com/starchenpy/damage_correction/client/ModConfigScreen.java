package com.starchenpy.damage_correction.client;

import com.starchenpy.damage_correction.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
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
    private Checkbox excludeBoss;
    private Checkbox excludePlayer;
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
        int y = PADDING;
        int spacing = WIDGET_HEIGHT + PADDING * 2;
        int width = 100;

        // 开关区
        enabled = new Checkbox(x, y, width, WIDGET_HEIGHT, Component.literal("启用伤害修正"), Config.enabled);
        this.addRenderableWidget(enabled);
        y += spacing;

        excludeBoss = new Checkbox(x, y, width, WIDGET_HEIGHT, Component.literal("不对 Boss 触发"), Config.excludeBoss);
        this.addRenderableWidget(excludeBoss);
        y += spacing;

        excludePlayer = new Checkbox(x, y, width, WIDGET_HEIGHT, Component.literal("不对玩家触发"), Config.excludePlayer);
        this.addRenderableWidget(excludePlayer);
        y += spacing;

        onlyPlayerDamage = new Checkbox(x, y, width, WIDGET_HEIGHT, Component.literal("仅能由玩家触发"), Config.onlyPlayerDamage);
        this.addRenderableWidget(onlyPlayerDamage);

        // 输入区
        x = width + PADDING * 2;
        y = PADDING;

        StringWidget thresholdRatioString = new StringWidget(Component.literal("阈值比例"), this.font);
        thresholdRatioString.setX(x);
        thresholdRatioString.setY(y);
        this.addRenderableWidget(thresholdRatioString);
        y += PADDING * 2;

        thresholdRatio = new EditBox(this.font, x, y, width, WIDGET_HEIGHT, Component.empty());
        thresholdRatio.setValue(String.valueOf(Config.thresholdRatio));
        thresholdRatio.setFilter(s -> s.matches("\\d*(\\.\\d*)?"));
        this.addRenderableWidget(thresholdRatio);
        y += spacing;

        StringWidget bossHpThresholdString = new StringWidget(Component.literal("Boss 血量阈值（0时关闭）"), this.font);
        bossHpThresholdString.setX(x);
        bossHpThresholdString.setY(y);
        this.addRenderableWidget(bossHpThresholdString);
        y += PADDING * 2;

        bossHpThreshold = new EditBox(this.font, x, y, width, WIDGET_HEIGHT, Component.empty());
        bossHpThreshold.setValue(String.valueOf(Config.boss_hp_threshold));
        bossHpThreshold.setFilter(s -> s.matches("\\d*"));
        this.addRenderableWidget(bossHpThreshold);

        // Boss 列表
        width = this.width / 3;
        x = this.width - width - PADDING;
        y = PADDING;

        StringWidget bossListString = new StringWidget(Component.literal("自定义 Boss 列表"), this.font);
        bossListString.setX(x);
        bossListString.setY(y);
        this.addRenderableWidget(bossListString);
        y += PADDING * 2;

        bossList = new MultiLineEditBox(this.font, x, y, width, this.height - spacing * 2, Component.empty(), Component.empty());
        bossList.setValue(String.join("\n", Config.bossStrings));
        this.addRenderableWidget(bossList);

        // 底部按钮
        this.addRenderableWidget(
                Button.builder(
                        CommonComponents.GUI_CANCEL,
                        b -> this.client.setScreen(parent)
                ).bounds(centerX - 110, this.height - PADDING - WIDGET_HEIGHT, 100, WIDGET_HEIGHT).build()
        );

        this.addRenderableWidget(
                Button.builder(
                        CommonComponents.GUI_DONE,
                        b -> {
                            save();
                            this.client.setScreen(parent);
                        }
                ).bounds(centerX + 10, this.height - PADDING - WIDGET_HEIGHT, 100, WIDGET_HEIGHT).build()
        );
    }

    private void save() {
        Config.enabled = enabled.selected();
        Config.excludeBoss = excludeBoss.selected();
        Config.excludePlayer = excludePlayer.selected();
        Config.onlyPlayerDamage = onlyPlayerDamage.selected();

        try {
            Config.thresholdRatio = Double.parseDouble(thresholdRatio.getValue());
        } catch (NumberFormatException ignored) {}

        try {
            Config.boss_hp_threshold = Integer.parseInt(bossHpThreshold.getValue());
        } catch (NumberFormatException ignored) {}

        Config.bossStrings = Arrays.stream(bossList.getValue().split("\n"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        Config.save();
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        this.client.setScreen(parent);
    }
}
