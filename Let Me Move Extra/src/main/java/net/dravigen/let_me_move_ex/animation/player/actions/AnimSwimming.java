package net.dravigen.let_me_move_ex.animation.player.actions;

import net.dravigen.dranimation_lib.interfaces.ICustomMovementEntity;
import net.dravigen.dranimation_lib.utils.AnimationUtils;
import net.dravigen.dranimation_lib.utils.ModelPartHolder;
import net.dravigen.let_me_move_ex.LmmEx_Settings;
import net.minecraft.src.*;

import static net.dravigen.dranimation_lib.utils.GeneralUtils.*;
import static net.dravigen.let_me_move_ex.LetMeMoveExAddon.crawl_key;

public class AnimSwimming extends AnimBaseAction {
	public static final ResourceLocation id = new ResourceLocation("LMM", "swimming");
	
	public AnimSwimming() {
		super(id, 0.8f, 0.15f, true);
	}
	
	public AnimSwimming(ResourceLocation id) {
		super(id, 0.8f, 0.15f, true);
	}
	
	@Override
	public boolean isGeneralConditonsMet(EntityPlayer player, AxisAlignedBB axisAlignedBB) {
		if (!LmmEx_Settings.SHOULD_SWIM.getBool()) {
			return false;
		}
		
		boolean conditionA = isInsideWater(player) && crawl_key.pressed;
		
		boolean conditionB = (isInsideWater(player) && player.getLookVec().yCoord < 0.45 ||
				isHeadInsideWater(player)) &&
				(player.moveForward > 0 || player.moveStrafing != 0) &&
				player.isUsingSpecialKey();
		
		return !player.doesStatusPreventSprinting() &&
				!player.isOnLadder() &&
				!player.capabilities.isFlying &&
				player.canSwim() &&
				(conditionA || conditionB);
	}
	
	@Override
	public boolean isActivationConditonsMet(EntityPlayer player, AxisAlignedBB axisAlignedBB) {
		return crawl_key.pressed || player.isUsingSpecialKey();
	}
	
	@Override
	public void renderAnimation(ModelBiped model, EntityLivingBase entity, float f, float g, float h, float i, float j,
			float u, float delta) {
		ICustomMovementEntity customEntity = (ICustomMovementEntity) entity;
		ModelPartHolder partHolder = customEntity.lmm_$getParHolder();
		
		float leaningPitch = Math.min(1.0F,
									  customEntity.lmm_$getLeaningPitch(Minecraft.getMinecraft()
																				.getTimer().renderPartialTicks));
		
		if (entity.moveForward != 0 || entity.moveStrafing != 0) {
			entity.renderYawOffset = incrementAngleUntilGoal(entity.renderYawOffset,
															 MathHelper.clamp_float(entity.rotationYaw -
																	 90 *
																			 (entity.moveStrafing -
																					 entity.moveStrafing / 2 *
																							 entity.moveForward), -45 + entity.rotationYaw, 45 + entity.rotationYaw),
															 delta * 0.1f);
		}
		
		leaningPitch = entity.inWater ? 1 : leaningPitch;
		f = entity.inWater ? f : f * 2;
		g = entity.inWater ? g : g * 2;
		
		i = clampedI(i);
		
		float[] head = new float[]{
				leaningPitch > 0.0F ? lerpAngle(leaningPitch, partHolder.getHead()[0], -pi / 4) : j * (pi / 180.0f),
				i * (pi / 180.0f) / 3,
				0,
				0,
				0,
				0
		};
		
		float[] body = new float[]{0, 0, 0, 0, 12, 0};
		float[] rArm = new float[]{0, 0, 0, -5, 2, 0};
		float[] lArm = new float[]{0, 0, 0, 5, 2, 0};
		float[] rLeg = new float[]{0, 0, 0, -1.9f, 12, 0.1f};
		float[] lLeg = new float[]{0, 0, 0, 1.9f, 12, 0.1f};
		
		if (entity.inWater) {
			float k = 1.0F;
			
			rArm[0] = MathHelper.cos(f * 0.6662F + pi) * 2.0F * g * 0.5F / k;
			rArm[1] = 0.0F;
			rArm[2] = 0.0F;
			
			lArm[0] = MathHelper.cos(f * 0.6662F) * 2.0F * g * 0.5F / k;
			lArm[1] = 0.0F;
			lArm[2] = 0.0F;
			
			rLeg[0] = MathHelper.cos(f * 0.6662F) * 1.4F * g / k;
			rLeg[1] = 0.0F;
			rLeg[2] = 0.0F;
			
			lLeg[0] = MathHelper.cos(f * 0.6662F + pi) * 1.4F * g / k;
			lLeg[1] = 0.0F;
			lLeg[2] = 0.0F;
			
			rArm[0] = model.heldItemRight != 0 ? rArm[0] * 0.5f - 0.31415927f * (float) model.heldItemRight : rArm[0];
			lArm[0] = model.heldItemLeft != 0 ? lArm[0] * 0.5f - 0.31415927f * (float) model.heldItemLeft : lArm[0];
			
			float l = f % 26.0F;
			
			if (l < 14.0F) {
				rArm[0] = lerpF(leaningPitch, rArm[0], 0.0F);
				rArm[1] = lerpF(leaningPitch, rArm[1], pi);
				rArm[2] = lerpF(leaningPitch, rArm[2], pi - 1.8707964F * method_2807(l) / method_2807(14.0F));
				
				lArm[0] = lerpAngle(leaningPitch, lArm[0], 0.0F);
				lArm[1] = lerpAngle(leaningPitch, lArm[1], pi);
				lArm[2] = lerpAngle(leaningPitch, lArm[2], pi + 1.8707964F * method_2807(l) / method_2807(14.0F));
			}
			else if (l >= 14.0F && l < 22.0F) {
				float o = (l - 14.0F) / 8.0F;
				rArm[0] = lerpF(leaningPitch, rArm[0], (pi / 2) * o);
				rArm[1] = lerpF(leaningPitch, rArm[1], pi);
				rArm[2] = lerpF(leaningPitch, rArm[2], 1.2707963F + 1.8707964F * o);
				
				lArm[0] = lerpAngle(leaningPitch, lArm[0], (pi / 2) * o);
				lArm[1] = lerpAngle(leaningPitch, lArm[1], pi);
				lArm[2] = lerpAngle(leaningPitch, lArm[2], 5.012389F - 1.8707964F * o);
			}
			else if (l >= 22.0F && l < 26.0F) {
				float o = (l - 22.0F) / 4.0F;
				rArm[0] = lerpF(leaningPitch, rArm[0], (pi / 2) - (pi / 2) * o);
				rArm[1] = lerpF(leaningPitch, rArm[1], pi);
				rArm[2] = lerpF(leaningPitch, rArm[2], pi);
				
				lArm[0] = lerpAngle(leaningPitch, lArm[0], (pi / 2) - (pi / 2) * o);
				lArm[1] = lerpAngle(leaningPitch, lArm[1], pi);
				lArm[2] = lerpAngle(leaningPitch, lArm[2], pi);
			}
			
			lLeg[0] = lerpF(leaningPitch, lLeg[0], 0.3F * MathHelper.cos(f * 0.33333334F + pi));
			rLeg[0] = lerpF(leaningPitch, rLeg[0], 0.3F * MathHelper.cos(f * 0.33333334F));
			
			rArm[0] += model.onGround * 2;
			rArm[2] += model.onGround * 2;
		}
		else {
			f *= entity.moveForward < 0 && entity.moveStrafing == 0 ? -1 : 1;
			
			g = Math.min(g * 8, 2.5f);
			
			AnimationUtils.offsetAllRotationPoints(0, 0, 1, head, rArm, lArm, rLeg, lLeg, body);
			
			rArm[0] = (cos(f) + 0.5f) * g * pi(1, 32) + pi(1, 12) - pi;
			lArm[0] = (cos(f + pi) + 0.5f) * g * pi(1, 32) + pi(1, 12) - pi;
			rLeg[0] = (cos(f + pi) + 1f) * g * pi(1, 64) - pi(1, 24);
			lLeg[0] = (cos(f) + 1f) * g * pi(1, 64) - pi(1, 24);
			
			body[0] = -pi(1, 16);
			body[1] = sin(f) * g * pi(1, 24);
			head[1] += -sin(f) * g * pi(1, 64);
			
			head[3] += sin(body[1]) * 4;
			
			rArm[2] = pi(1, 32);
			lArm[2] = -pi(1, 32);
			rLeg[2] = pi(1, 32);
			lLeg[2] = -pi(1, 32);
			
			body[5] += sin(body[0]) * 12;
			
			rArm[4] += cos(f) * g;
			lArm[4] += cos(f + pi) * g;
			
			rLeg[4] += (cos(f + pi) - 0.75f) * g - 2;
			lLeg[4] += (cos(f) - 0.75f) * g - 2;
			
			rArm[5] += Math.max(0, (sin(f)) * g) - 2;
			lArm[5] += Math.max(0, (sin(f + pi)) * g) - 2;
			
			rLeg[5] += Math.max(0, (sin(f + pi)) * g) - 4;
			lLeg[5] += Math.max(0, (sin(f)) * g) - 4;
			
		}
		
		breath(h, head, rArm, lArm, rLeg, lLeg, body);
		
		swingArm(model, body, rArm, lArm, head);
		
		AnimationUtils.smoothRotateAll(partHolder.getHead(), head, 0.3f * delta, 0.7f * delta);
		AnimationUtils.smoothRotateAll(partHolder.getBody(), body, 0.8f * delta, 0.7f * delta);
		AnimationUtils.smoothRotateAll(partHolder.getrArm(), rArm, 0.3f * delta, 0.7f * delta);
		AnimationUtils.smoothRotateAll(partHolder.getlArm(), lArm, 0.3f * delta, 0.7f * delta);
		AnimationUtils.smoothRotateAll(partHolder.getrLeg(), rLeg, 0.3f * delta, 0.7f * delta);
		AnimationUtils.smoothRotateAll(partHolder.getlLeg(), lLeg, 0.3f * delta, 0.7f * delta);
	}
	
	@Override
	public void updateLeaning(EntityLivingBase entity) {
		ICustomMovementEntity customEntity = (ICustomMovementEntity) entity;
		float goal;
		
		if (entity.inWater) {
			goal = (entity.rotationPitch + 90) / 90f;
		}
		else {
			goal = 1;
		}
		
		customEntity.lmm_$setLeaningPitch(goal);
	}
	
	@Override
	public boolean getHungerCost(EntityPlayer player, double distX, double distY, double distZ) {
		if (canFastSwimInWater(player) || player.onGround) {
			int total = Math.round(MathHelper.sqrt_double(distX * distX + distY * distY + distZ * distZ) * 100.0f);
			
			if (total > 0) {
				player.addStat(StatList.distanceDoveStat, total);
				float modifier = isInsideWater(player) ? 1 : 0.75f;
				float par2 = 1.5f * modifier * total * 0.001f * getHungerDifficultyMultiplier(player);
				player.addExhaustion(par2);
			}
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean getCustomMove(EntityPlayer player) {
		if (canFastSwimInWater(player)) {
			boolean b1 = !isHeadInsideWater(player) && isInsideWater(player);
			
			player.motionY = b1 && player.motionY > 0 ? 0 : player.motionY;
			
			if (player.moveForward > 0) {
				Vec3 look = player.getLookVec();
				Vec3 direction = look;
				
				if ((isInsideWater(player) && look.yCoord < 0 && look.yCoord > -0.2) ||
						b1 && look.yCoord > 0 && look.yCoord < 0.45) {
					direction = Vec3.createVectorHelper(look.xCoord, 0, look.zCoord);
				}
				
				float speed = 0.2f * player.getMovementSpeedModifierFromEffects();
				
				player.moveFlying(player.moveStrafing, player.moveForward, speed * 0.4f);
				
				player.moveEntity(player.motionX, direction.yCoord * speed + player.motionY, player.motionZ);
				
				player.motionY *= 0.5f;
				player.motionX *= 0.6f;
				player.motionZ *= 0.6f;
				
				player.prevLimbSwingAmount = player.limbSwingAmount;
				double deltaX = player.posX - player.prevPosX;
				double deltaY = player.posY - player.prevPosY;
				double deltaZ = player.posZ - player.prevPosZ;
				float var12 = MathHelper.sqrt_double(deltaY * deltaY + deltaX * deltaX + deltaZ * deltaZ) * 4.0f;
				
				if (var12 > 1.0f) {
					var12 = 1.0f;
				}
				
				player.limbSwingAmount += (var12 - player.limbSwingAmount) * 0.4f;
				player.limbSwing += player.limbSwingAmount;
				
				return true;
			}
			else {
				player.limbSwingAmount = 0;
			}
			
			player.moveEntity(player.motionX, -0.02, player.motionZ);
			
			player.motionY *= 0.5f;
			player.motionX *= 0.9f;
			player.motionZ *= 0.9f;
			player.motionY -= 0.02;
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean customBodyHeadRotation(EntityLivingBase entity) {
		return entity.moveForward != 0 || entity.moveStrafing != 0;
	}
	
	private boolean canFastSwimInWater(EntityPlayer player) {
		return !player.isEating() && !player.capabilities.isFlying && isInsideWater(player);
	}
}
