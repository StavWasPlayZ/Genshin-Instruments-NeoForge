package com.cstav.genshinstrument.event;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.attachment.instrumentOpen.InstrumentOpenProvider;
import com.cstav.genshinstrument.item.InstrumentItem;
import com.cstav.genshinstrument.util.ServerUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.common.Mod.EventBusSubscriber;
import net.neoforged.fml.common.Mod.EventBusSubscriber.Bus;
import net.neoforged.neoforge.event.TickEvent.LevelTickEvent;
import net.neoforged.neoforge.event.TickEvent.Phase;

@EventBusSubscriber(bus = Bus.FORGE, modid = GInstrumentMod.MODID)
public abstract class ServerEvents {
    private static final int MAX_BLOCK_INSTRUMENT_DIST = 6;
    
    @SubscribeEvent
    public static void onServerTick(final LevelTickEvent event) {
        if ((event.phase != Phase.END) && (event.side == LogicalSide.SERVER))
            event.level.players().forEach((player) -> {
                if (shouldAbruptlyClose(player))
                    ServerUtil.setInstrumentClosed(player);
            });
    }

    private static boolean shouldAbruptlyClose(final Player player) {
        if (!InstrumentOpenProvider.isOpen(player))
            return false;

        if (InstrumentOpenProvider.isItem(player)) {
            // Close instrument item if it is no longer in their hands
            final InteractionHand hand = InstrumentOpenProvider.getHand(player);
            if (hand == null)
                return true;

            final ItemStack handItem = player.getItemInHand(hand);
            // This is done like so because there is no event (that I know of) for when an item is moved/removed
            return !(handItem.getItem() instanceof InstrumentItem);
        } else {
            // Close an instrument block if the player is too far away
            return !InstrumentOpenProvider.getBlockPos(player)
                .closerToCenterThan(player.position(), MAX_BLOCK_INSTRUMENT_DIST);
        }
    }

}
