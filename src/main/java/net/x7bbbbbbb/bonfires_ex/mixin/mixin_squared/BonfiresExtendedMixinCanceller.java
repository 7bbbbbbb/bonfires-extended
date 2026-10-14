package net.x7bbbbbbb.bonfires_ex.mixin.mixin_squared;

import com.bawnorton.mixinsquared.api.MixinCanceller;
import java.util.List;

public class BonfiresExtendedMixinCanceller implements MixinCanceller {

    private static final List<String> BLOCKED_MIXINS = List.of(
            "wehavecookies56.bonfires.mixins.ItemStackMixin"
    );

    @Override
    public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
        return BLOCKED_MIXINS.contains(mixinClassName);
    }
}