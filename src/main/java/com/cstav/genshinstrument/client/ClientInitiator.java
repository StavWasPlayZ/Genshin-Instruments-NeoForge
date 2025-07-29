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
import com.cstav.genshinstrument.client.gui.screen.options.instrument.GridInstrumentOptionsScreen;
import com.cstav.genshinstrument.demos.GenshinInstruments;
import com.cstav.genshinstrument.item.clientExtensions.ModItemPredicates;
import com.cstav.genshinstrument.networking.GIPacketHandler;
import com.cstav.genshinstrument.networking.IModPacket;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.model.SeparateTransformsModel;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.Map;
import java.util.function.Supplier;

@Mod(value = GenshinInstruments.MODID, dist = Dist.CLIENT)
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

    public ClientInitiator(final ModContainer container) {
        container.registerExtensionPoint(
            IConfigScreenFactory.class,
            (container1, screen) -> new GridInstrumentOptionsScreen(screen)
        );

        container.registerConfig(Type.CLIENT, ModClientConfigs.CONFIGS);


        ModArmPose.load();
        ModItemPredicates.register();

        InstrumentScreenRegistry.register(INSTRUMENTS);
    }

    @SubscribeEvent
    public static void onPayloadRegistration(final RegisterPayloadHandlersEvent event) {
        //TODO: Move to ServerUtil

        PayloadRegistrar registrar = event.registrar(GInstrumentMod.MODID)
            .versioned(GIPacketHandler.PROTOCOL_VERSION);

        for (final Class<IModPacket> c2sPacketClass : GIPacketHandler.ACCEPTABLE_PACKETS_C2S) {
            registrar = registrar.playToServer(
                IModPacket.type(c2sPacketClass),
                IModPacket.codec(c2sPacketClass),
                (iModPacket, context) ->
                    context.enqueueWork(() -> iModPacket.handleServer(context))
            );
        }
    }

    @SubscribeEvent
    public static void modelLoadEvent(final ModelEvent.RegisterGeometryLoaders event) {
        event.register(GInstrumentMod.loc("separate_transforms"), SeparateTransformsModel.Loader.INSTANCE);
    }

}
