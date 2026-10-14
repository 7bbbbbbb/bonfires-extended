package net.x7bbbbbbb.bonfires_ex;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.x7bbbbbbb.bonfires_ex.config.BonfiresExtendedConfig;
import net.x7bbbbbbb.bonfires_ex.util.LootTableModifier;
import net.x7bbbbbbb.bonfires_ex.util.PlacementRestrictionLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wehavecookies56.bonfires.items.EstusFlaskItem;
import wehavecookies56.bonfires.setup.ComponentSetup;
import wehavecookies56.bonfires.setup.ItemSetup;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class BonfiresExtended implements ModInitializer {
	public static final String MOD_ID = "bonfires_ex";
    public static final BonfiresExtendedConfig CONFIG = BonfiresExtendedConfig.createAndLoad();
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static boolean SURVEYOR_AVAILABLE = false;
    private static final Map<UUID, Integer> refillQueue = new HashMap<>();

    // refill Estus fallback
    private static void refillEstus(ServerPlayerEntity player) {
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.isOf(ItemSetup.estus_flask)) {
                EstusFlaskItem.Estus estus = stack.get(ComponentSetup.ESTUS);
                if (estus != null) {
                    stack.set(ComponentSetup.ESTUS, new EstusFlaskItem.Estus(estus.maxUses(), estus.maxUses()));
                }
            }
        }
    }

	@Override
	public void onInitialize() {
		LOGGER.info("[Bonfires-Ex] Modifying Bonfires mod...");
        PlacementRestrictionLoader.init();
        if (FabricLoader.getInstance().isModLoaded("surveyor")) {
            SURVEYOR_AVAILABLE = true;
        }
        if (FabricLoader.getInstance().isModLoaded("ctov")) {
            FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(container -> {
                ResourceManagerHelper.registerBuiltinResourcePack(
                        Identifier.of(MOD_ID, "bonfires_ex_ctov"),
                        container,
                        ResourcePackActivationType.DEFAULT_ENABLED
                );
            });
        }
        if (FabricLoader.getInstance().isModLoaded("t_and_t")) {
            FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(container -> {
                ResourceManagerHelper.registerBuiltinResourcePack(
                        Identifier.of(MOD_ID, "bonfires_ex_t_and_t"),
                        container,
                        ResourcePackActivationType.DEFAULT_ENABLED
                );
            });
        }
        if (FabricLoader.getInstance().isModLoaded("terralith")) {
            FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(container -> {
                ResourceManagerHelper.registerBuiltinResourcePack(
                        Identifier.of(MOD_ID, "bonfires_ex_terralith"),
                        container,
                        ResourcePackActivationType.DEFAULT_ENABLED
                );
            });
        }
        if (FabricLoader.getInstance().isModLoaded("vc_gliders")) {
            LootTableModifier.addVCGliders();
        }
        if (FabricLoader.getInstance().isModLoaded("reliable_gliders")) {
            LootTableModifier.addReliableGliders();
        }

        // refill Estus fallback
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            if (!alive) {
                refillQueue.put(newPlayer.getUuid(), 5);
            }
        });
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (refillQueue.isEmpty()) return;
            Iterator<Map.Entry<UUID, Integer>> iterator = refillQueue.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<UUID, Integer> entry = iterator.next();
                int remainingTicks = entry.getValue() - 1;
                if (remainingTicks <= 0) {
                    ServerPlayerEntity player = server.getPlayerManager().getPlayer(entry.getKey());
                    if (player != null) {
                        refillEstus(player);
                    }
                    iterator.remove();
                } else {
                    entry.setValue(remainingTicks);
                }
            }
        });
	}
}