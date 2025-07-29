package com.cstav.genshinstrument.criteria;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.event.InstrumentPlayedEvent;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

//NOTE: There to make it load on setup too
@EventBusSubscriber(modid = GInstrumentMod.MODID)
public class ModCriteria {
    private static final DeferredRegister<CriterionTrigger<?>> CRITERION = DeferredRegister.create(BuiltInRegistries.TRIGGER_TYPES.key(), GInstrumentMod.MODID);
    public static void register(final IEventBus bus) {
        CRITERION.register(bus);
    }

    // It doesn't account for namespaces, so will use genshinstrument_ prefix instead
    public static final DeferredHolder<CriterionTrigger<?>, InstrumentPlayedTrigger> INSTRUMENT_PLAYED_TRIGGER =
        CRITERION.register("instrument_played", InstrumentPlayedTrigger::new);

    @SubscribeEvent
    public static void onInstrumentPlayed(final InstrumentPlayedEvent<?> event) {
        if (event.level().isClientSide)
            return;

        // Only get player events
        if (!event.isByPlayer())
            return;

        final Item instrument = Registries.ITEM.getValue(event.soundMeta().instrumentId());
        // Perhaps troll packets
        if (instrument == null)
            return;

        INSTRUMENT_PLAYED_TRIGGER.get().trigger(
            (ServerPlayer) event.entityInfo().get().entity,
            new ItemStack(instrument)
        );
    }

}