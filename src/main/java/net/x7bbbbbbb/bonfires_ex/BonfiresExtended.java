package net.x7bbbbbbb.bonfires_ex;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.x7bbbbbbb.bonfires_ex.config.BonfiresExtendedConfig;
import net.x7bbbbbbb.bonfires_ex.util.EndIslandPlacementRestriction;
import net.x7bbbbbbb.bonfires_ex.util.LootTableModifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BonfiresExtended implements ModInitializer {
	public static final String MOD_ID = "bonfires_ex";
    public static final BonfiresExtendedConfig CONFIG = BonfiresExtendedConfig.createAndLoad();
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static boolean SURVEYOR_AVAILABLE = false;

	@Override
	public void onInitialize() {
		LOGGER.info("[Bonfires-Ex] Modifying Bonfires mod...");
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
        if (FabricLoader.getInstance().isModLoaded("vc_gliders")) {
            LootTableModifier.addVCGliders();
        }
        if (FabricLoader.getInstance().isModLoaded("reliable_gliders")) {
            LootTableModifier.addReliableGliders();
        }

        EndIslandPlacementRestriction bonfireRestriction = new EndIslandPlacementRestriction(
                Identifier.of("bonfires", "ash_bone_pile"),
                "block.bonfires_ex.bonfire_place_fail",
                150.0
        );
        bonfireRestriction.register();
	}
}