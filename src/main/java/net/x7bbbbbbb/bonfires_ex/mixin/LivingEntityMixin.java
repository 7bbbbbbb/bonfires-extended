package net.x7bbbbbbb.bonfires_ex.mixin;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.data.ReinforceHandler;

import java.util.Map;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
/*
    @Inject(
            method = "getEquipmentChanges",
            at = @At("RETURN")
    )
    private void bonfires_ex$correctReinforceDamage(CallbackInfoReturnable<Map<EquipmentSlot, ItemStack>> cir) {
        if (cir.getReturnValue() == null) {
            return;
        }

        LivingEntity entity = (LivingEntity) (Object) this;

        ItemStack mainStack = entity.getItemBySlot(EquipmentSlot.MAINHAND);
        ItemStack offStack = entity.getItemBySlot(EquipmentSlot.OFFHAND);

        double mainBonus = bonfires_ex$getReinforceBonus(mainStack);
        double offBonus = bonfires_ex$getReinforceBonus(offStack);
        double maxBonus = Math.max(mainBonus, offBonus);

        AttributeInstance attributeInstance = entity.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attributeInstance != null) {
            attributeInstance.removeModifier(Bonfires.reinforceDamageModifier);

            if (maxBonus > 0) {
                AttributeModifier modifier = new AttributeModifier(
                        Bonfires.reinforceDamageModifier,
                        maxBonus,
                        AttributeModifier.Operation.ADD_VALUE
                );
                attributeInstance.addTransientModifier(modifier);
            }
        }
    }

    @Unique
    private double bonfires_ex$getReinforceBonus(ItemStack stack) {
        if (stack != null && !stack.isEmpty() && ReinforceHandler.canReinforce(stack)) {
            ReinforceHandler.ReinforceLevel rlevel = ReinforceHandler.getReinforceLevel(stack);
            if (rlevel != null && rlevel.level() > 0) {
                return Bonfires.CONFIG.common.reinforceDamagePerLevel() * rlevel.level();
            }
        }
        return 0.0;
    }
 */
}