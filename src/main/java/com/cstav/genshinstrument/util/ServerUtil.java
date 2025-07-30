package com.cstav.genshinstrument.util;

import com.cstav.genshinstrument.attachment.instrumentopen.InstrumentOpenProvider;
import com.cstav.genshinstrument.networking.GIPacketHandler;
import com.cstav.genshinstrument.networking.IModPacket;
import com.cstav.genshinstrument.networking.packet.instrument.s2c.NotifyInstrumentOpenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ServerUtil {

    public static void registerC2SPackets(List<Class<IModPacket>> packetTypes, PayloadRegistrar payloadRegistrar) {
        for (final Class<IModPacket> packetClass : packetTypes) {
            payloadRegistrar.playToServer(
                IModPacket.type(packetClass),
                IModPacket.codec(packetClass),

                (packet, context) ->
                    context.enqueueWork(() -> packet.handleServer(context))
            );
        }
    }

    public static void registerS2CPackets(
        final List<Class<IModPacket>> packetTypes,
        Supplier<Map<String, BiConsumer<? extends IModPacket, IPayloadContext>>> packetSwitchSupplier,
        PayloadRegistrar payloadRegistrar
    ) {
        for (final Class<IModPacket> packetClass : packetTypes) {
            payloadRegistrar.playToClient(
                IModPacket.type(packetClass),
                IModPacket.codec(packetClass),

                (packet, context) ->
                    executeClientPacketHandler(packet, context, packetSwitchSupplier)
            );
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SuppressWarnings("unchecked")
    private static void executeClientPacketHandler(
        final IModPacket packet, final IPayloadContext context,
        Supplier<Map<String, BiConsumer<? extends IModPacket, IPayloadContext>>> packetSwitchSupplier
    ) {
        final BiConsumer<? extends IModPacket, IPayloadContext> packetHandler =
            packetSwitchSupplier.get().get(packet.type().id().getPath());

        // It HAS to be extending IModPacket.
        // It's an abstract class.
        ((BiConsumer<IModPacket, IPayloadContext>) packetHandler)
            .accept(packet, context);
    }


    public static <T extends IModPacket> Entry<String, BiConsumer<T, IPayloadContext>> switchEntry(
        BiConsumer<T, IPayloadContext> handler,
        final Class<T> packetType
    ) {
        return Map.entry(IModPacket.path(packetType), handler);
    }



    public static void notifyOpenStateToPlayers(final ServerPlayer target) {
        final Level level = target.level();

        level.players().forEach((player) -> {
            if (player.equals(target))
                return;

            if (InstrumentOpenProvider.isOpen(player))
                notifyOpenStateToPlayer(player, target);
        });
    }

    public static void notifyOpenStateToPlayer(final Player player, final ServerPlayer target) {
        final NotifyInstrumentOpenPacket packet;

        if (InstrumentOpenProvider.isItem(player)) {
            packet = new NotifyInstrumentOpenPacket(
                player.getUUID(),
                InstrumentOpenProvider.getHand(player)
            );
        } else {
            packet = new NotifyInstrumentOpenPacket(
                player.getUUID(),
                InstrumentOpenProvider.getBlockPos(player)
            );
        }

        GIPacketHandler.sendToClient(packet, target);
    }

}
