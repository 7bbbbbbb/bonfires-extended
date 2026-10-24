package net.x7bbbbbbb.bonfires_ex.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.x7bbbbbbb.bonfires_ex.BonfireLandmarkManager;
import net.x7bbbbbbb.bonfires_ex.BonfiresExtended;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.bonfire.BonfireRegistry;
import wehavecookies56.bonfires.data.BonfireHandler;

import java.util.UUID;

@Mixin(value = BonfireHandler.class, remap = false)
public abstract class BonfireHandlerMixin {
    /*
    @Shadow
    BonfireRegistry registry;

    @Inject(method = "addBonfire", at = @At("TAIL"))
    private void onAddBonfire(Bonfire bonfire, CallbackInfoReturnable<Boolean> cir) {
        if (BonfiresExtended.SURVEYOR_AVAILABLE && cir.getReturnValue()) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                ServerLevel targetWorld = server.getLevel(bonfire.getDimension());
                if (targetWorld != null) {
                    BonfireLandmarkManager.updateBonfireLandmark(targetWorld, bonfire);
                }
            }
        }
    }

    @Inject(method = "removeBonfire", at = @At("HEAD"))
    private void onRemoveBonfire(UUID id, CallbackInfoReturnable<Boolean> cir) {
        if (BonfiresExtended.SURVEYOR_AVAILABLE) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null && this.registry != null) {
                Bonfire bonfire = this.registry.getBonfires().get(id);
                if (bonfire != null) {
                    ServerLevel targetWorld = server.getLevel(bonfire.getDimension());
                    if (targetWorld != null) {
                        BonfireLandmarkManager.removeBonfireLandmark(targetWorld, id);
                    }
                }
            }
        }
    }

     */
}