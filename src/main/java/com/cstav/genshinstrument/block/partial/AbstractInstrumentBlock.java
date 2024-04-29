package com.cstav.genshinstrument.block.partial;

import com.cstav.genshinstrument.attachment.instrumentOpen.InstrumentOpenProvider;
import com.cstav.genshinstrument.block.partial.client.IClientArmPoseProvider;
import com.cstav.genshinstrument.client.ModArmPose;
import com.cstav.genshinstrument.networking.GIPacketHandler;
import com.cstav.genshinstrument.networking.packet.instrument.NotifyInstrumentOpenPacket;
import com.cstav.genshinstrument.util.ServerUtil;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;

import java.util.function.Consumer;

public abstract class AbstractInstrumentBlock extends BaseEntityBlock {

    public AbstractInstrumentBlock(Properties pProperties) {
        super(pProperties);

        if (!FMLEnvironment.dist.isDedicatedServer())
            initClientBlockUseAnim((pose) -> clientBlockArmPose = pose);
    }

    
    // Abstract implementations

    /**
     * A server-side event fired when the player has interacted with the instrument.
     * It should send a packet to the given player for opening this instrument's screen.
     */
    protected abstract void onInstrumentOpen(final ServerPlayer player);
    @Override
    public abstract InstrumentBlockEntity newBlockEntity(BlockPos pPos, BlockState pState);

    
    /**
     * An instance of {@link IClientArmPoseProvider} as defined in {@link AbstractInstrumentBlock#initClientBlockUseAnim}
     */
    private Object clientBlockArmPose;
    protected void initClientBlockUseAnim(final Consumer<ArmPose> consumer) {
        consumer.accept(ModArmPose.PLAYING_BLOCK_INSTRUMENT);
    }

    @OnlyIn(Dist.CLIENT)
    public ArmPose getClientBlockArmPose() {
        return (ArmPose)clientBlockArmPose;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pResult) {
        if (pLevel.isClientSide)
            return InteractionResult.CONSUME;


        final BlockEntity be = pLevel.getBlockEntity(pPos);
        if (!(be instanceof InstrumentBlockEntity))
            return InteractionResult.FAIL;

        if (ServerUtil.sendOpenPacket((ServerPlayer)pPlayer, this::onInstrumentOpen, pPos)) {
            ((InstrumentBlockEntity)be).users.add(pPlayer.getUUID());
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        final BlockEntity be = pLevel.getBlockEntity(pPos);
        if (!(be instanceof InstrumentBlockEntity))
            return;


        final InstrumentBlockEntity ibe = (InstrumentBlockEntity)be;
        
        for (final Player player : pLevel.players()) {
            ibe.users.forEach((user) -> {
                InstrumentOpenProvider.setClosed(pLevel.getPlayerByUUID(user));
                GIPacketHandler.sendToClient(new NotifyInstrumentOpenPacket(user), (ServerPlayer)player);
            });
        }
    }

}
