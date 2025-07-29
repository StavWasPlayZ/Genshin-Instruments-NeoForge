package com.cstav.genshinstrument.client.gui.screen.instrument.ukelele;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public enum Ukulele3rdOctaveType {
    CHORDS("button.genshinstrument.ukulele_3rd_octave.chords"),
    TREBLE("button.genshinstrument.ukulele_3rd_octave.treble");

    public final String key;
    Ukulele3rdOctaveType(final String key) {
        this.key = key;
    }
}
