package com.cstav.genshinstrument.networking.buttonidentifier;

import com.cstav.genshinstrument.client.gui.screen.instrument.drum.DrumButtonType;
import com.cstav.genshinstrument.client.gui.screen.instrument.drum.DrumNoteButton;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class DrumNoteIdentifier extends NoteButtonIdentifier {

    public final DrumButtonType noteType;
    public final boolean isRight;

    @OnlyIn(Dist.CLIENT)
    public DrumNoteIdentifier(final DrumNoteButton note) {
        noteType = note.btnType;
        isRight = note.isRight;
    }

    public DrumNoteIdentifier(FriendlyByteBuf buf) {
        noteType = buf.readEnum(DrumButtonType.class);
        isRight = buf.readBoolean();
    }
    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        super.writeToNetwork(buf);
        buf.writeEnum(noteType);
        buf.writeBoolean(isRight);
    }

    @Override
    public boolean matches(NoteButtonIdentifier other) {
        return MatchType.forceMatch(other, this::drumMatch);
    }
    private boolean drumMatch(final DrumNoteIdentifier other) {
        return (noteType == other.noteType) && (isRight == other.isRight);
    }

    
}
