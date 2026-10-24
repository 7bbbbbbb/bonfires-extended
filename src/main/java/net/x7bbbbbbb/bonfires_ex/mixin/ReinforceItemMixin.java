package net.x7bbbbbbb.bonfires_ex.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.x7bbbbbbb.bonfires_ex.BonfiresExtended;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import wehavecookies56.bonfires.data.ReinforceHandler;
import wehavecookies56.bonfires.packets.server.ReinforceItem;

@Mixin(ReinforceItem.class)
public class ReinforceItemMixin {

    @WrapOperation(
            method = "handle",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;set(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;"
            )
    )
    private Object nerfDurabilityBoost(ItemStack stack, DataComponentType<?> type, Object value,
                                       Operation<Object> original) {
        if (type == DataComponents.MAX_DAMAGE) {
            Integer baseMax = stack.getItem().components().get(DataComponents.MAX_DAMAGE);
            ReinforceHandler.ReinforceLevel rlevel = ReinforceHandler.getReinforceLevel(stack);

            if (baseMax != null && rlevel != null) {
                int newMax = baseMax + baseMax * rlevel.level() * BonfiresExtended.CONFIG.durabilityBoostPercentage() / 100;
                return original.call(stack, type, (Object) newMax);
            }
        }
        return original.call(stack, type, value);
    }
}