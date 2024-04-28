package com.cstav.genshinstrument.networking;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.networking.api.IModPacket;
import com.cstav.genshinstrument.networking.api.OOPPacketRegistrar;
import com.cstav.genshinstrument.networking.buttonidentifier.DrumNoteIdentifier;
import com.cstav.genshinstrument.networking.buttonidentifier.NoteButtonIdentifier;
import com.cstav.genshinstrument.networking.buttonidentifier.NoteGridButtonIdentifier;
import com.cstav.genshinstrument.networking.packet.instrument.*;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.common.Mod.EventBusSubscriber;
import net.neoforged.fml.common.Mod.EventBusSubscriber.Bus;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class GIPacketHandler {
    @SuppressWarnings("unchecked")
    public static final List<Class<IModPacket>> ACCEPTABLE_PACKETS = List.of(new Class[] {
        InstrumentPacket.class, PlayNotePacket.class, OpenInstrumentPacket.class, CloseInstrumentPacket.class,
        NotifyInstrumentOpenPacket.class
    });

    @SuppressWarnings("unchecked")
    public static final List<Class<? extends NoteButtonIdentifier>> ACCEPTABLE_IDENTIFIERS = List.of(new Class[] {
        NoteButtonIdentifier.class, NoteGridButtonIdentifier.class, DrumNoteIdentifier.class
    });


    private static final String PROTOCOL_VERSION = "5.0";

    public static void registerPackets() {
        OOPPacketRegistrar.registerModPackets(GInstrumentMod.MODID, ACCEPTABLE_PACKETS, PROTOCOL_VERSION);
    }


    public static void sendToServer(final IModPacket packet) {
        PacketDistributor.SERVER.noArg().send(packet);
    }
    public static void sendToClient(final IModPacket packet, final ServerPlayer player) {
        PacketDistributor.PLAYER.with(player).send(packet);
    }
}
