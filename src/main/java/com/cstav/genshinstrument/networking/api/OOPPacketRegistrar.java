package com.cstav.genshinstrument.networking.api;

import com.cstav.genshinstrument.GInstrumentMod;
import com.mojang.logging.LogUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.FriendlyByteBuf.Reader;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod.EventBusSubscriber;
import net.neoforged.fml.common.Mod.EventBusSubscriber.Bus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import org.slf4j.Logger;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;

@EventBusSubscriber(modid = GInstrumentMod.MODID, bus = Bus.MOD)
public class OOPPacketRegistrar {
    private static final Logger LOGGER = LogUtils.getLogger();

    // The below implementation considers EMI as well bc I am too lazy to rewrite that sht again
    private static final HashMap<String, ModPacketsContainer> MOD_PACKETS_MAP = new HashMap<>();

    public static void registerModPackets(String modid, List<Class<IModPacket>> acceptablePackets, String protocolVersion) {
        MOD_PACKETS_MAP.put(modid, new ModPacketsContainer(acceptablePackets, protocolVersion));
    }

    @SubscribeEvent
    public static void registerPackets(final RegisterPayloadHandlerEvent event) {
        for (final var entry : MOD_PACKETS_MAP.entrySet()) {
            final String modid = entry.getKey();
            final IPayloadRegistrar registrar = event.registrar(modid);

            final ModPacketsContainer packetsContainer = entry.getValue();
            registrar.versioned(packetsContainer.protocolVersion());

            for (final Class<IModPacket> packetType : entry.getValue().packetTypes()) {
                try {
                    registrar.play(IModPacket.getPacketId(modid, packetType), getPacketInstantiation(packetType), (handler) -> {
                        try {
                            if (getDirection(packetType) == NetworkDirection.PLAY_TO_CLIENT) {
                                handler.client(OOPPacketRegistrar::handlePacket);
                            } else {
                                handler.server(OOPPacketRegistrar::handlePacket);
                            }
                        } catch (Exception e) {
                            LOGGER.error(
                                "Error registering packet of type "+packetType.getName()
                                    +". Make sure to have static field of NetworkDirection NETWORK_DIRCTION",
                                e
                            );
                        }
                    });
                } catch (Exception e) {
                    LOGGER.error(
                        "Error registering packet of type "+packetType.getName()
                            +". Make sure to have a constructor taking FriendlyByteBuf.",
                        e
                    );
                }
            }
        }
    }

    private static void handlePacket(IModPacket packet, PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> packet.handle(context));
    }

    @SuppressWarnings("unchecked")
    private static <T> T getStaticField(final Class<?> clazz, final String field)
            throws NoSuchFieldException, IllegalAccessException {
        return (T) clazz.getField(field).get(null);
    }

    private static NetworkDirection getDirection(final Class<IModPacket> packetType)
            throws NoSuchFieldException, IllegalAccessException {
        return getStaticField(packetType, "NETWORK_DIRECTION");
    }

    private static Reader<IModPacket> getPacketInstantiation(Class<IModPacket> packetType)
            throws NoSuchMethodException {
        final Constructor<IModPacket> packetConstructor = packetType.getDeclaredConstructor(FriendlyByteBuf.class);

        return (buf) -> {
            try {
                return packetConstructor.newInstance(buf);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

}
