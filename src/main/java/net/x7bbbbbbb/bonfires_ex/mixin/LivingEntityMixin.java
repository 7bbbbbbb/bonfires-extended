package net.x7bbbbbbb.bonfires_ex.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
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

    @Inject(
            method = "getEquipmentChanges",
            at = @At("RETURN")
    )
    private void bonfires_ex$correctReinforceDamage(CallbackInfoReturnable<Map<EquipmentSlot, ItemStack>> cir) {
        if (cir.getReturnValue() == null) {
            return;
        }

        LivingEntity entity = (LivingEntity) (Object) this;

        ItemStack mainStack = entity.getEquippedStack(EquipmentSlot.MAINHAND);
        ItemStack offStack = entity.getEquippedStack(EquipmentSlot.OFFHAND);

        double mainBonus = bonfires_ex$getReinforceBonus(mainStack);
        double offBonus = bonfires_ex$getReinforceBonus(offStack);
        double maxBonus = Math.max(mainBonus, offBonus);

        EntityAttributeInstance attributeInstance = entity.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        if (attributeInstance != null) {
            attributeInstance.removeModifier(Bonfires.reinforceDamageModifier);

            if (maxBonus > 0) {
                EntityAttributeModifier modifier = new EntityAttributeModifier(
                        Bonfires.reinforceDamageModifier,
                        maxBonus,
                        EntityAttributeModifier.Operation.ADD_VALUE
                );
                attributeInstance.addTemporaryModifier(modifier);
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
}