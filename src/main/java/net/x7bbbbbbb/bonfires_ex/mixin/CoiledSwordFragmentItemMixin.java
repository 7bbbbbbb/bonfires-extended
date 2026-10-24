package net.x7bbbbbbb.bonfires_ex.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wehavecookies56.bonfires.items.CoiledSwordFragmentItem;

@Mixin(CoiledSwordFragmentItem.class)
public class CoiledSwordFragmentItemMixin {

    @ModifyArg(
            method = "<init>",
            at = @At(value = "INVOKE", target = "Lwehavecookies56/bonfires/items/HomewardBoneItem;<init>(Lnet/minecraft/world/item/Item$Properties;)V"),
            index = 0
    )
    private static Item.Properties setMaxDamage(Item.Properties settings) {
        return settings.durability(6);
    }

    @Inject(
            method = "use",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/InteractionResultHolder;success(Ljava/lang/Object;)Lnet/minecraft/world/InteractionResultHolder;")
    )
    private void damageOnTeleport(Level world, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        if (!world.isClientSide()) {
            ItemStack stack = player.getItemInHand(hand);
            // Damage the item by 1 using 1.20.5+ hurtAndBreak method signature
            EquipmentSlot slot = (hand == InteractionHand.MAIN_HAND) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
            stack.hurtAndBreak(1, (ServerPlayer) player, slot);
        }
    }
}