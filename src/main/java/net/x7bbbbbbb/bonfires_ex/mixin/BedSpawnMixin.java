package net.x7bbbbbbb.bonfires_ex.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.x7bbbbbbb.bonfires_ex.BonfiresExtended;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class BedSpawnMixin {

    @Inject(method = "setRespawnPosition", at = @At("HEAD"), cancellable = true)
    private void onSetSpawnPoint(ResourceKey<Level> dimension, @Nullable BlockPos pos, float angle, boolean forced, boolean sendMessage, CallbackInfo ci) {
        if (BonfiresExtended.CONFIG.disableBedSpawn()) {
            ServerPlayer player = (ServerPlayer) (Object) this;

            if (pos != null && !forced) {
                if (player.level().getBlockState(pos).getBlock() instanceof BedBlock) {
                    ci.cancel();
                }
            }
        }
    }
}