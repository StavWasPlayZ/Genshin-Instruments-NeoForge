package com.cstav.genshinstrument.networking.api;

import com.cstav.genshinstrument.GInstrumentMod;
import com.mojang.logging.LogUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
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
    public static void registerPackets(final RegisterPayloadHandlersEvent event) {
        for (final var entry : MOD_PACKETS_MAP.entrySet()) {
            final String modid = entry.getKey();
            final PayloadRegistrar registrar = event.registrar(modid);

            final ModPacketsContainer packetsContainer = entry.getValue();
            registrar.versioned(packetsContainer.protocolVersion());

            for (final Class<IModPacket> packetType : entry.getValue().packetTypes()) {
                try {
                    final CustomPacketPayload.Type<IModPacket> type = IModPacket.getPacketType(modid, packetType);
                    
                    if (getDirection(packetType) == NetworkDirection.PLAY_TO_CLIENT) {
                        registrar.playToClient(type, getPacketInstantiation(packetType), OOPPacketRegistrar::handlePacket);
                    } else {
                        registrar.playToServer(type, getPacketInstantiation(packetType), OOPPacketRegistrar::handlePacket);
                    }
                } catch (Exception e) {
                    LOGGER.error(
                        "Error registering packet of type "+packetType.getName()
                            +". Make sure to have a constructor taking FriendlyByteBuf & static field of NetworkDirection NETWORK_DIRCTION.",
                        e
                    );
                }
            }
        }
    }

    private static void handlePacket(IModPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> packet.handle(context));
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

    private static StreamCodec<? super RegistryFriendlyByteBuf, IModPacket>getPacketInstantiation(Class<IModPacket> packetType)
            throws NoSuchMethodException {
        final Constructor<IModPacket> packetConstructor = packetType.getDeclaredConstructor(FriendlyByteBuf.class);

        return new StreamCodec<>() {
            @Override
            public IModPacket decode(RegistryFriendlyByteBuf buf) {
                try {
                    return packetConstructor.newInstance(buf);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buf, IModPacket packet) {
                packet.write(buf);
            }
        };
    }

}
