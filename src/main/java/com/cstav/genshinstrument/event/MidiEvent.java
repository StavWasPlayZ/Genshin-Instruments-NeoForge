package com.cstav.genshinstrument.event;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.Event;

import javax.sound.midi.MidiMessage;

@OnlyIn(Dist.CLIENT)
public class MidiEvent extends Event {

    public final MidiMessage message;
    public final long timeStamp;

    public MidiEvent(final MidiMessage message, final long timeStamp) {
        this.message = message;
        this.timeStamp = timeStamp;
    }
    
}
