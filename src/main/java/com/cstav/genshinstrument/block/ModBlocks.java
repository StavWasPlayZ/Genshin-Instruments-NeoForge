package com.cstav.genshinstrument.block;

import com.cstav.genshinstrument.GInstrumentMod;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.createBlocks(GInstrumentMod.MODID);

    public static void register(final IEventBus bus) {
        BLOCKS.register(bus);
    }

    // //NOTE: For testing purposes
    // public static final DeferredHolder<Block> LYRE_BLOCK = BLOCKS.register("lyre_block", () ->
    //     new LyreInstrumentBlock(Properties.copy(Blocks.OAK_WOOD))
    // );
}
