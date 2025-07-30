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
import com.cstav.genshinstrument.networking.packet.instrument.util.ClientDistExec;
import com.cstav.genshinstrument.util.ServerUtil;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.List;

@EventBusSubscriber(modid = GInstrumentMod.MODID)
public class GIPacketHandler {
    @SuppressWarnings("unchecked")
    private static final List<Class<IModPacket>> ACCEPTABLE_PACKETS_C2S = List.of(new Class[] {
        C2SNoteSoundPacket.class,
        CloseInstrumentPacket.class,
        C2SHeldNoteSoundPacket.class,
        ReqInstrumentOpenStatePacket.class
    });

    @SuppressWarnings("unchecked")
    private static final List<Class<IModPacket>> ACCEPTABLE_PACKETS_S2C = List.of(new Class[] {
        NotifyInstrumentOpenPacket.class,
        S2CNoteSoundPacket.class,
        OpenInstrumentPacket.class,
        S2CHeldNoteSoundPacket.class
    });


    public static final String PROTOCOL_VERSION = "5.0";
    private static final PayloadRegistrar payloadRegistrar = new PayloadRegistrar(PROTOCOL_VERSION);

    @SubscribeEvent
    public static void onPayloadRegistration(final RegisterPayloadHandlersEvent event) {
        ServerUtil.registerC2SPackets(ACCEPTABLE_PACKETS_C2S, payloadRegistrar);
    }


    @OnlyIn(Dist.CLIENT)
    public static void registerClientPackets() {
        ServerUtil.registerS2CPackets(ACCEPTABLE_PACKETS_S2C, ClientDistExec.PACKET_SWITCH, payloadRegistrar);
    }


    public static void sendToServer(final IModPacket packet) {
        PacketDistributor.sendToServer(packet);
    }
    public static void sendToClient(final IModPacket packet, final ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, packet);
    }
}
