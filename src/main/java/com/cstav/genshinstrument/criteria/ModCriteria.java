package com.cstav.genshinstrument.criteria;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.event.InstrumentPlayedEvent;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod.EventBusSubscriber;
import net.neoforged.fml.common.Mod.EventBusSubscriber.Bus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

//NOTE: There to make it load on setup too
@EventBusSubscriber(bus = Bus.FORGE, modid = GInstrumentMod.MODID)
public class ModCriteria {

    private static final DeferredRegister<CriterionTrigger<?>> CRITERION = DeferredRegister.create(BuiltInRegistries.TRIGGER_TYPES, GInstrumentMod.MODID);

    public static final DeferredHolder<CriterionTrigger<?>, InstrumentPlayedTrigger> INSTRUMENT_PLAYED_TRIGGER = CRITERION.register("instrument_played", () -> new InstrumentPlayedTrigger());

    private static <T extends CriterionTrigger<?>> T register(String name, T criterionTrigger) {
        return Registry.register(
            BuiltInRegistries.TRIGGER_TYPES,
            new ResourceLocation(GInstrumentMod.MODID, name),
            criterionTrigger
        );
    }

    @SubscribeEvent
    public static void onInstrumentPlayed(final InstrumentPlayedEvent.ByPlayer event) {
        if (!event.level.isClientSide)
            INSTRUMENT_PLAYED_TRIGGER.get().trigger((ServerPlayer)event.player, new ItemStack(BuiltInRegistries.ITEM.get(event.instrumentId)));
    }
    
}