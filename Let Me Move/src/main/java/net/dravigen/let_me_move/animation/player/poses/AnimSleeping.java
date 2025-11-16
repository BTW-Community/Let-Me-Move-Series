package net.dravigen.let_me_move.animation.player.poses;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ResourceLocation;

public class AnimSleeping extends AnimCommon {
	public static final ResourceLocation id = new ResourceLocation("LMM", "sleeping");
	
	public AnimSleeping() {
		super(id, 0.2f);
	}
	
	@Override
	public boolean isGeneralConditonsMet(EntityPlayer player, AxisAlignedBB axisAlignedBB) {
		return player.isPlayerSleeping();
	}
}
