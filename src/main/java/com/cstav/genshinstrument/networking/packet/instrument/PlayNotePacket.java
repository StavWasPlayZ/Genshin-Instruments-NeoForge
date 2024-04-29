package com.cstav.genshinstrument.networking.packet.instrument;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.networking.api.NetworkDirection;
import com.cstav.genshinstrument.networking.buttonidentifier.NoteButtonIdentifier;
import com.cstav.genshinstrument.networking.packet.INoteIdentifierSender;
import com.cstav.genshinstrument.sound.NoteSound;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;
import java.util.UUID;

public class PlayNotePacket implements INoteIdentifierSender {
    public static final NetworkDirection NETWORK_DIRECTION = NetworkDirection.PLAY_TO_CLIENT;

    @Override
    public String modid() {
        return GInstrumentMod.MODID;
    }

    private final int pitch, volume;

    private final Optional<BlockPos> position;
    private final NoteSound sound;
    private final ResourceLocation instrumentId;
    private final Optional<NoteButtonIdentifier> noteIdentifier;
    
    private final Optional<UUID> playerUUID;

    public PlayNotePacket(Optional<BlockPos> pos, NoteSound sound, int pitch, int volume, ResourceLocation instrumentId,
        Optional<NoteButtonIdentifier> noteIdentifier, Optional<UUID> playerUUID) {

        this.pitch = pitch;
        this.volume = volume;

        this.position = pos;
        this.sound = sound;
        this.instrumentId = instrumentId;
        this.noteIdentifier = noteIdentifier;

        this.playerUUID = playerUUID;
    }
    public PlayNotePacket(FriendlyByteBuf buf) {
        pitch = buf.readInt();
        volume = buf.readInt();

        position = buf.readOptional((fbb) -> fbb.readBlockPos());
        sound = NoteSound.readFromNetwork(buf);
        instrumentId = buf.readResourceLocation();
        noteIdentifier = buf.readOptional(this::readNoteIdentifierFromNetwork);

        playerUUID = buf.readOptional((fbb) -> fbb.readUUID());
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(pitch);
        buf.writeInt(volume);

        buf.writeOptional(position, (fbb, pos) -> buf.writeBlockPos(pos));
        sound.writeToNetwork(buf);
        buf.writeResourceLocation(instrumentId);
        buf.writeOptional(noteIdentifier, (fbb, identifier) -> identifier.writeToNetwork(fbb));

        buf.writeOptional(playerUUID, (fbb, uuid) -> buf.writeUUID(uuid));
    }


    @Override
    public void handle(final IPayloadContext context) {
        sound.play(
            pitch, volume, playerUUID,
            instrumentId, noteIdentifier, position
        );
    }
}