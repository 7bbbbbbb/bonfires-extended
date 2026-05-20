package net.x7bbbbbbb.bonfires_ex.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wehavecookies56.bonfires.client.gui.CreateBonfireScreen;
import wehavecookies56.bonfires.client.gui.widgets.NameTextField;

@Mixin(CreateBonfireScreen.class)
public abstract class CreateBonfireScreenMixin {
    @Shadow(remap = false)
    private NameTextField nameBox;

    @Inject(method = "init", at = @At("TAIL"))
    private void adjustMaxNameLength(CallbackInfo ci) {
        if (this.nameBox != null) {
            this.nameBox.setMaxLength(24);
        }
    }
}