package com.cstav.genshinstrument.client.gui.screen.instrument.vintagelyre;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.client.config.ModClientConfigs;
import com.cstav.genshinstrument.client.gui.screen.instrument.partial.InstrumentThemeLoader;
import com.cstav.genshinstrument.client.gui.screen.instrument.partial.notegrid.GridInstrumentScreen;
import com.cstav.genshinstrument.client.gui.screen.options.instrument.VintageLyreOptionsScreen;
import com.cstav.genshinstrument.client.gui.screen.options.instrument.partial.InstrumentOptionsScreen;
import com.cstav.genshinstrument.sound.GISounds;
import com.cstav.genshinstrument.sound.NoteSound;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VintageLyreScreen extends GridInstrumentScreen {
    public static final ResourceLocation INSTRUMENT_ID = new ResourceLocation(GInstrumentMod.MODID, "vintage_lyre");
    public static final String[] NOTE_LAYOUT = {
        "C", "Db", "Eb", "F", "G", "Ab", "Bb",
        "C", "D", "Eb", "F", "G", "A", "Bb",
        "C", "D", "Eb", "F", "G", "A", "Bb"
    };

    @Override
    public ResourceLocation getInstrumentId() {
        return INSTRUMENT_ID;
    }


    @Override
    public NoteSound[] getInitSounds() {
        return GISounds.VINTAGE_LYRE_NOTE_SOUNDS;
    }

    @Override
    public String[] noteLayout() {
        return shouldSoundNormalize()
            ? GridInstrumentScreen.NOTE_LAYOUT
            : NOTE_LAYOUT;
    }

    @Override
    protected InstrumentOptionsScreen initInstrumentOptionsScreen() {
        return new VintageLyreOptionsScreen(this);
    }

    public boolean shouldSoundNormalize() {
        return ModClientConfigs.NORMALIZE_VINTAGE_LYRE.get()
            // To normalize a flattened note, one must go up a note.
            // ...But we're maxed.
            && (getPitch() != NoteSound.MAX_PITCH);
    }


    @Override
    public VintageNoteButton createNote(int row, int column) {
        return new VintageNoteButton(row, column, this);
    }
    
    
    private static final InstrumentThemeLoader THEME_LOADER = new InstrumentThemeLoader(INSTRUMENT_ID);
    @Override
    public InstrumentThemeLoader getThemeLoader() {
        return THEME_LOADER;
    }
}
