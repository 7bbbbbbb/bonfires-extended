package net.x7bbbbbbb.bonfires_ex.mixin;

import net.minecraft.block.BedBlock;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.x7bbbbbbb.bonfires_ex.BonfiresExtended;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class BedSpawnMixin {

    @Inject(method = "setSpawnPoint", at = @At("HEAD"), cancellable = true)
    private void onSetSpawnPoint(RegistryKey<World> dimension, @Nullable BlockPos pos, float angle, boolean forced, boolean sendMessage, CallbackInfo ci) {
        if (BonfiresExtended.CONFIG.disableBedSpawn()) {
            ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

            if (pos != null && !forced) {
                if (player.getWorld().getBlockState(pos).getBlock() instanceof BedBlock) {
                    ci.cancel();
                }
            }
        }
    }
}