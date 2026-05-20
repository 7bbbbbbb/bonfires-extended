package net.x7bbbbbbb.bonfires_ex.util;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class LootTableModifier {
    private static final RegistryKey<LootTable> TARGET_LOOT_TABLE =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("bonfires_ex", "chests/bonfire_tower_structure"));

    public static void addVCGliders() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (source.isBuiltin() && TARGET_LOOT_TABLE.equals(key)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.2F))
                        .with(ItemEntry.builder(Registries.ITEM.get(Identifier.of("vc_gliders", "paraglider_wood"))));
                tableBuilder.pool(poolBuilder);
            }
        });
    }

    public static void addReliableGliders() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (source.isBuiltin() && TARGET_LOOT_TABLE.equals(key)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.2F))
                        .with(ItemEntry.builder(Registries.ITEM.get(Identifier.of("reliable_gliders", "glider"))));
                tableBuilder.pool(poolBuilder);
            }
        });
    }
}
