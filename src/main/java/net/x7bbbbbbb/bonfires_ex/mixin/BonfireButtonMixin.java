package net.x7bbbbbbb.bonfires_ex.mixin;

import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import wehavecookies56.bonfires.client.gui.BonfireScreen;
import wehavecookies56.bonfires.client.gui.widgets.BonfireButton;

@Mixin(value = BonfireButton.class, remap = false)
public abstract class BonfireButtonMixin extends Button {

    public BonfireButtonMixin(int x, int y, int width, int height, Component message, OnPress onPress, CreateNarration createNarration) {
        super(x, y, width, height, message, onPress, createNarration);
    }

    @Redirect(
            method = "renderWidget(Lnet/minecraft/client/gui/GuiGraphics;IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lwehavecookies56/bonfires/client/gui/BonfireScreen;drawCenteredStringNoShadow(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)V"
            )
    )
    private void redirectBonfireTextRender(BonfireScreen parentScreen, GuiGraphics context, Font textRenderer, String text, int x, int y, int color) {
        if (text != null && text.length() > 14) {
            int padding = 4;
            int maxTextWidth = this.width - (padding * 2);
            int textWidth = textRenderer.width(text);

            context.enableScissor(
                    this.getX() + padding,
                    this.getY(),
                    this.getX() + this.width - padding,
                    this.getY() + this.height
            );

            int scrollOffset = 0;
            int maxScroll = textWidth - maxTextWidth;

            if (maxScroll > 0) {
                long time = Util.getMillis();
                long speed = 30; // Milliseconds per pixel movement
                long pauseDuration = 1200; // Pause at the start and end in milliseconds
                long loopTime = (maxScroll * speed * 2) + (pauseDuration * 2);
                long progress = time % loopTime;
                if (progress < pauseDuration) {
                    scrollOffset = 0;
                } else if (progress < pauseDuration + (maxScroll * speed)) {
                    scrollOffset = (int) ((progress - pauseDuration) / speed);
                } else if (progress < (pauseDuration * 2) + (maxScroll * speed)) {
                    scrollOffset = maxScroll;
                } else {
                    long backwardProgress = progress - ((pauseDuration * 2) + (maxScroll * speed));
                    scrollOffset = maxScroll - (int) (backwardProgress / speed);
                }
            }
            int textY = (this.getY() + this.height / 2) - (textRenderer.lineHeight / 2);
            int textX = this.getX() + padding - scrollOffset;
            context.drawString(textRenderer, text, textX, textY, color, false);
            context.disableScissor();
        } else {
            parentScreen.drawCenteredStringNoShadow(context, textRenderer, text, x, y, color);
        }
    }
}