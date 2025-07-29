package com.cstav.genshinstrument.event;

import com.cstav.genshinstrument.networking.packet.instrument.NoteSoundMetadata;
import com.cstav.genshinstrument.sound.NoteSound;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

/**
 * An event fired when a {@link NoteSound} has been produced.
 * This event is fired on the Forge event bus
 */
public class NoteSoundPlayedEvent extends InstrumentPlayedEvent<NoteSound> {
    public NoteSoundPlayedEvent(Level level, NoteSound sound, NoteSoundMetadata soundMeta) {
        super(level, sound, soundMeta);
    }

    public NoteSoundPlayedEvent(Entity initiator, NoteSound sound, NoteSoundMetadata soundMeta) {
        super(initiator, sound, soundMeta);
    }
}
