
package net.x7bbbbbbb.bonfires_ex.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import wehavecookies56.bonfires.setup.ComponentSetup;

import java.util.ArrayList;

public final class CreativeTabModify {

    private static final ResourceKey<CreativeModeTab> BONFIRES_TAB =
            ResourceKey.create(
                    Registries.CREATIVE_MODE_TAB,
                    ResourceLocation.fromNamespaceAndPath(
                            "bonfires", "bonfires"
                    )
            );

    private static final ResourceLocation ASH_BONE_PILE =
            ResourceLocation.fromNamespaceAndPath(
                    "bonfires", "ash_bone_pile"
            );

    private CreativeTabModify() {
    }

    public static void register(
            net.neoforged.bus.api.IEventBus modEventBus
    ) {
        modEventBus.addListener(CreativeTabModify::onBuildCreativeTab);
    }

    private static void onBuildCreativeTab(
            BuildCreativeModeTabContentsEvent event
    ) {
        if (!event.getTabKey().equals(BONFIRES_TAB)) {
            return;
        }

        for (ItemStack stack :
                new ArrayList<>(event.getParentEntries())) {
            removeIfProblematic(event, stack);
        }

        for (ItemStack stack :
                new ArrayList<>(event.getSearchEntries())) {
            removeIfProblematic(event, stack);
        }
    }

    private static void removeIfProblematic(
            BuildCreativeModeTabContentsEvent event,
            ItemStack stack
    ) {
        if (stack.isEmpty()) {
            return;
        }

        ResourceLocation itemId =
                BuiltInRegistries.ITEM.getKey(stack.getItem());

        if (ASH_BONE_PILE.equals(itemId)
                && stack.has(ComponentSetup.BONFIRE_DATA)) {
            event.remove(
                    stack,
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            );
        }
    }
}
