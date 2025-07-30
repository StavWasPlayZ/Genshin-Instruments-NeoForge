package com.cstav.genshinstrument.networking.packet.instrument.c2s;

import com.cstav.genshinstrument.client.gui.screen.instrument.partial.note.NoteButton;
import com.cstav.genshinstrument.networking.IModPacket;
import com.cstav.genshinstrument.networking.packet.instrument.NoteSoundMetadata;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;

/**
 * A generic instrument note packet,
 * notifying that a specific note should be played in the level
 * @param <T> The sound object type
 */
public abstract class C2SNotePacket<T> extends IModPacket {

    public final T sound;
    public final NoteSoundMetadata meta;


    public C2SNotePacket(T sound, NoteSoundMetadata meta) {
        this.sound = sound;
        this.meta = meta;
    }
    @OnlyIn(Dist.CLIENT)
    public C2SNotePacket(NoteButton noteButton, T sound, int pitch) {
        this(sound, new NoteSoundMetadata(
            noteButton.getSoundSourcePos(),
            pitch, noteButton.instrumentScreen.volume,
            noteButton.instrumentScreen.getInstrumentId(),
            Optional.ofNullable(noteButton.getIdentifier())
        ));
    }


    public C2SNotePacket(FriendlyByteBuf buf) {
        sound = readSound(buf);
        meta = NoteSoundMetadata.read(buf);
    }
    @Override
    public void write(final RegistryFriendlyByteBuf buf) {
        writeSound(buf);
        meta.write(buf);
    }

    protected abstract T readSound(FriendlyByteBuf buf);
    protected abstract void writeSound(FriendlyByteBuf buf);


    @Override
    public void handleServer(final IPayloadContext context) {
        final ServerPlayer player = (ServerPlayer) context.player();
        sendPlayNotePackets(player);
    }
    protected abstract void sendPlayNotePackets(final ServerPlayer player);
}
