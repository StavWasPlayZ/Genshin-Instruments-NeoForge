package com.cstav.genshinstrument.client;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.client.gui.screen.instrument.drum.AratakisGreatAndGloriousDrumScreen;
import com.cstav.genshinstrument.client.gui.screen.instrument.floralzither.FloralZitherScreen;
import com.cstav.genshinstrument.client.gui.screen.instrument.vintagelyre.VintageLyreScreen;
import com.cstav.genshinstrument.client.gui.screen.instrument.windsonglyre.WindsongLyreScreen;
import com.cstav.genshinstrument.client.gui.screen.options.instrument.GridInstrumentOptionsScreen;
import com.cstav.genshinstrument.item.clientExtensions.ModItemPredicates;
import com.cstav.genshinstrument.util.CommonUtil;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod.EventBusSubscriber;
import net.neoforged.fml.common.Mod.EventBusSubscriber.Bus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.ConfigScreenHandler.ConfigScreenFactory;

@EventBusSubscriber(value = Dist.CLIENT, bus = Bus.MOD, modid = GInstrumentMod.MODID)
public class ClientInitiator {

    private static final Class<?>[] LOAD_ME = new Class[] {
        WindsongLyreScreen.class, VintageLyreScreen.class,
        FloralZitherScreen.class, AratakisGreatAndGloriousDrumScreen.class
    };

    @SubscribeEvent
    public static void initClient(final FMLClientSetupEvent event) {
        ModArmPose.load();
        ModItemPredicates.register();

        CommonUtil.loadClasses(LOAD_ME);

        // Register grid options type as the main configs
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenFactory.class,
            () -> new ConfigScreenFactory((minecraft, screen) -> new GridInstrumentOptionsScreen(screen))
        );
    }

}
