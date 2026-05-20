package net.x7bbbbbbb.bonfires_ex.mixin;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wehavecookies56.bonfires.client.gui.BonfireScreen;

@Mixin(value = BonfireScreen.class, remap = false)
public class BonfireScreenMixin {
    @Inject(
            method = "drawCenteredStringNoShadow(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onDrawCenteredStringNoShadow(DrawContext guiGraphics, TextRenderer fr, String text, int x, int y, int color, CallbackInfo ci) {
        if (text != null && text.length() > 14) {
            int maxTextWidth = 75;
            int textWidth = fr.getWidth(text);

            int leftBound = x - (maxTextWidth / 2);
            int rightBound = x + (maxTextWidth / 2);
            guiGraphics.enableScissor(leftBound, y - 10, rightBound, y + fr.fontHeight + 10);

            int scrollOffset = 0;
            int maxScroll = textWidth - maxTextWidth;

            if (maxScroll > 0) {
                long time = Util.getMeasuringTimeMs();
                long speed = 30; // Milliseconds per pixel shift
                long pauseDuration = 1200; // Pause time at edges in milliseconds
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
            int textX = leftBound - scrollOffset;
            guiGraphics.drawText(fr, text, textX, y, color, false);
            guiGraphics.disableScissor();

            ci.cancel();
        }
    }

    @Redirect(
            method = "drawSelectedBonfire",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;IIIZ)I"
            )
    )
    private int redirectSelectedBonfireText(DrawContext context, TextRenderer textRenderer, String text, int x, int y, int color, boolean shadow) {
        if (text != null && text.length() > 14) {
            int maxTextWidth = 80;
            int textWidth = textRenderer.getWidth(text);
            context.enableScissor(
                    x,
                    y,
                    x + maxTextWidth,
                    y + textRenderer.fontHeight + 2
            );

            int scrollOffset = 0;
            int maxScroll = textWidth - maxTextWidth;

            if (maxScroll > 0) {
                long time = Util.getMeasuringTimeMs();
                long speed = 30; // Milliseconds per pixel movement
                long pauseDuration = 1200; // Pause duration at both ends in milliseconds
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
            context.drawText(textRenderer, text, x - scrollOffset, y, color, shadow);
            context.disableScissor();
            return 0;
        }
        return context.drawText(textRenderer, text, x, y, color, shadow);
    }
}