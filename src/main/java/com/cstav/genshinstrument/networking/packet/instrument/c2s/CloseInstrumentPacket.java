package com.cstav.genshinstrument.networking.packet.instrument.c2s;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.networking.IModPacket;
import com.cstav.genshinstrument.networking.packet.instrument.util.InstrumentPacketUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * A C2S packet for notifying the sever that the
 * client has closed their instrument screen
 */
public class CloseInstrumentPacket extends IModPacket {
    public static final CloseInstrumentPacket INSTANCE = new CloseInstrumentPacket();
    public static final String MOD_ID = GInstrumentMod.MODID;
    public static final StreamCodec<RegistryFriendlyByteBuf, CloseInstrumentPacket> CODEC = StreamCodec.unit(INSTANCE);

    private CloseInstrumentPacket() {}


    @Override
    public void handleServer(final IPayloadContext context) {
        InstrumentPacketUtil.setInstrumentClosed((ServerPlayer) context.player());
    }
    
}