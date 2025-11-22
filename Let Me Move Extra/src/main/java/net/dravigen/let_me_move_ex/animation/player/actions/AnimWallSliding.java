package net.dravigen.let_me_move_ex.animation.player.actions;

import net.dravigen.dranimation_lib.interfaces.ICustomMovementEntity;
import net.dravigen.dranimation_lib.utils.AnimationUtils;
import net.dravigen.dranimation_lib.utils.ModelPartHolder;
import net.dravigen.let_me_move_ex.LmmEx_Settings;
import net.minecraft.src.*;

import static net.dravigen.dranimation_lib.utils.GeneralUtils.*;

public class AnimWallSliding extends AnimBaseAction {
	public static final ResourceLocation id = new ResourceLocation("LMM", "wallSliding");
	
	public AnimWallSliding() {
		super(id, 1.8f, 1);
	}
	
	@Override
	public boolean isGeneralConditonsMet(EntityPlayer player, AxisAlignedBB axisAlignedBB) {
		if (!LmmEx_Settings.SHOULD_WALL_SLIDE.getBool()) {
			return false;
		}
		
		return !player.isEating() &&
				!player.isOnLadder() &&
				!player.capabilities.isFlying &&
				player.fallDistance > 1.5 &&
				!player.onGround &&
				!player.inWater &&
				getWallSide(player, 0, player.height) != null &&
				player.canJump();
	}
	
	@Override
	public boolean isActivationConditonsMet(EntityPlayer player, AxisAlignedBB axisAlignedBB) {
		return player.isSneaking();
	}
	
	@Override
	public void renderAnimation(ModelBiped model, EntityLivingBase entity, float f, float g, float h, float i, float j,
			float u, float delta) {
		ICustomMovementEntity customEntity = (ICustomMovementEntity) entity;
		ModelPartHolder partHolder = customEntity.lmm_$getParHolder();
		
		float[] head = new float[]{0, 0, 0, 0, 1.5f, 0};
		float[] body = new float[]{pi(1, 8), 0, 0, 0, 12, 0};
		float[] rArm = new float[]{pi(1, 16), 0, pi(1, 6), -5, 2, 0};
		float[] lArm = new float[]{0, 0, -pi(4, 5), 5, 2, 0};
		float[] rLeg = new float[]{pi(1, 7), 0, -pi(1, 10), -1.9f, 12, 0.1f};
		float[] lLeg = new float[]{0, 0, -pi(1, 5), 1.9f, 12, 0.1f};
		
		AnimationUtils.offsetAllRotationPoints(1, 0, 0, head, rArm, lArm, rLeg, lLeg, body);
		
		head[5] -= sin(body[0]) * 12;
		rArm[5] -= sin(body[0]) * 12 - 2;
		lArm[5] -= sin(body[0]) * 12 - 2;
		
		breath(h, head, rArm, lArm, rLeg, lLeg, body);
		
		float onGround = model.onGround;
		if (!(onGround <= 0.0F)) {
			body[1] = MathHelper.sin(MathHelper.sqrt_float(onGround) * (pi * 2)) * 0.05F;
			
			rArm[1] = rArm[1] + body[1];
			
			lArm[1] = lArm[1] + body[1];
			lArm[0] = lArm[0] + body[1];
			
			onGround = 1.0F - model.onGround;
			onGround *= onGround;
			onGround *= onGround;
			onGround = 1.0F - onGround;
			
			float v = MathHelper.sin(onGround * pi);
			float v1 = MathHelper.sin(model.onGround * pi) * -(head[0] - 0.7F) * 0.75F;
			
			rArm[0] = (float) (rArm[0] - (v * 1.2 + v1));
			rArm[1] = rArm[1] + body[1] * 2.0F;
			rArm[2] = rArm[2] + MathHelper.sin(model.onGround * pi) * -0.4F;
		}
		
		AnimationUtils.smoothRotateAll(partHolder.getHead(), head, 0.3f * delta, 0.7f * delta);
		AnimationUtils.smoothRotateAll(partHolder.getBody(), body, 0.8f * delta, 0.7f * delta);
		AnimationUtils.smoothRotateAll(partHolder.getrArm(), rArm, 0.3f * delta, 0.7f * delta);
		AnimationUtils.smoothRotateAll(partHolder.getlArm(), lArm, 0.3f * delta, 0.7f * delta);
		AnimationUtils.smoothRotateAll(partHolder.getrLeg(), rLeg, 0.3f * delta, 0.7f * delta);
		AnimationUtils.smoothRotateAll(partHolder.getlLeg(), lLeg, 0.3f * delta, 0.7f * delta);
	}
	
	@Override
	public boolean getHungerCost(EntityPlayer player, double distX, double distY, double distZ) {
		int total = Math.round(MathHelper.sqrt_double(distY * distY) * 100.0f);
		
		if (total > 0) {
			player.addStat(StatList.distanceFallenStat, total);
			player.addExhaustion(0.75f * total * 0.001f * getHungerDifficultyMultiplier(player));
		}
		
		return true;
	}
	
	@Override
	public boolean customBodyHeadRotation(EntityLivingBase entity) {
		return true;
	}
}
