package net.x7bbbbbbb.bonfires_ex.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
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
            at = @At(value = "INVOKE", target = "Lwehavecookies56/bonfires/items/HomewardBoneItem;<init>(Lnet/minecraft/item/Item$Settings;)V"),
            index = 0
    )
    private static Item.Settings setMaxDamage(Item.Settings settings) {
        return settings.maxDamage(4);
    }

    @Inject(
            method = "use",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/TypedActionResult;success(Ljava/lang/Object;)Lnet/minecraft/util/TypedActionResult;")
    )
    private void damageOnTeleport(World world, PlayerEntity player, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (!world.isClient) {
            ItemStack stack = player.getStackInHand(hand);
            // Damage the item by 1
            stack.damage(1, (ServerPlayerEntity) player, LivingEntity.getSlotForHand(hand));
        }
    }
}