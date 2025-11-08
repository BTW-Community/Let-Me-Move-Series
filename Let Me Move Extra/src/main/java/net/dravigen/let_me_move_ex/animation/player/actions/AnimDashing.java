package net.dravigen.let_me_move_ex.animation.player.actions;

import net.dravigen.dr_api_gen.interfaces.ICustomMovementEntity;
import net.dravigen.dr_api_gen.utils.AnimationUtils;
import net.dravigen.dr_api_gen.utils.ModelPartHolder;
import net.dravigen.let_me_move.animation.player.poses.AnimCommon;
import net.minecraft.src.*;

import static net.dravigen.dr_api_gen.utils.GeneralUtils.*;

public class AnimDashing extends AnimCommon {
	public static final ResourceLocation id = new ResourceLocation("LMM", "dashing");
	private static int pressTime = 0;
	
	public AnimDashing() {
		super(id, 1.8f, 1, false, 60, 5, true, 0);
	}
	
	@Override
	public boolean isGeneralConditonsMet(EntityPlayer player, AxisAlignedBB axisAlignedBB) {
		return !player.isEating() && player.moveForward == 0 && player.onGround && !player.doesStatusPreventSprinting();
	}
	
	@Override
	public boolean isActivationConditonsMet(EntityPlayer player, AxisAlignedBB axisAlignedBB) {
		if (player.isUsingSpecialKey() || player.moveStrafing != 0) {
			if (pressTime < 5 && player.isUsingSpecialKey() && player.moveStrafing != 0) {
				
				return true;
			}
			
			pressTime++;
		}
		else {
			pressTime = 0;
		}
		
		return false;
	}
	
	@Override
	public void renderAnimation(ModelBiped model, EntityLivingBase entity, float f, float g, float h, float i, float j,
			float u, float delta) {
		ICustomMovementEntity customEntity = (ICustomMovementEntity) entity;
		
		ModelPartHolder partHolder = customEntity.lmm_$getParHolder();
		//partHolder.resetAnimationRotationPoints();
		
		float side = customEntity.lmm_$getSideValue();
		
		i = clampedI(i);
		
		entity.renderYawOffset = entity.rotationYawHead + 45 * side;
		
		float[] body = new float[]{0, 0, 0, 0, 12, 0};
		float[] head = new float[]{j * (pi / 180.0f), i * (pi / 180.0f), 0, 0, 0, 0};
		float[] rArm = new float[]{pi * 0.5f, side, side * pi * 0.25f, -5, 2, 0};
		float[] lArm = new float[]{pi * 0.5f, side, side * pi * 0.25f, 5, 2, 0};
		float[] rLeg = new float[]{pi * 0.25f, 0, side * -pi * 0.125f, -1.9f, 12, 0.1f};
		float[] lLeg = new float[]{pi * 0.25f, 0, side * -pi * 0.125f, 1.9f, 12, 0.1f};
		
		AnimationUtils.smoothRotateAll(partHolder.getBody(), body, 0.3f * delta, 0.7f * delta);
		AnimationUtils.smoothRotateAll(partHolder.getHead(), head, 0.8f * delta, 0.7f * delta);
		AnimationUtils.smoothRotateAll(partHolder.getrArm(), rArm, 0.3f * delta, 0.7f * delta);
		AnimationUtils.smoothRotateAll(partHolder.getlArm(), lArm, 0.3f * delta, 0.7f * delta);
		AnimationUtils.smoothRotateAll(partHolder.getrLeg(), rLeg, 0.3f * delta, 0.7f * delta);
		AnimationUtils.smoothRotateAll(partHolder.getlLeg(), lLeg, 0.3f * delta, 0.7f * delta);
	}
	
	@Override
	public boolean getHungerCost(EntityPlayer player, double distX, double distY, double distZ) {
		int total = Math.round(MathHelper.sqrt_double(distX * distX + distZ * distZ) * 100.0f);
		
		if (total > 0) {
			player.addStat(StatList.distanceWalkedStat, total);
			float par2 = 1.75f * (float) total * 0.001f * getHungerDifficultyMultiplier(player);
			player.addExhaustion(par2);
			
		}
		
		return true;
	}
	
	@Override
	public boolean getCustomMove(EntityPlayer player) {
		if (((ICustomMovementEntity)player).lmm_$getTimeRendered() == 0) {
			movedOnce = false;
		}
		
		if (!movedOnce) {
			float var1 = player.moveStrafing * 8;
			float var4 = var1 * var1;
			
			if (var4 >= 1.0E-4f) {
				if ((var4 = MathHelper.sqrt_float(var4)) < 1.0f) {
					var4 = 1.0f;
				}
				
				var4 = 1 / var4;
				float var5 = MathHelper.sin(player.rotationYaw * (float) Math.PI / 180.0f);
				float var6 = MathHelper.cos(player.rotationYaw * (float) Math.PI / 180.0f);
				
				
				float modifier = player.getSpeedModifier() * player.getMovementSpeedModifierFromEffects();
				player.motionX = (var1 *= var4) * var6 * modifier;
				player.motionZ = var1 * var5 * modifier;
			}
			
			player.moveEntity(player.motionX, player.motionY, player.motionZ);
			
			player.motionY *= 0.8f;
			
			movedOnce = true;
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public void updateAnimationTime(ResourceLocation currentAnimation, EntityLivingBase player) {
		if (player.isEating()) {
			ICustomMovementEntity customPlayer = (ICustomMovementEntity) player;
			customPlayer.lmm_$setTimeRendered(this.totalDuration);
			
			return;
		}
		
		super.updateAnimationTime(currentAnimation, player);
	}
}
