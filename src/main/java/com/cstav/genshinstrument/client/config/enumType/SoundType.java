package com.cstav.genshinstrument.client.config.enumType;

import com.cstav.genshinstrument.sound.NoteSound;

import java.util.function.Supplier;

public interface SoundType {
    Supplier<NoteSound[]> getSoundArr();
}
