package com.cstav.genshinstrument.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;

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
