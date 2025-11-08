package net.dravigen.let_me_move_ex.animation.player.actions;

import net.dravigen.dr_api_gen.interfaces.ICustomMovementEntity;
import net.dravigen.dr_api_gen.utils.AnimationUtils;
import net.dravigen.dr_api_gen.utils.ModelPartHolder;
import net.minecraft.src.*;
import net.dravigen.let_me_move.animation.player.poses.AnimCommon;

import static net.dravigen.let_me_move_ex.LetMeMoveExAddon.crawl_key;
import static net.dravigen.dr_api_gen.utils.GeneralUtils.*;

public class AnimSkyDiving extends AnimCommon {
	public static final ResourceLocation id = new ResourceLocation("LMM", "skyDiving");
	
	public AnimSkyDiving() {
		super(id, 1f, 0.2f, true);
	}
	
	@Override
	public boolean isGeneralConditonsMet(EntityPlayer player, AxisAlignedBB axisAlignedBB) {
		return !player.isEating() &&
				player.fallDistance >= 10 &&
				!player.capabilities.isFlying &&
				!player.doesStatusPreventSprinting();
	}
	
	@Override
	public boolean isActivationConditonsMet(EntityPlayer player, AxisAlignedBB axisAlignedBB) {
		return !crawl_key.pressed && player.isSneaking();
	}
	
	@Override
	public void renderAnimation(ModelBiped model, EntityLivingBase entity, float f, float g, float h, float i, float j,
			float u, float delta) {
		ICustomMovementEntity customEntity = (ICustomMovementEntity) entity;
		ModelPartHolder partHolder = customEntity.lmm_$getParHolder();
		
		//partHolder.resetAnimationRotationPoints();
		
		boolean fMove = entity.moveForward > 0;
		
		i = clampedI(i);
		
		float[] head = new float[]{
				-0.5f, i * (pi / 180.0f), 0,
				0,
				0,
				0
		};
		float[] body = new float[]{0, 0, 0, 0, 12, 0};
		float[] rArm = new float[]{0.5f, 0, fMove ? 0.5f : 2, -5, 2, 0};
		float[] lArm = new float[]{0.5f, 0, fMove ? -0.5f : -2, 5, 2, 0};
		float[] rLeg = new float[]{0.5f, 0, 0.15f, -1.9f, 12, 0.1f};
		float[] lLeg = new float[]{0.5f, 0, -0.15f, 1.9f, 12, 0.1f};
		
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
		float goal = entity.moveForward > 0 ? 1.2f : 1;
		
		customEntity.lmm_$setLeaningPitch(goal);
	}
	
	@Override
	public boolean getHungerCost(EntityPlayer player, double distX, double distY, double distZ) {
		int total = Math.round(MathHelper.sqrt_double(distX * distX + distY * distY + distZ * distZ) * 100.0f);
		
		if (total > 0) {
			float par2 = 0.05f * total * 0.001f * getHungerDifficultyMultiplier(player);
			
			if (player.moveForward > 0) {
				par2 *= 1.5f;
				player.addExhaustion(par2);
			}
			else {
				player.addExhaustionWithoutVisualFeedback(par2);
			}
			
			player.addStat(StatList.distanceFallenStat, total);
		}
		
		return true;
	}
}
