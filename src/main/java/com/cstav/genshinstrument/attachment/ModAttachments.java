package com.cstav.genshinstrument.attachment;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.attachment.instrumentOpen.InstrumentOpen;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModAttachments {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, GInstrumentMod.MODID);
    public static void register(final IEventBus bus) {
        ATTACHMENT_TYPES.register(bus);
    }

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<InstrumentOpen>> INSTRUMENT_OPEN = ATTACHMENT_TYPES.register(
        "instrument_caps", () -> AttachmentType.serializable(InstrumentOpen::new).build()
    );


//    @SubscribeEvent
//    public static void registerCapabilities(final AttachCapabilitiesEvent<Entity> event) {
//        if (event.getObject() instanceof Player) {
//
//            if (!event.getObject().getCapability(InstrumentOpenProvider.INSTRUMENT_OPEN).isPresent())
//                event.addCapability(new ResourceLocation(GInstrumentMod.MODID, "instrument_caps"), new InstrumentOpenProvider());
//
//        }
//    }
    
}
