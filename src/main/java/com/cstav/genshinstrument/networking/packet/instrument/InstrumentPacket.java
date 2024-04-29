package com.cstav.genshinstrument.networking.packet.instrument;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.attachment.instrumentOpen.InstrumentOpen;
import com.cstav.genshinstrument.client.gui.screen.instrument.partial.note.NoteButton;
import com.cstav.genshinstrument.networking.api.NetworkDirection;
import com.cstav.genshinstrument.networking.buttonidentifier.NoteButtonIdentifier;
import com.cstav.genshinstrument.networking.packet.INoteIdentifierSender;
import com.cstav.genshinstrument.sound.NoteSound;
import com.cstav.genshinstrument.util.ServerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;

public class InstrumentPacket implements INoteIdentifierSender {
    public static final NetworkDirection NETWORK_DIRECTION = NetworkDirection.PLAY_TO_SERVER;

    @Override
    public String modid() {
        return GInstrumentMod.MODID;
    }

    /** Optionally pass a position that defers from the player's */
    private final Optional<BlockPos> pos;
    private final NoteSound sound;

    private final int pitch, volume;

    private final ResourceLocation instrumentId;
    private final Optional<NoteButtonIdentifier> noteIdentifier;

    public InstrumentPacket(Optional<BlockPos> pos, NoteSound sound, int pitch, int volume,
            ResourceLocation instrumentId, Optional<NoteButtonIdentifier> noteIdentifier) {
        this.pos = pos;
        this.sound = sound;

        this.pitch = pitch;
        this.volume = volume;

        this.instrumentId = instrumentId;
        this.noteIdentifier = noteIdentifier;
    }
    @OnlyIn(Dist.CLIENT)
    public InstrumentPacket(final NoteButton noteButton) {
        this(Optional.empty(), noteButton.getSound(),
            noteButton.getPitch(), noteButton.instrumentScreen.volume,
            noteButton.instrumentScreen.getInstrumentId(),
            Optional.ofNullable(noteButton.getIdentifier())
        );
    }

    public InstrumentPacket(FriendlyByteBuf buf) {
        pos = buf.readOptional((fbb) -> fbb.readBlockPos());
        sound = NoteSound.readFromNetwork(buf);

        pitch = buf.readInt();
        volume = buf.readInt();

        instrumentId = buf.readResourceLocation();
        noteIdentifier = buf.readOptional(this::readNoteIdentifierFromNetwork);
    }

    @Override
    public void write(final FriendlyByteBuf buf) {
        buf.writeOptional(pos, (fbb, pos) -> buf.writeBlockPos(pos));
        sound.writeToNetwork(buf);

        buf.writeInt(pitch);
        buf.writeInt(volume);

        buf.writeResourceLocation(instrumentId);
        buf.writeOptional(noteIdentifier, (fbb, identifier) -> identifier.writeToNetwork(fbb));
    }



    @Override
    public void handle(final IPayloadContext context) {
        final ServerPlayer player = (ServerPlayer) context.player();
        if (!InstrumentOpen.isOpen(player))
            return;

        sendPlayNotePackets(player);
    }

    protected void sendPlayNotePackets(final ServerPlayer player) {

        ServerUtil.sendPlayNotePackets(player, pos,
            sound, instrumentId, noteIdentifier.orElse(null),
            pitch, volume,
            PlayNotePacket::new
        );
        
    }
    
}