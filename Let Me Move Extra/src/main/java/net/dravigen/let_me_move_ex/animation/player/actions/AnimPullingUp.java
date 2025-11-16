package net.dravigen.let_me_move_ex.animation.player.actions;

import net.dravigen.dranimation_lib.interfaces.ICustomMovementEntity;
import net.dravigen.dranimation_lib.utils.AnimationUtils;
import net.dravigen.dranimation_lib.utils.ModelPartHolder;
import net.minecraft.src.*;
import net.dravigen.let_me_move.animation.player.poses.AnimCommon;

import static net.dravigen.dranimation_lib.utils.GeneralUtils.*;

public class AnimPullingUp extends AnimCommon {
	public static final ResourceLocation id = new ResourceLocation("LMM", "pullingUp");
	private static double yBlockAboveWall;
	private coords prevSide;
	
	public AnimPullingUp() {
		super(id, 1.4f, 1f, false, 20, 40, false);
	}
	
	@Override
	public boolean shouldActivateAnimation(EntityPlayer player, AxisAlignedBB axisAlignedBB) {
		if (!AnimationUtils.extraIsPresent) return false;
		
		return super.shouldActivateAnimation(player, axisAlignedBB);
	}
	
	@Override
	public boolean isGeneralConditonsMet(EntityPlayer player, AxisAlignedBB axisAlignedBB) {
		double deltaHeight = yBlockAboveWall - player.boundingBox.minY;
		return !player.capabilities.isFlying &&
				!player.isOnLadder() &&
				player.getHeldItem() == null &&
				!player.isEating() &&
				!player.inWater &&
				!player.doesStatusPreventSprinting() &&
				checkIfEntityFacingWall(player) &&
				(yBlockAboveWall = getWallTopYIfEmptySpace(player)) != -1 &&
				!(deltaHeight <= 1.5f && player.onGround) &&
				!(deltaHeight < 1.5f &&
						!player.worldObj.getCollidingBlockBounds(player.boundingBox.copy().offset(0, -1, 0))
								.isEmpty()) &&
				!isEntityHeadInsideBlock(player, deltaHeight + this.height - 1);
	}
	
	@Override
	public boolean isActivationConditonsMet(EntityPlayer player, AxisAlignedBB axisAlignedBB) {
		return player.isSneaking() &&
				player.moveForward > 0 &&
				(getWallTopYIfEmptySpace(player) - player.boundingBox.minY) < 2;
	}
	
	@Override
	public void renderAnimation(ModelBiped model, EntityLivingBase entity, float f, float g, float h, float i, float j,
			float u, float delta) {
		ICustomMovementEntity customEntity = (ICustomMovementEntity) entity;
		int timeRendered = customEntity.lmm_$getTimeRendered();
		
		ModelPartHolder partHolder = customEntity.lmm_$getParHolder();
		//partHolder.resetAnimationRotationPoints();
		
		i = clampedI(i);
		
		float[] head = new float[]{
				j * (pi / 180.0f),
				MathHelper.clamp_float(i * (pi / 180.0f), -pi / 2f, pi / 2f),
				i * (pi / 180.0f) / 6,
				0,
				0,
				0
		};
		
		float[] body = new float[]{0, 0, 0, 0, 12, 0};
		float[] rArm = new float[]{0, 0, 0, -5, 2, 0};
		float[] lArm = new float[]{0, 0, 0, 5, 2, 0};
		float[] rLeg = new float[]{0, 0, 0, -1.9f, 12, 0.1f};
		float[] lLeg = new float[]{0, 0, 0, 1.9f, 12, 0.1f};
		
		AnimationUtils.offsetAllRotationPoints(0, 0, -2, head, rArm, lArm, rLeg, lLeg, body);
		
		rArm[0] = -pi(7, 8) + pi(7, 8) * timeRendered / 35;
		lArm[0] = -pi(7, 8) + pi(7, 8) * timeRendered / 35;
		
		if (entity.moveStrafing != 0) {
			rArm[2] = sin(entity.ticksExisted / 8f) / 2f;
			lArm[2] = sin(-entity.ticksExisted / 8f) / 2f;
			rArm[1] = sin(-entity.ticksExisted / 8f) / 2f;
			lArm[1] = sin(entity.ticksExisted / 8f) / 2f;
		}
		
		if (timeRendered > 20) {
			body[0] = 0.5F;
			
			rArm[0] += 0.4F;
			lArm[0] += 0.4F;
			
			body[4] += 1;
			body[5] = 4.8f;
			
			rArm[4] += 3.2f;
			lArm[4] += 3.2F;
			
			rLeg[4] += 0.2F;
			rLeg[5] = 4.0F;
			
			lLeg[4] += 0.2F;
			lLeg[5] = 4.0F;
			
			head[4] += 4.2f;
		}
		
		rLeg[0] = cos(h / 3) * pi(1, 16);
		lLeg[0] = cos(h / 3 + pi) * pi(1, 16);
		
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
	public void updateAnimationTime(ResourceLocation currentAnimation, EntityLivingBase player) {
		ICustomMovementEntity customPlayer = (ICustomMovementEntity) player;
		
		if (currentAnimation.equals(this.getID())) {
			customPlayer.lmm_$setTimeRendered(MathHelper.floor_double((2 -
					((yBlockAboveWall + 0.1) - player.boundingBox.minY)) * this.totalDuration / 2));
			
			int timeRendered = customPlayer.lmm_$getTimeRendered();
			
			if (player.isEating() ||
					player.onGround ||
					timeRendered < 0 ||
					((EntityPlayer) player).doesStatusPreventSprinting() ||
					!player.isSneaking() ||
					timeRendered < 35 &&
							(getWallSide(player, 0, player.height) == null || getWallTopYIfEmptySpace(player) == -1)) {
				this.startCooldown(customPlayer);
			}
		}
		
		int cooldown = customPlayer.lmm_$getCooldown(id);
		
		if (cooldown > 0) {
			customPlayer.lmm_$setCooldown(cooldown - 1, id);
		}
	}
	
	@Override
	public boolean getHungerCost(EntityPlayer player, double distX, double distY, double distZ) {
		player.addExhaustion(1.33f / 100f * getHungerDifficultyMultiplier(player));
		
		return true;
	}
	
	@Override
	public boolean getCustomMove(EntityPlayer player) {
		coords side = getWallSide(player, 0, player.height);
		ICustomMovementEntity customPlayer = (ICustomMovementEntity) player;
		int timeRendered = customPlayer.lmm_$getTimeRendered();
		
		if (side != null) {
			prevSide = side;
		}
		
		double x = 0;
		double z = 0;
		
		player.motionY *= 0.8f;
		player.fallDistance = 0;
		x = prevSide == coords.SOUTH ? player.moveStrafing : prevSide == coords.NORTH ? -player.moveStrafing : x;
		z = prevSide == coords.EAST ? -player.moveStrafing : prevSide == coords.WEST ? player.moveStrafing : z;
		
		if (player.moveForward != 0) {
			if (timeRendered == this.totalDuration - 1 && !movedOnce) {
				x = prevSide == coords.EAST ? 10 : prevSide == coords.WEST ? -10 : x;
				z = prevSide == coords.SOUTH ? 10 : prevSide == coords.NORTH ? -10 : z;
				
				this.startCooldown(customPlayer);
				
				movedOnce = true;
			}
			else {
				movedOnce = false;
			}
			
			float var1 = player.moveForward < 0 ? -1 : 1;
			
			player.moveEntity(x / 24, var1 * 1.75d / this.totalDuration, z / 24);
		}
		else {
			player.moveEntity(x / 24, -0.0125, z / 24);
		}
		
		return true;
	}
	
	@Override
	public boolean customBodyHeadRotation(EntityLivingBase entity) {
		return true;
	}
}
