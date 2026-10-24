package net.x7bbbbbbb.bonfires_ex.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.x7bbbbbbb.bonfires_ex.BonfiresExtended;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.BonfiresConfig;
import wehavecookies56.bonfires.data.BonfireHandler;
import wehavecookies56.bonfires.packets.PacketHandler;
import wehavecookies56.bonfires.packets.client.SendBonfiresToClient;
import wehavecookies56.bonfires.packets.server.RequestDimensionsFromServer;

import java.util.Objects;

@Mixin(RequestDimensionsFromServer.class)
public class RequestDimensionsFromServerMixin {

    @Inject(method = "handle", at = @At("HEAD"), cancellable = true)
    private void overrideHandle(IPayloadContext context, CallbackInfo ci) {
        if (BonfiresExtended.CONFIG.disableInvalidBonfiresCheck()) {
            ci.cancel();
            ServerPlayer player = (ServerPlayer)context.player();
            BonfireHandler handler = BonfireHandler.getServerHandler(Objects.requireNonNull(player.getServer()));

            if (BonfiresConfig.Common.bonfireDiscoveryMode) {
                PacketHandler.sendTo(new SendBonfiresToClient(player), player);
            } else {
                PacketHandler.sendTo(new SendBonfiresToClient(), player);
            }
        }
    }
}