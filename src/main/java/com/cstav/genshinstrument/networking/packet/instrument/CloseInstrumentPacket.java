package com.cstav.genshinstrument.networking.packet.instrument;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.attachment.instrumentOpen.InstrumentOpenProvider;
import com.cstav.genshinstrument.networking.GIPacketHandler;
import com.cstav.genshinstrument.networking.api.IModPacket;
import com.cstav.genshinstrument.networking.api.NetworkDirection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class CloseInstrumentPacket implements IModPacket {
    public static final NetworkDirection NETWORK_DIRECTION = NetworkDirection.PLAY_TO_SERVER;

    @Override
    public String modid() {
        return GInstrumentMod.MODID;
    }


    public CloseInstrumentPacket() {}
    public CloseInstrumentPacket(FriendlyByteBuf buf) {}


    @Override
    public void handle(final PlayPayloadContext context) {
        final Player player = context.player().get();
        InstrumentOpenProvider.setClosed(player);

        for (final Player oPlayer : player.level().players())
            GIPacketHandler.sendToClient(new NotifyInstrumentOpenPacket(player.getUUID()), (ServerPlayer)oPlayer);
    }
    
}