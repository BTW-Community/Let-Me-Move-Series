package net.dravigen.let_me_move.animation.player.poses;

import net.dravigen.dr_api_gen.interfaces.ICustomMovementEntity;
import net.minecraft.src.*;

public class AnimRunning extends AnimCommon {
	public static final ResourceLocation id = new ResourceLocation("LMM", "running");
	
	public AnimRunning() {
		super(id);
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
		return (player.moveForward != 0 || player.moveStrafing != 0) &&
				player.isSprinting() &&
				!player.isSneaking() &&
				!player.capabilities.isFlying;
	}
	
	@Override
	public boolean customBodyHeadRotation(EntityLivingBase entity) {
		return entity.moveForward != 0 || entity.moveStrafing != 0;
	}
}
