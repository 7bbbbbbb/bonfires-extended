package net.x7bbbbbbb.bonfires_ex.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Mixin(targets = "wehavecookies56.bonfires.client.ScreenshotUtils")
public class ScreenshotUtilsMixin {
    private static final Logger LOGGER = LoggerFactory.getLogger("BonfireResizer");

    @Inject(
            method = "takeScreenshot(Ljava/lang/String;Ljava/util/UUID;)V",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private static void onTakeScreenshot(String bonfireName, UUID bonfireUUID, CallbackInfo ci) {
        Path p = Paths.get(MinecraftClient.getInstance().runDirectory.getPath(), "bonfires");
        String nameNoInvalid = bonfireName.replaceAll("[\\\\/:*?\"<>|]", "_").toLowerCase();
        String fileName = nameNoInvalid + "_" + bonfireUUID.toString() + ".png";
        File fileToCreate = new File(p.toFile(), fileName);

        try {
            Files.createDirectories(p);
        } catch (IOException e) {
            LOGGER.error("Failed to create bonfires storage directory", e);
        }
        NativeImage fullImage = ScreenshotRecorder.takeScreenshot(MinecraftClient.getInstance().getFramebuffer());
        int targetWidth = 320;
        int targetHeight = 180;
        NativeImage resizedImage = new NativeImage(fullImage.getFormat(), targetWidth, targetHeight, false);

        try {
            for (int y = 0; y < targetHeight; y++) {
                for (int x = 0; x < targetWidth; x++) {
                    int srcX = x * fullImage.getWidth() / targetWidth;
                    int srcY = y * fullImage.getHeight() / targetHeight;

                    resizedImage.setColor(x, y, fullImage.getColor(srcX, srcY));
                }
            }
            resizedImage.writeTo(fileToCreate);
            LOGGER.info("Successfully saved downscaled bonfire thumbnail: {}", fileName);

        } catch (IOException ioexception) {
            LOGGER.warn("Failed saving downscaled screenshot", ioexception);
        } finally {
            fullImage.close();
            resizedImage.close();
        }
        ci.cancel();
    }
}