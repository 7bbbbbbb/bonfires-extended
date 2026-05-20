package net.x7bbbbbbb.bonfires_ex.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.x7bbbbbbb.bonfires_ex.BonfireLandmarkManager;
import net.x7bbbbbbb.bonfires_ex.BonfiresExtended;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.bonfire.BonfireRegistry;

import java.util.UUID;

@Mixin(targets = "wehavecookies56.bonfires.data.BonfireHandler$Default")
public abstract class BonfireHandlerDefaultMixin {

    @Shadow
    World provider;

    @Shadow
    BonfireRegistry registry;

    @Inject(method = "addBonfire", at = @At("TAIL"))
    private void onAddBonfire(Bonfire bonfire, CallbackInfoReturnable<Boolean> cir) {
        if (BonfiresExtended.SURVEYOR_AVAILABLE && cir.getReturnValue() && this.provider != null && !this.provider.isClient()) {
            MinecraftServer server = this.provider.getServer();
            if (server != null) {
                ServerWorld targetWorld = server.getWorld(bonfire.getDimension());
                if (targetWorld != null) {
                    BonfireLandmarkManager.updateBonfireLandmark(targetWorld, bonfire);
                }
            }
        }
    }

    @Inject(method = "removeBonfire", at = @At("HEAD"))
    private void onRemoveBonfire(UUID id, CallbackInfoReturnable<Boolean> cir) {
        if (BonfiresExtended.SURVEYOR_AVAILABLE && this.provider != null && !this.provider.isClient()) {
            MinecraftServer server = this.provider.getServer();
            if (server != null && this.registry != null) {
                Bonfire bonfire = this.registry.getBonfires().get(id);
                if (bonfire != null) {
                    ServerWorld targetWorld = server.getWorld(bonfire.getDimension());
                    if (targetWorld != null) {
                        BonfireLandmarkManager.removeBonfireLandmark(targetWorld, id);
                    }
                }
            }
        }
    }
}