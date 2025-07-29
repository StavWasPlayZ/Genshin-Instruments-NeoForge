package com.cstav.genshinstrument.client;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.client.config.ModClientConfigs;
import com.cstav.genshinstrument.client.gui.screen.instrument.InstrumentScreenRegistry;
import com.cstav.genshinstrument.client.gui.screen.instrument.djemdjemdrum.DjemDjemDrumScreen;
import com.cstav.genshinstrument.client.gui.screen.instrument.floralzither.FloralZitherScreen;
import com.cstav.genshinstrument.client.gui.screen.instrument.gloriousdrum.AratakisGreatAndGloriousDrumScreen;
import com.cstav.genshinstrument.client.gui.screen.instrument.nightwind_horn.NightwindHornScreen;
import com.cstav.genshinstrument.client.gui.screen.instrument.partial.InstrumentScreen;
import com.cstav.genshinstrument.client.gui.screen.instrument.ukelele.UkuleleScreen;
import com.cstav.genshinstrument.client.gui.screen.instrument.vintagelyre.VintageLyreScreen;
import com.cstav.genshinstrument.client.gui.screen.instrument.windsonglyre.WindsongLyreScreen;
import com.cstav.genshinstrument.item.clientExtensions.ModItemPredicates;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.SeparateTransformsModel;

import java.util.Map;
import java.util.function.Supplier;

@EventBusSubscriber(value = Dist.CLIENT, modid = GInstrumentMod.MODID)
public class ClientInitiator {

    private static final Map<ResourceLocation, Supplier<? extends InstrumentScreen>> INSTRUMENTS = Map.of(
        WindsongLyreScreen.INSTRUMENT_ID, WindsongLyreScreen::new,
        VintageLyreScreen.INSTRUMENT_ID, VintageLyreScreen::new,
        FloralZitherScreen.INSTRUMENT_ID, FloralZitherScreen::new,
        AratakisGreatAndGloriousDrumScreen.INSTRUMENT_ID, AratakisGreatAndGloriousDrumScreen::new,
        NightwindHornScreen.INSTRUMENT_ID, NightwindHornScreen::new,

        UkuleleScreen.INSTRUMENT_ID, UkuleleScreen::new,
        DjemDjemDrumScreen.INSTRUMENT_ID, DjemDjemDrumScreen::new
    );

    @SubscribeEvent
    public static void initClient(final FMLClientSetupEvent event) {
        ModArmPose.load();
        ModItemPredicates.register();

        InstrumentScreenRegistry.register(INSTRUMENTS);
    }

    @SubscribeEvent
    public static void registerConfigs(final FMLConstructModEvent event) {
        ModLoadingContext.get().registerConfig(Type.CLIENT, ModClientConfigs.CONFIGS, "instrument_configs.toml");
    }

    @SubscribeEvent
    public static void modelLoadEvent(final ModelEvent.RegisterGeometryLoaders event) {
        event.register( GInstrumentMod.loc("separate_transforms"), SeparateTransformsModel.Loader.INSTANCE);
    }

}
