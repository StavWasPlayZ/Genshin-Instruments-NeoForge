package com.cstav.genshinstrument.criteria;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.event.InstrumentPlayedEvent;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

//NOTE: There to make it load on setup too
@EventBusSubscriber(bus = Bus.GAME, modid = GInstrumentMod.MODID)
public class ModCriteria {

    private static final DeferredRegister<CriterionTrigger<?>> CRITERION = DeferredRegister.create(BuiltInRegistries.TRIGGER_TYPES, GInstrumentMod.MODID);
    public static void register(final IEventBus bus) {
        CRITERION.register(bus);
    }

    public static final DeferredHolder<CriterionTrigger<?>, InstrumentPlayedTrigger> INSTRUMENT_PLAYED_TRIGGER = CRITERION.register("instrument_played", () -> new InstrumentPlayedTrigger());

    @SubscribeEvent
    public static void onInstrumentPlayed(final InstrumentPlayedEvent.ByPlayer event) {
        if (!event.level.isClientSide)
            INSTRUMENT_PLAYED_TRIGGER.get().trigger((ServerPlayer)event.player, new ItemStack(BuiltInRegistries.ITEM.get(event.instrumentId)));
    }
    
}