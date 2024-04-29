package com.cstav.genshinstrument;

import com.cstav.genshinstrument.attachment.ModAttachments;
import com.cstav.genshinstrument.item.GIItems;
import com.cstav.genshinstrument.networking.GIPacketHandler;
import com.cstav.genshinstrument.sound.GISounds;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main class for the Genshin Instruments mod
 * 
 * @author StavWasPlayZ
 */
@Mod(GInstrumentMod.MODID)
public class GInstrumentMod
{
    public static final String MODID = "genshinstrument";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);


    public GInstrumentMod(IEventBus bus, ModContainer modContainer)
    {
        GIPacketHandler.registerPackets();

        GIItems.register(bus);
        ModAttachments.register(bus);
        // ModBlocks.register(bus);
        // ModBlockEntities.register(bus);

        GISounds.register(bus);
        GICreativeModeTabs.regsiter(bus);
    }
}
