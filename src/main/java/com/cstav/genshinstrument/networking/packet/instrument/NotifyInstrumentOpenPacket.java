package com.cstav.genshinstrument.networking.packet.instrument;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.attachment.instrumentOpen.InstrumentOpenProvider;
import com.cstav.genshinstrument.networking.api.IModPacket;
import com.cstav.genshinstrument.networking.api.NetworkDirection;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;
import java.util.UUID;

public class NotifyInstrumentOpenPacket implements IModPacket {
    public static final NetworkDirection NETWORK_DIRECTION = NetworkDirection.PLAY_TO_CLIENT;

    @Override
    public String modid() {
        return GInstrumentMod.MODID;
    }

    private final UUID playerUUID;
    private final boolean isOpen;
    private final Optional<BlockPos> pos;
    private final Optional<InteractionHand> hand;

    /**
     * Constructs packet notifying of a closed instrument
     */
    public NotifyInstrumentOpenPacket(UUID playerUUID) {
        this.playerUUID = playerUUID;

        this.isOpen = false;
        this.pos = Optional.empty();
        this.hand = Optional.empty();
    }
    public NotifyInstrumentOpenPacket(UUID playerUUID, BlockPos pos) {
        this.playerUUID = playerUUID;

        this.isOpen = true;
        this.pos = Optional.of(pos);
        this.hand = Optional.empty();
    }
    public NotifyInstrumentOpenPacket(UUID playerUUID, InteractionHand hand) {
        this.playerUUID = playerUUID;

        this.isOpen = true;
        this.pos = Optional.empty();
        this.hand = Optional.of(hand);
    }
    
    public NotifyInstrumentOpenPacket(final FriendlyByteBuf buf) {
        playerUUID = buf.readUUID();
        isOpen = buf.readBoolean();
        pos = buf.readOptional((fbb) -> fbb.readBlockPos());
        hand = buf.readOptional((fbb) -> fbb.readEnum(InteractionHand.class));
    }
    
    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(playerUUID);
        buf.writeBoolean(isOpen);
        buf.writeOptional(pos, (fbb, pos) -> buf.writeBlockPos(pos));
        buf.writeOptional(hand, FriendlyByteBuf::writeEnum);
    }


    @SuppressWarnings("resource")
    @Override
    public void handle(final IPayloadContext context) {
        final Player player = Minecraft.getInstance().level.getPlayerByUUID(playerUUID);

        if (isOpen) {

            if (pos.isPresent()) // is block instrument
                InstrumentOpenProvider.setOpen(player, pos.get());
            else
                InstrumentOpenProvider.setOpen(player, hand.get());

        } else
            InstrumentOpenProvider.setClosed(player);
    }
    
}
