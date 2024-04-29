package com.cstav.genshinstrument.event;

import com.cstav.genshinstrument.attachment.instrumentOpen.InstrumentOpenProvider;
import com.cstav.genshinstrument.block.partial.InstrumentBlockEntity;
import com.cstav.genshinstrument.networking.buttonidentifier.NoteButtonIdentifier;
import com.cstav.genshinstrument.sound.NoteSound;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

import java.util.Optional;

/**
 * An event fired when an instrument sound has been produced.
 * This event is fired on the Forge event bus
 */
public class InstrumentPlayedEvent extends Event implements ICancellableEvent {

    public final NoteSound sound;
    public final int pitch, volume;

    public final Level level;
    
    public final ResourceLocation instrumentId;
    public final Optional<NoteButtonIdentifier> noteIdentifier;
    public final BlockPos playPos;


    /**
     * Convenience method to convert the volume of the note
     * into a {@code float} percentage
     */
    public float volume() {
        return volume / 100f;
    }
    

    public InstrumentPlayedEvent(NoteSound sound, int pitch, int volume, Level level, BlockPos pos,
            ResourceLocation instrumentId, NoteButtonIdentifier noteIdentifier) {

        this.sound = sound;
        this.pitch = pitch;
        this.volume = volume;

        this.level = level;
        this.playPos = pos;

        this.instrumentId = instrumentId;
        this.noteIdentifier = Optional.ofNullable(noteIdentifier);
    }

    public static class ByPlayer extends InstrumentPlayedEvent implements ICancellableEvent {
        public final Player player;

        // The value below will only be supplied if initiated from an instrument
        /** The hand holding the instrument played by this player */
        public final Optional<InteractionHand> hand;


        /**
         * <p>Returns whether this event was fired by an instrument instrument.</p>
         * A {@code false} result does NOT indicate a block instrument.
         * @see ByPlayer#isBlockInstrument
         */
        public boolean isItemInstrument() {
            return hand.isPresent();
        }
        /**
         * <p>Returns whether this event was fired by a block instrument.</p>
         * A {@code false} result does NOT indicate an instrument instrument.
         * @see ByPlayer#isItemInstrument()
         */
        public boolean isBlockInstrument() {
            return !isItemInstrument()
                && player.level().getBlockEntity(playPos) instanceof InstrumentBlockEntity;
        }

        /**
         * @return Whether the played sound was not produced by an instrument
         */
        public boolean isNotInstrument() {
            return !isBlockInstrument() && !isItemInstrument();
        }


        public ByPlayer(NoteSound sound, int pitch, int volume, Player player, BlockPos pos,
                ResourceLocation instrumentId, NoteButtonIdentifier noteIdentifier) {
            super(
                sound, pitch, volume,
                player.level(), pos,
                instrumentId, noteIdentifier
            );

            this.player = player;

            if (InstrumentOpenProvider.isItem(player)) {
                this.hand = Optional.of(InstrumentOpenProvider.getHand(player));
            } else {
                this.hand = Optional.empty();
            }
        }
    }
    
}
