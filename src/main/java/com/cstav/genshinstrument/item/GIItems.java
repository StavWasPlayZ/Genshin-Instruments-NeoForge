package com.cstav.genshinstrument.item;

import com.cstav.genshinstrument.GInstrumentMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.cstav.genshinstrument.networking.packet.instrument.util.InstrumentPacketUtil.sendOpenPacket;

@EventBusSubscriber(modid = GInstrumentMod.MODID)
public class GIItems {
    
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(GInstrumentMod.MODID);
    public static void register(final IEventBus bus) {
        ITEMS.register(bus);
    }

    public static final DeferredHolder<Item, Item>
        WINDSONG_LYRE = ITEMS.register("windsong_lyre", () ->
            new InstrumentItem(
                (player) -> sendOpenPacket(player, loc("windsong_lyre"))
            )
        ),
        VINTAGE_LYRE = ITEMS.register("vintage_lyre", () ->
            new InstrumentItem(
                (player) -> sendOpenPacket(player, loc("vintage_lyre"))
            )
        ),

        FLORAL_ZITHER = ITEMS.register("floral_zither", () ->
            new InstrumentItem(
                (player) -> sendOpenPacket(player, loc("floral_zither"))
            )
        ),

        GLORIOUS_DRUM = ITEMS.register("glorious_drum", () ->
            new InstrumentItem(
                (player) -> sendOpenPacket(player, loc("glorious_drum"))
            )
        ),

        NIGHTWIND_HORN = ITEMS.register("nightwind_horn", () ->
            new NightwindHornItem(
                (player) -> sendOpenPacket(player, loc("nightwind_horn"))
            )
        ),

        UKULELE = ITEMS.register("ukulele", () ->
            new InstrumentItem(
                (player) -> sendOpenPacket(player, loc("ukulele"))
            )
        ),

        DJEM_DJEM_DRUM = ITEMS.register("djem_djem_drum", () ->
            new InstrumentItem(
                (player) -> sendOpenPacket(player, loc("djem_djem_drum"))
            )
        )
    ;

    private static ResourceLocation loc(final String path) {
        return GInstrumentMod.loc(path);
    }


    @SubscribeEvent
    public static void registerItemsToTab(final BuildCreativeModeTabContentsEvent event) {
        if (!event.getTabKey().equals(CreativeModeTabs.TOOLS_AND_UTILITIES))
            return;

        for (final DeferredHolder<Item, ? extends Item> itemObj : ITEMS.getEntries())
            event.accept(itemObj.get());
    }

}
