package net.dravigen.let_me_move.animation.player.poses;

import net.dravigen.dr_api_gen.interfaces.ICustomMovementEntity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ResourceLocation;
import net.minecraft.src.StatCollector;

public class AnimStanding extends AnimCommon {
	public static final ResourceLocation id = new ResourceLocation("LMM", "standing");
	
	public AnimStanding() {
		super(id);
	}
	
	@Override
	public String getName(EntityPlayer player) {
		if (((ICustomMovementEntity) player).lmm_$getJumpTime() > 0) {
			return StatCollector.translateToLocal("LMM.animation.jumping");
		}
		
		return super.getName(player);
	}
}
