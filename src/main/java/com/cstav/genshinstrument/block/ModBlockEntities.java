package com.cstav.genshinstrument.block;

import com.cstav.genshinstrument.GInstrumentMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public abstract class ModBlockEntities {
    
    public static final DeferredRegister<BlockEntityType<?>> BETS =
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, GInstrumentMod.MODID);

    public static void register(final IEventBus bus) {
        BETS.register(bus);
    }

    // public static final DeferredHolder<BlockEntityType<InstrumentBlockEntity>> INSTRUMENT_BE = BETS.register("instrument_be", () -> 
    //     BlockEntityType.Builder.of((pos, state) -> new InstrumentBlockEntity(pos, state), ModBlocks.LYRE_BLOCK.get())
    //         .build(null)
    // );
}
