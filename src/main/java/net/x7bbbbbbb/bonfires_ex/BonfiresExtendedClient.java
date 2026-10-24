package net.x7bbbbbbb.bonfires_ex;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = BonfiresExtended.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = BonfiresExtended.MOD_ID, value = Dist.CLIENT)
public class BonfiresExtendedClient {
    public BonfiresExtendedClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        BonfiresExtended.LOGGER.info("HELLO FROM CLIENT SETUP");
        BonfiresExtended.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }
}
