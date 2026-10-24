package net.x7bbbbbbb.bonfires_ex.config;

import io.wispforest.owo.config.Option;
import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.Sync;
import net.x7bbbbbbb.bonfires_ex.BonfiresExtended;

@Modmenu(modId = BonfiresExtended.MOD_ID)
@Config(name = "bonfires_ex_config", wrapperName = "BonfiresExtendedConfig")
public class BonfiresExtendedConfigModel {
    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    public boolean disableBedSpawn = true;
    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    public boolean disableInvalidBonfiresCheck = true;
    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    public int durabilityBoostPercentage = 10;
}
