package com.cstav.genshinstrument.block;

import com.cstav.genshinstrument.GInstrumentMod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(GInstrumentMod.MODID);

    public static void register(final IEventBus bus) {
        BLOCKS.register(bus);
    }

    // //NOTE: For testing purposes
    // public static final RegistryObject<Block> LYRE_BLOCK = BLOCKS.register("lyre_block", () ->
    //     new LyreInstrumentBlock(Properties.copy(Blocks.OAK_WOOD))
    // );
}
