package com.cstav.genshinstrument.client.util;

import com.cstav.genshinstrument.client.config.enumType.SoundType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TogglablePedalSound<T extends SoundType> {

    public final T enabled, disabled;

    public TogglablePedalSound(final T enabled, final T disabled) {
        this.enabled = enabled;
        this.disabled = disabled;
    }

}