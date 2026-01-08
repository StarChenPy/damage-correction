package com.starchenpy.damage_correction.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import javax.annotation.ParametersAreNonnullByDefault;

public class StringWidget extends AbstractWidget {
    private final Font font;

    public StringWidget(Component context, Font font) {
        super(0, 0, font.width(context.getVisualOrderText()), 9, context);
        this.font = font;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void m_6305_(PoseStack p_94669_, int p_94670_, int p_94671_, float p_94672_) {
        m_93243_(p_94669_, font, this.getMessage(), this.x + 24, this.y + (this.height - 8) / 2, 14737632 | Mth.ceil(this.alpha * 255.0F) << 24);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public void updateNarration(NarrationElementOutput pNarrationElementOutput) {
        pNarrationElementOutput.add(NarratedElementType.TITLE, this.createNarrationMessage());
    }
}
