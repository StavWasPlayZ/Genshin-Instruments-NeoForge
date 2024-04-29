package com.cstav.genshinstrument.criteria;

import com.cstav.genshinstrument.networking.packet.instrument.InstrumentPacket;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

/**
 * <p>The class holding the genshinstrument:instrument_played trigger.</p>
 *
 * <p>
 * It is triggered in {@link InstrumentPacket} such that every sound
 * produced by an instrument will trigger this criteria.
 * It will pass the played instrument from within the {@code instrument} JSON instrument object.
 * </p>
 *
 * Internally, used for triggering advancements for the player.
 */
public class InstrumentPlayedTrigger extends SimpleCriterionTrigger<InstrumentPlayedTrigger.TriggerInstance> {

    public void trigger(final ServerPlayer player, final ItemStack instrument) {
        super.trigger(player, (triggerInstance) ->
            triggerInstance.matches(instrument)
        );
    }

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }


    public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> instrument) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create((triggerInstance) ->
            triggerInstance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player")
                    .forGetter(TriggerInstance::player),
                ItemPredicate.CODEC.optionalFieldOf("instrument")
                    .forGetter(TriggerInstance::instrument)
            ).apply(triggerInstance, TriggerInstance::new)
        );

        public boolean matches(final ItemStack instrument) {
            return instrument().isEmpty() || instrument().get().test(instrument);
        }
    }

}
