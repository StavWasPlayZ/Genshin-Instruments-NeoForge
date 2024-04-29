package com.cstav.genshinstrument.attachment.instrumentOpen;

import com.cstav.genshinstrument.attachment.ModAttachments;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

public class InstrumentOpen implements INBTSerializable<CompoundTag> {
    public static final String
        OPEN_TAG = "InstrumentOpen",
        IS_ITEM_TAG = "IsItem",
        BLOCK_POS_TAG = "BlockPos",
        HAND_TAG = "InOffhand"
    ;

    private boolean isOpen = false, isItem = false;
    private BlockPos blockPos;
    private InteractionHand hand;

    public static boolean isOpen(final Player player) {
        final InstrumentOpen oIsOpen = player.getData(ModAttachments.INSTRUMENT_OPEN);
        return oIsOpen.isOpen;
    }


    public boolean isOpen() {
        return isOpen;
    }
    public boolean isItem() {
        return isItem;
    }

    /**
     * The position of the instrument block.
     * Present only for when {@link InstrumentOpen#isItem() not an instrument}.
     */
    public BlockPos getBlockPos() {
        return blockPos;
    }
    /**
     * The hand holding the instrument.
     * Present only for when {@link InstrumentOpen#isItem() is an instrument}.
     */
    public InteractionHand getHand() {
        return hand;
    }

    public void setOpen(final InteractionHand hand) {
        isOpen = true;

        this.hand = hand;
        isItem = true;
    }
    public void setOpen(final BlockPos blockPos) {
        isOpen = true;

        this.blockPos = blockPos;
        isItem = false;
    }

    public void setClosed() {
        isOpen = false;
    }
    public void setBlockPos(final BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(Provider provider) {
        final CompoundTag nbt = new CompoundTag();

        nbt.putBoolean(OPEN_TAG, isOpen);
        nbt.putBoolean(IS_ITEM_TAG, isItem);

        if (blockPos != null)
            nbt.put(BLOCK_POS_TAG, NbtUtils.writeBlockPos(blockPos));
        if (hand != null)
            nbt.putBoolean(HAND_TAG, hand == InteractionHand.OFF_HAND);

        return nbt;
    }
    @Override
    public void deserializeNBT(Provider provider, CompoundTag nbt) {
        isOpen = nbt.getBoolean(OPEN_TAG);
        isItem = nbt.getBoolean(IS_ITEM_TAG);

        NbtUtils.readBlockPos(nbt, BLOCK_POS_TAG).ifPresent(this::setBlockPos);

        if (nbt.contains(HAND_TAG, Tag.TAG_BYTE))
            hand = nbt.getBoolean(HAND_TAG) ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
    }
}
