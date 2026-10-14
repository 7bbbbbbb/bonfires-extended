package net.x7bbbbbbb.bonfires_ex.mixin;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wehavecookies56.bonfires.items.EstusFlaskItem;
import wehavecookies56.bonfires.setup.ComponentSetup;
import wehavecookies56.bonfires.setup.CreativeTabSetup;
import wehavecookies56.bonfires.setup.ItemSetup;

@Mixin(value = CreativeTabSetup.class, remap = false)
public class CreativeTabSetupMixin {

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private static void removeProblematicCreativeTabItems(CallbackInfo ci) {
        ci.cancel();
        ItemGroup cleanTab = FabricItemGroup.builder()
                .displayName(Text.translatable("itemGroup.bonfires"))
                .icon(() -> new ItemStack(ItemSetup.coiled_sword))
                .entries((displayContext, entries) -> {
                    Registries.ITEM.getEntrySet().stream()
                            .filter(entry -> ((RegistryKey<?>) entry.getKey()).getValue().getNamespace().equals("bonfires"))
                            .filter(entry -> entry.getValue() != ItemSetup.estus_flask)
                            .forEach(entry -> entries.add((ItemConvertible) entry.getValue()));
                    for (int i = 3; i < 16; ++i) {
                        ItemStack stack = new ItemStack(ItemSetup.estus_flask);
                        stack.set(ComponentSetup.ESTUS, new EstusFlaskItem.Estus(i, i));
                        entries.add(stack);
                    }
                })
                .build();

        Registry.register(Registries.ITEM_GROUP, Identifier.of("bonfires", "bonfires"), cleanTab);
    }
}