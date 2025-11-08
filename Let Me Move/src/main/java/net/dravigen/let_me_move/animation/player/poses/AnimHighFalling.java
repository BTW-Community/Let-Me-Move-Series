package net.dravigen.let_me_move.animation.player.poses;

import net.dravigen.dr_api_gen.interfaces.ICustomMovementEntity;
import net.dravigen.dr_api_gen.utils.AnimationUtils;
import net.dravigen.dr_api_gen.utils.ModelPartHolder;
import net.minecraft.src.*;

import static net.dravigen.dr_api_gen.utils.GeneralUtils.*;

public class AnimHighFalling extends AnimCommon {
	public final static int minFallHeight = 24;
	public static ResourceLocation id = new ResourceLocation("LMM", "highFalling");
	
	public AnimHighFalling() {
		super(id, 1.8f, 0.005f, true);
	}
	
	@Override
	public boolean isGeneralConditonsMet(EntityPlayer player, AxisAlignedBB axisAlignedBB) {
		return player.fallDistance >= minFallHeight &&
				(!player.isSneaking() || (player.isSneaking() && player.doesStatusPreventSprinting())) &&
				!player.capabilities.isFlying;
	}
	
	@Override
	public void renderAnimation(ModelBiped model, EntityLivingBase entity, float f, float g, float h, float i, float j,
			float u, float delta) {
		ICustomMovementEntity customEntity = (ICustomMovementEntity) entity;
		ModelPartHolder partHolder = customEntity.lmm_$getParHolder();
		
		//partHolder.resetAnimationRotationPoints();
		
		i = clampedI(i);
		
		float leaning = customEntity.lmm_$getLeaningPitch(Minecraft.getMinecraft().getTimer().renderPartialTicks);
		float sin = MathHelper.sin(leaning);
		float cos = MathHelper.cos(leaning);
		float cos1 = MathHelper.cos(leaning + 2);
		float sin1 = MathHelper.sin(leaning + 2);
		
		float[] head = new float[]{
				0.25f, i * (pi / 180.0f), 0,
				0,
				0,
				0
		};
		float[] body = new float[]{0, 0, 0, 0, 12, 0};
		float[] rArm = new float[]{cos, 0, 1.75f + sin, -5, 2, 0};
		float[] lArm = new float[]{cos1, 0, -1.75f - sin1, 5, 2, 0};
		float[] rLeg = new float[]{cos * 1.5f, 0, 0, -1.9f, 12, 0.1f};
		float[] lLeg = new float[]{sin * 1.5f, 0, 0, 1.9f, 12, 0.1f};
		
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
		float goal = (entity.fallDistance - minFallHeight) / 6;
		
		customEntity.lmm_$setLeaningPitch(goal);
	}
}
