package com.cstav.genshinstrument.networking.packet.instrument.util;

import com.cstav.genshinstrument.attachment.instrumentopen.InstrumentOpenProvider;
import com.cstav.genshinstrument.client.gui.screen.instrument.InstrumentScreenRegistry;
import com.cstav.genshinstrument.event.InstrumentOpenStateChangedEvent;
import com.cstav.genshinstrument.networking.IModPacket;
import com.cstav.genshinstrument.networking.packet.instrument.s2c.NotifyInstrumentOpenPacket;
import com.cstav.genshinstrument.networking.packet.instrument.s2c.OpenInstrumentPacket;
import com.cstav.genshinstrument.networking.packet.instrument.s2c.S2CHeldNoteSoundPacket;
import com.cstav.genshinstrument.networking.packet.instrument.s2c.S2CNoteSoundPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Map;
import java.util.function.BiConsumer;

import static com.cstav.genshinstrument.util.ServerUtil.switchEntry;

@OnlyIn(Dist.CLIENT)
public class ClientDistExec {

    public static final Map<String, BiConsumer<? extends IModPacket, IPayloadContext>> PACKET_SWITCH = Map.ofEntries(
        switchEntry(ClientDistExec::handle, S2CNoteSoundPacket.class),
        switchEntry(ClientDistExec::handle, S2CHeldNoteSoundPacket.class),
        switchEntry(ClientDistExec::handle, OpenInstrumentPacket.class),
        switchEntry(ClientDistExec::handle, NotifyInstrumentOpenPacket.class)
    );


    private static void handle(final S2CNoteSoundPacket packet, final IPayloadContext context) {
        packet.sound.playFromServer(packet.initiatorID, packet.meta);
    }

    private static void handle(final S2CHeldNoteSoundPacket packet, final IPayloadContext context) {
        packet.sound.playFromServer(packet.initiatorID, packet.oInitiatorID, packet.meta, packet.phase);
    }

    public static void handle(final OpenInstrumentPacket packet, final IPayloadContext context) {
        InstrumentScreenRegistry.setScreenByID(packet.instrumentType);
    }

    public static void handle(final NotifyInstrumentOpenPacket packet, final IPayloadContext context) {
        final Player player = Minecraft.getInstance().level.getPlayerByUUID(packet.playerUUID);

        if (player == null)
            return;

        if (packet.isOpen) {

            if (packet.pos.isPresent()) // is block instrument
                InstrumentOpenProvider.setOpen(player, packet.pos.get());
            else // is item instrument
                InstrumentOpenProvider.setOpen(player, packet.hand.get());

        } else {
            InstrumentOpenProvider.setClosed(player);
        }

        NeoForge.EVENT_BUS.post(new InstrumentOpenStateChangedEvent(packet.isOpen, player, packet.pos, packet.hand));
    }

}
