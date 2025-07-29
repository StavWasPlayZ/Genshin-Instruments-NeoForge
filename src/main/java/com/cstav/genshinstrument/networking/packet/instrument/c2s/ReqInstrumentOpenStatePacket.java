package com.cstav.genshinstrument.networking.packet.instrument.c2s;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.capability.ModCapabilities;
import com.cstav.genshinstrument.networking.IModPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public class ReqInstrumentOpenStatePacket extends IModPacket {
    public static final String MOD_ID = GInstrumentMod.MODID;
    public static final StreamCodec<RegistryFriendlyByteBuf, ReqInstrumentOpenStatePacket> CODEC = CustomPacketPayload.codec(
        ReqInstrumentOpenStatePacket::write,
        ReqInstrumentOpenStatePacket::new
    );


    private final UUID uuid;

    public ReqInstrumentOpenStatePacket(final UUID uuid) {
        this.uuid = uuid;
    }
    public ReqInstrumentOpenStatePacket(FriendlyByteBuf buf) {
        uuid = buf.readUUID();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(uuid);
    }

    @Override
    public void handleServer(final IPayloadContext context) {
        final ServerPlayer player = (ServerPlayer) context.player();
        ModCapabilities.notifyOpenStateToPlayer(player.level().getPlayerByUUID(uuid), player);
    }
}
