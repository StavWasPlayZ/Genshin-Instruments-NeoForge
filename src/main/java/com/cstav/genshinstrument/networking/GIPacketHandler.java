package com.cstav.genshinstrument.networking;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.networking.packet.instrument.c2s.C2SHeldNoteSoundPacket;
import com.cstav.genshinstrument.networking.packet.instrument.c2s.C2SNoteSoundPacket;
import com.cstav.genshinstrument.networking.packet.instrument.c2s.CloseInstrumentPacket;
import com.cstav.genshinstrument.networking.packet.instrument.c2s.ReqInstrumentOpenStatePacket;
import com.cstav.genshinstrument.networking.packet.instrument.s2c.NotifyInstrumentOpenPacket;
import com.cstav.genshinstrument.networking.packet.instrument.s2c.OpenInstrumentPacket;
import com.cstav.genshinstrument.networking.packet.instrument.s2c.S2CHeldNoteSoundPacket;
import com.cstav.genshinstrument.networking.packet.instrument.s2c.S2CNoteSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.List;

@EventBusSubscriber(modid = GInstrumentMod.MODID)
public class GIPacketHandler {
    @SuppressWarnings("unchecked")
    public static final List<Class<IModPacket>> ACCEPTABLE_PACKETS_C2S = List.of(new Class[] {
        C2SNoteSoundPacket.class,
        CloseInstrumentPacket.class,
        C2SHeldNoteSoundPacket.class,
        ReqInstrumentOpenStatePacket.class
    });

    @SuppressWarnings("unchecked")
    public static final List<Class<IModPacket>> ACCEPTABLE_PACKETS_S2C = List.of(new Class[] {
        NotifyInstrumentOpenPacket.class,
        S2CNoteSoundPacket.class,
        OpenInstrumentPacket.class,
        S2CHeldNoteSoundPacket.class
    });


    public static final String PROTOCOL_VERSION = "5.0";

    @SubscribeEvent
    public static void onPayloadRegistration(final RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(GInstrumentMod.MODID)
            .versioned(PROTOCOL_VERSION);

        for (final Class<IModPacket> c2sPacketClass : ACCEPTABLE_PACKETS_C2S) {
            registrar = registrar.playToServer(
                IModPacket.type(c2sPacketClass),
                IModPacket.codec(c2sPacketClass),
                (iModPacket, context) ->
                    context.enqueueWork(() -> iModPacket.handleServer(context))
            );
        }
    }


    public static void sendToServer(final IModPacket packet) {
        PacketDistributor.sendToServer(packet);
    }
    public static void sendToClient(final IModPacket packet, final ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, packet);
    }
}
