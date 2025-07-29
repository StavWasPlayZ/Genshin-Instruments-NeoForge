package com.cstav.genshinstrument.client.util;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.client.gui.screen.options.instrument.partial.SoundTypeOptionsScreen;
import com.cstav.genshinstrument.client.keyMaps.InstrumentKeyMappings;
import com.cstav.genshinstrument.event.MidiEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

/**
 * A patch class for all events who previously were annotated with
 * {@literal @}SubscribeEvent, but their parents' annotation collided with
 * {@literal @}EventBusSubscriber and {@literal @}OnlyIn.
 * <p>
 * Necessary since 1.20.6.
 */
@EventBusSubscriber(modid = GInstrumentMod.MODID, value = Dist.CLIENT)
public class ClientEventCaller {

    @SubscribeEvent
    public static void onMidiReceivedEvent(final MidiEvent event) {
        SoundTypeOptionsScreen.onMidiReceivedEvent(event);
    }

    @SubscribeEvent
    public static void registerKeybinds(final RegisterKeyMappingsEvent event) {
        InstrumentKeyMappings.registerKeybinds(event);
    }
    
}
