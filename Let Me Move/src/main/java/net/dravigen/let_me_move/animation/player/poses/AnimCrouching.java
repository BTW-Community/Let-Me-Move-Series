package net.dravigen.let_me_move.animation.player.poses;

import net.dravigen.dr_api_gen.interfaces.ICustomMovementEntity;
import net.minecraft.src.*;

public class AnimCrouching extends AnimCommon {
	public static final ResourceLocation id = new ResourceLocation("LMM", "crouching");
	
	public AnimCrouching() {
		super(id, 1.4f, 0.3f);
	}
	
	@Override
	public String getName(EntityPlayer player) {
		if (((ICustomMovementEntity) player).lmm_$getJumpTime() > 0) {
			return StatCollector.translateToLocal("LMM.animation.jumping");
		}
		
		return super.getName(player);
	}
	
	@Override
	public boolean isGeneralConditonsMet(EntityPlayer player, AxisAlignedBB axisAlignedBB) {
		return true;
	}
	
	@Override
	public boolean isActivationConditonsMet(EntityPlayer player, AxisAlignedBB axisAlignedBB) {
		return player.isSneaking();
	}
	
	@Override
	public boolean customBodyHeadRotation(EntityLivingBase entity) {
		return entity.moveForward != 0 || entity.moveStrafing != 0;
	}
}
