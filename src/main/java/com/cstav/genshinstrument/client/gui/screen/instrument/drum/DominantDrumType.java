package com.cstav.genshinstrument.client.gui.screen.instrument.drum;

import com.cstav.genshinstrument.client.gui.screen.options.instrument.midi.DrumMidiOptionsScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public enum DominantDrumType {
    DON, KA, BOTH;

    public String getKey() {
        return (this == BOTH)
            ? (DrumMidiOptionsScreen.DDT_KEY+".both")
            : ((this == KA) ? DrumButtonType.KA : DrumButtonType.DON).getTransKey();
    }
}
