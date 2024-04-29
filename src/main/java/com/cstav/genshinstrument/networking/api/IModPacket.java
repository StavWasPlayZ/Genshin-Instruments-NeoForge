package com.cstav.genshinstrument.networking.api;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface IModPacket extends CustomPacketPayload {
    void handle(final IPayloadContext context);
    String modid();
    
    default void write(final FriendlyByteBuf buf) {}

    @Override
    default Type<? extends CustomPacketPayload> type() {
        return getPacketType(modid(), getClass());
    }

    static CustomPacketPayload.Type<IModPacket> getPacketType(final String modid, final Class<? extends IModPacket> packetType) {
        return new Type<>(
            new ResourceLocation(modid, packetType.getSimpleName().toLowerCase())
        );
    }
}
