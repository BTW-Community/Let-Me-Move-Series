package net.dravigen.let_me_move_ex.animation.player.actions;

import net.dravigen.dranimation_lib.interfaces.ICustomMovementEntity;
import net.dravigen.dranimation_lib.utils.AnimationUtils;
import net.dravigen.dranimation_lib.utils.ModelPartHolder;
import net.dravigen.let_me_move_ex.LmmEx_Settings;
import net.minecraft.src.*;

import static net.dravigen.dranimation_lib.utils.GeneralUtils.checkIfEntityFacingWall;
import static net.dravigen.dranimation_lib.utils.GeneralUtils.pi;
import static net.dravigen.let_me_move_ex.LetMeMoveExAddon.roll_key;

public class AnimRolling extends AnimBaseAction {
	public static final ResourceLocation id = new ResourceLocation("LMM", "rolling");
	
	public AnimRolling() {
		super(id, 0.8f, 1, true, 60, 25, true);
	}
	
	@Override
	public boolean isGeneralConditonsMet(EntityPlayer player, AxisAlignedBB axisAlignedBB) {
		if (!LmmEx_Settings.SHOULD_ROLL.getBool()) {
			return false;
		}
		
		return !player.isEating() &&
				!player.doesStatusPreventSprinting() &&
				!player.inWater &&
				!checkIfEntityFacingWall(player);
	}
	
	@Override
	public boolean isActivationConditonsMet(EntityPlayer player, AxisAlignedBB axisAlignedBB) {
		return roll_key.pressed;
	}
	
	@Override
	public void renderAnimation(ModelBiped model, EntityLivingBase entity, float f, float g, float h, float i, float j,
			float u, float delta) {
		ICustomMovementEntity customEntity = (ICustomMovementEntity) entity;
		ModelPartHolder partHolder = customEntity.lmm_$getParHolder();
		
		//partHolder.resetAnimationRotationPoints();
		
		float[] head = new float[]{0, 0, 0, 0, 0, 0};
		float[] body = new float[]{0, 0, 0, 0, 12, 0};
		float[] rArm = new float[]{0, 0, 0, -5, 2, 0};
		float[] lArm = new float[]{0, 0, 0, 5, 2, 0};
		float[] rLeg = new float[]{0, 0, 0, -1.9f, 12, 0.1f};
		float[] lLeg = new float[]{0, 0, 0, 1.9f, 12, 0.1f};
		
		int t = customEntity.lmm_$getTimeRendered();
		
		if (t < 10) {
			rArm[0] = -pi;
			lArm[0] = -pi;
			rLeg[0] = -pi * 0.25f;
			lLeg[0] = -pi * 0.25f;
			head[0] = pi * 0.25f;
			head[1] = 0;
		}
		else if (t < 20) {
			rArm[0] = -pi * 0.8f;
			lArm[0] = -pi * 0.8f;
			rLeg[0] = -pi * 0.5f;
			lLeg[0] = -pi * 0.5f;
			head[0] = pi * 0.4f;
			head[1] = 0;
		}
		else {
			head[0] = j * (pi / 180.0f);
			head[1] = i * (pi / 180.0f);
		}
		
		this.breath(h, head, rArm, lArm, rLeg, lLeg, body);
		
		this.hurt(h, entity, head, body, rArm, lArm, rLeg, lLeg);
		
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
		
		float goal = (float) customEntity.lmm_$getTimeRendered() / ((this.totalDuration - 5) / 4.5f);
		if (customEntity.lmm_$getTimeRendered() > (this.totalDuration - 5) / 4.5f * 4) goal = 4;
		
		customEntity.lmm_$setLeaningPitch(goal);
	}
	
	@Override
	public boolean getHungerCost(EntityPlayer player, double distX, double distY, double distZ) {
		int var7 = Math.round(MathHelper.sqrt_double(distX * distX + distZ * distZ) * 100.0f);
		
		if (var7 > 0) {
			player.addStat(StatList.distanceWalkedStat, var7);
			float par2 = 1.25f * (float) var7 * 0.001f * getHungerDifficultyMultiplier(player);
			player.addExhaustion(par2);
		}
		
		return true;
	}
	
	@Override
	public boolean getCustomMove(EntityPlayer player) {
		float par1 = player.moveStrafing;
		float par2 = 1.5f * player.getSpeedModifier() * player.getMovementSpeedModifierFromEffects();
		float var3 = 0.91f;
		
		if (player.onGround) {
			var3 = player.getDefaultSlipperinessOnGround();
			int var4 = player.worldObj.getBlockId(MathHelper.floor_double(player.posX),
												  MathHelper.floor_double(player.boundingBox.minY - 0.25),
												  MathHelper.floor_double(player.posZ));
			if (var4 > 0) {
				var3 = player.getSlipperinessRelativeToBlock(var4);
			}
		}
		
		float var8 = 0.16277136f / (var3 * var3 * var3);
		float var5 = player.onGround ? player.getAIMoveSpeed() * var8 : player.jumpMovementFactor;
		player.moveFlying(par1, par2, var5);
		var3 = 0.91f;
		
		if (player.onGround) {
			var3 = player.getDefaultSlipperinessOnGround();
			int var6 = player.worldObj.getBlockId(MathHelper.floor_double(player.posX),
												  MathHelper.floor_double(player.boundingBox.minY - 0.25),
												  MathHelper.floor_double(player.posZ));
			if (var6 > 0) {
				var3 = player.getSlipperinessRelativeToBlock(var6);
			}
		}
		
		player.moveEntity(player.motionX, player.motionY, player.motionZ);
		if (player.isCollidedHorizontally && player.isOnLadder()) {
			player.motionY = 0.2;
		}
		player.motionY = !(!player.worldObj.isRemote ||
				player.worldObj.blockExists((int) player.posX, 0, (int) player.posZ) &&
						player.worldObj.getChunkFromBlockCoords((int) player.posX, (int) player.posZ).isChunkLoaded) ? (
								 player.posY > 0.0
								 ? -0.1
								 : 0.0) : (player.motionY -= 0.08);
		player.motionY *= 0.98f;
		player.motionX *= var3;
		player.motionZ *= var3;
		
		return true;
	}
	
	@Override
	public void updateAnimationTime(ResourceLocation currentAnimation, EntityLivingBase player) {
		if (player.isEating()) {
			ICustomMovementEntity customPlayer = (ICustomMovementEntity) player;
			customPlayer.lmm_$setTimeRendered(this.totalDuration);
			
			return;
		}
		super.updateAnimationTime(currentAnimation, player);
		
		int t = ((ICustomMovementEntity) player).lmm_$getTimeRendered();
		
		if (t < 10) {
			this.height = 1.8f - t / 10f;
		}
		else if (t < 20) {
			if (t > 15) this.height = 0.8f + (t - 15) / 5f;
			else this.height = 0.8f;
			
		}
		else {
			this.height = 1.8f;
		}
	}
}
