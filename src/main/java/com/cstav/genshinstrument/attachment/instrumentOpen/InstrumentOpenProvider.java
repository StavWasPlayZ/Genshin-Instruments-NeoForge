package com.cstav.genshinstrument.attachment.instrumentOpen;

import com.cstav.genshinstrument.attachment.ModAttachments;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public class InstrumentOpenProvider {
    

    public static void setOpen(final Player player, final BlockPos pos) {
        setData(player, (data) -> data.setOpen(pos));
    }
    public static void setOpen(Player player, InteractionHand hand) {
        setData(player, (data) -> data.setOpen(hand));
    }
    public static void setClosed(Player player) {
        setData(player, InstrumentOpen::setClosed);
    }

    public static void setBlockPos(Player player, BlockPos blockPos) {
        setData(player, (data) -> data.setBlockPos(blockPos));
    }

    public static boolean isOpen(final Player player) {
        return player.getData(ModAttachments.INSTRUMENT_OPEN).isOpen();
    }
    public static boolean isItem(final Player player) {
        return player.getData(ModAttachments.INSTRUMENT_OPEN).isItem();
    }

    public static BlockPos getBlockPos(final Player player) {
        return player.getData(ModAttachments.INSTRUMENT_OPEN).getBlockPos();
    }
    public static InteractionHand getHand(final Player player) {
        return player.getData(ModAttachments.INSTRUMENT_OPEN).getHand();
    }


    private static void setData(final Player player, final Consumer<InstrumentOpen> dataConsumer) {
        final InstrumentOpen data = player.getData(ModAttachments.INSTRUMENT_OPEN);
        dataConsumer.accept(data);
        player.setData(ModAttachments.INSTRUMENT_OPEN, data);
    }
}
