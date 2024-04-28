package com.cstav.genshinstrument.networking.api;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public interface IModPacket extends CustomPacketPayload {
    void handle(final PlayPayloadContext context);
    String modid();

    @Override
    default void write(FriendlyByteBuf pBuffer) {}

    @Override
    default ResourceLocation id() {
        return getPacketId(modid(), getClass());
    }

    static ResourceLocation getPacketId(final String modid, final Class<? extends IModPacket> packetType) {
        return new ResourceLocation(modid, packetType.getSimpleName().toLowerCase());
    }
}
