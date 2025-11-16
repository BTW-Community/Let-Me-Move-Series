package net.dravigen.let_me_move.animation.player.poses;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ResourceLocation;

public class AnimFloating extends AnimCommon {
	public static final ResourceLocation id = new ResourceLocation("LMM", "floating");
	public AnimFloating() {
		super(id);
	}
	
	@Override
	public boolean isGeneralConditonsMet(EntityPlayer player, AxisAlignedBB axisAlignedBB) {
		return player.inWater && !player.onGround;
	}
}
