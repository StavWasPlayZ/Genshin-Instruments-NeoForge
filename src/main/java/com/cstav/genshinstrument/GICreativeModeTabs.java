package com.cstav.genshinstrument;

import com.cstav.genshinstrument.item.GIItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GICreativeModeTabs {

    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GInstrumentMod.MODID);

    public static void regsiter(final IEventBus bus) {
        TABS.register(bus);
    }

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab>
        INSTRUMENTS_TAB = TABS.register("instruments_tab",
            () -> CreativeModeTab.builder()

                .title(Component.translatable("genshinstrument.itemGroup.instruments"))
                .icon(() -> new ItemStack(GIItems.FLORAL_ZITHER.get()))
                
                .displayItems((displayParams, out) ->
                    GIItems.ITEMS.getEntries().forEach((item) ->
                        out.accept(item.get())
                    )
                )

            .build()
        )
    ;

}
