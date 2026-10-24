package net.x7bbbbbbb.bonfires_ex.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class PlacementRestrictionLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger("bonfires_ex");
    private static final String FILE_NAME = "bonfires_ex_placement_restrictions.json";

    private static final String DEFAULT_JSON = """
            [
              {
                "block": "bonfires:ash_bone_pile",
                "dimension": "minecraft:the_end",
                "message": "block.bonfires_ex.bonfire_place_fail",
                "max_radius": 150.0
              }
            ]
            """;

    private static final List<PlacementRestriction> RESTRICTIONS = new ArrayList<>();

    private PlacementRestrictionLoader() {}

    public static void init() {
        load();
        // Register this class to NeoForge's EVENT_BUS to listen to PlayerInteractEvent
        NeoForge.EVENT_BUS.register(PlacementRestrictionLoader.class);
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        if (!(event.getLevel() instanceof Level world)) return;
        InteractionHand hand = event.getHand();
        BlockHitResult hitResult = event.getHitVec();

        for (PlacementRestriction restriction : RESTRICTIONS) {
            if (restriction.blockPlacement(player, world, hand, hitResult)) {
                player.displayClientMessage(Component.translatable(restriction.getCustomMessage()), true);
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.FAIL);
                return;
            }
        }
    }

    private static void load() {
        RESTRICTIONS.clear();
        Path file = FMLPaths.CONFIGDIR.get().resolve(FILE_NAME);

        try {
            if (Files.notExists(file)) {
                Files.createDirectories(file.getParent());
                Files.writeString(file, DEFAULT_JSON, StandardCharsets.UTF_8);
            }

            try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                JsonArray array = JsonParser.parseReader(reader).getAsJsonArray();
                for (JsonElement element : array) {
                    parseEntry(element.getAsJsonObject());
                }
            }
        } catch (IOException | RuntimeException e) {
            LOGGER.error("Failed to load {}", file, e);
        }

        LOGGER.info("Loaded {} placement restriction(s)", RESTRICTIONS.size());
    }

    private static void parseEntry(JsonObject obj) {
        ResourceLocation block = ResourceLocation.tryParse(obj.get("block").getAsString());
        ResourceLocation dimension = ResourceLocation.tryParse(obj.get("dimension").getAsString());
        String message = obj.get("message").getAsString();
        double maxRadius = obj.has("max_radius") ? obj.get("max_radius").getAsDouble() : 0;

        if (block == null || dimension == null) {
            LOGGER.warn("Skipping placement restriction with invalid identifier: {}", obj);
            return;
        }
        RESTRICTIONS.add(new PlacementRestriction(block, dimension, message, maxRadius));
    }
}