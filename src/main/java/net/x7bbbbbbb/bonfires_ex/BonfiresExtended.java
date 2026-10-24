package net.x7bbbbbbb.bonfires_ex;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.x7bbbbbbb.bonfires_ex.config.BonfiresExtendedConfig;
import net.x7bbbbbbb.bonfires_ex.util.CreativeTabModify;
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

@Mod(BonfiresExtended.MOD_ID)
public class BonfiresExtended {
    public static final String MOD_ID = "bonfires_ex";
    public static final BonfiresExtendedConfig CONFIG = BonfiresExtendedConfig.createAndLoad();
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static boolean SURVEYOR_AVAILABLE = false;
    private static final Map<UUID, Integer> refillQueue = new HashMap<>();

    public BonfiresExtended(IEventBus modEventBus) {
        LOGGER.info("[Bonfires-Ex] Modifying Bonfires mod...");
        PlacementRestrictionLoader.init();

        if (ModList.get().isLoaded("surveyor")) {
            SURVEYOR_AVAILABLE = true;
        }

        // Register mod bus listener for built-in resource pack registration
        modEventBus.addListener(this::registerBuiltinPacks);
        CreativeTabModify.register(modEventBus);

        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.register(LootTableModifier.class);

        // Register game event bus listener for player respawn & server tick
        NeoForge.EVENT_BUS.register(this);
    }

    // Built-in resource pack registration using NeoForge's helper method
    private void registerBuiltinPacks(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.SERVER_DATA) {
            registerPackIfLoaded(
                    event, "ctov", "bonfires_ex_ctov",
                    "CTOV Compat Datapack"
            );

            registerPackIfLoaded(
                    event, "t_and_t", "bonfires_ex_t_and_t",
                    "T and T Compat Datapack"
            );

            registerPackIfLoaded(
                    event, "terralith", "bonfires_ex_terralith",
                    "Terralith Compat Datapack"
            );
        }
    }

    private void registerPackIfLoaded(
            AddPackFindersEvent event,
            String modId,
            String packFolder,
            String displayName
    ) {
        if (ModList.get().isLoaded(modId)) {
            event.addPackFinders(
                    ResourceLocation.fromNamespaceAndPath(
                            MOD_ID, "resourcepacks/" + packFolder
                    ),
                    PackType.SERVER_DATA,
                    Component.literal(displayName),
                    PackSource.BUILT_IN,
                    true,
                    Pack.Position.TOP
            );
        }
    }

    // refill Estus fallback
    private static void refillEstus(ServerPlayer player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(ItemSetup.estus_flask.get())) {
                EstusFlaskItem.Estus estus = stack.get(ComponentSetup.ESTUS.get());
                if (estus != null) {
                    stack.set(ComponentSetup.ESTUS.get(), new EstusFlaskItem.Estus(estus.maxUses(), estus.maxUses()));
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!event.isEndConquered() && event.getEntity() instanceof ServerPlayer player) {
            refillQueue.put(player.getUUID(), 5);
        }
    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        if (refillQueue.isEmpty()) return;

        Iterator<Map.Entry<UUID, Integer>> iterator = refillQueue.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, Integer> entry = iterator.next();
            int remainingTicks = entry.getValue() - 1;
            if (remainingTicks <= 0) {
                ServerPlayer player = event.getServer().getPlayerList().getPlayer(entry.getKey());
                if (player != null) {
                    refillEstus(player);
                }
                iterator.remove();
            } else {
                entry.setValue(remainingTicks);
            }
        }
    }
}