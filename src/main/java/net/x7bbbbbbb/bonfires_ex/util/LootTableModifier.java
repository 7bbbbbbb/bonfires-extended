
package net.x7bbbbbbb.bonfires_ex.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.LootTableLoadEvent;

@EventBusSubscriber(modid = "bonfires_ex")
public class LootTableModifier {

    private static final ResourceKey<LootTable> TARGET_LOOT_TABLE =
            ResourceKey.create(
                    Registries.LOOT_TABLE,
                    ResourceLocation.fromNamespaceAndPath(
                            "bonfires_ex",
                            "chests/bonfire_tower_structure"
                    )
            );

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        if (!TARGET_LOOT_TABLE.location().equals(event.getName())) {
            return;
        }

        addOptionalItem(
                event,
                "vc_gliders",
                "paraglider_wood"
        );

        addOptionalItem(
                event,
                "reliable_gliders",
                "glider"
        );
    }

    private static void addOptionalItem(
            LootTableLoadEvent event,
            String namespace,
            String path
    ) {
        ResourceLocation itemId =
                ResourceLocation.fromNamespaceAndPath(namespace, path);

        if (!BuiltInRegistries.ITEM.containsKey(itemId)) {
            return;
        }

        Item item = BuiltInRegistries.ITEM.get(itemId);

        LootPool pool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(item))
                .when(LootItemRandomChanceCondition.randomChance(0.2F))
                .build();

        event.getTable().addPool(pool);
    }
}
