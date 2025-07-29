package com.cstav.genshinstrument.client.midi;

import com.cstav.genshinstrument.sound.NoteSound;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public record MidiOverflowResult(
    NoteSound newNoteSound,
    int pitchOffset,
    int fixedOctaveNote,
    OverflowType type
) {
    public enum OverflowType {TOP, BOTTOM}
}