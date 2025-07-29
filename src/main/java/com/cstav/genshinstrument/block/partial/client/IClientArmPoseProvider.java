package com.cstav.genshinstrument.block.partial.client;

import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IClientArmPoseProvider {
    ArmPose getArmPose();
}
