package net.dravigen.let_me_move.animation.player.poses;

import net.dravigen.dr_api_gen.interfaces.ICustomMovementEntity;
import net.dravigen.dr_api_gen.utils.AnimationUtils;
import net.dravigen.dr_api_gen.utils.ModelPartHolder;
import net.minecraft.src.*;

import static net.dravigen.dr_api_gen.utils.GeneralUtils.pi;

public class AnimLowFalling extends AnimCommon {
	public static final ResourceLocation id = new ResourceLocation("LMM", "lowFalling");
	public final static int minFallHeight = 3;
	
	public AnimLowFalling() {
		super(id, 1.8f, 0.02f);
	}
	
	@Override
	public boolean isGeneralConditonsMet(EntityPlayer player, AxisAlignedBB axisAlignedBB) {
		return player.fallDistance >= minFallHeight &&
				player.fallDistance < AnimHighFalling.minFallHeight &&
				!player.isSneaking() &&
				!player.capabilities.isFlying;
	}
	
	@Override
	public void renderAnimation(ModelBiped model, EntityLivingBase entity, float f, float g, float h, float i, float j,
			float u, float delta) {
		ICustomMovementEntity customEntity = (ICustomMovementEntity) entity;
		ModelPartHolder partHolder = customEntity.lmm_$getParHolder();
		
		//partHolder.resetAnimationRotationPoints();
		
		i = clampedI(i);
		
		float v = h % 200 / 1.75f;
		float sin = MathHelper.sin(v);
		float cos = MathHelper.cos(v);
		float cos1 = MathHelper.cos(v + 2);
		float sin1 = MathHelper.sin(v + 2);
		
		float[] head = new float[]{
				j * (pi / 180.0f), i * (pi / 180.0f), 0,
				0,
				0,
				0
		};
		float[] body = new float[]{0, 0, 0, 0, 12, 0};
		float[] rArm = new float[]{cos * 0.65f, 0, 1.75f + sin * 0.65f, -5, 2, 0};
		float[] lArm = new float[]{cos1 * 0.65f, 0, -1.75f - sin1 * 0.65f, 5, 2, 0};
		float[] rLeg = new float[]{-sin * 0.5f, 0, 0, -1.9f, 12, 0.1f};
		float[] lLeg = new float[]{sin * 0.5f, 0, 0, 1.9f, 12, 0.1f};
		
		AnimationUtils.smoothRotateAll(partHolder.getHead(), head, 0.3f * delta, 0.7f * delta);
		AnimationUtils.smoothRotateAll(partHolder.getBody(), body, 0.8f * delta, 0.7f * delta);
		AnimationUtils.smoothRotateAll(partHolder.getrArm(), rArm, 0.3f * delta, 0.7f * delta);
		AnimationUtils.smoothRotateAll(partHolder.getlArm(), lArm, 0.3f * delta, 0.7f * delta);
		AnimationUtils.smoothRotateAll(partHolder.getrLeg(), rLeg, 0.3f * delta, 0.7f * delta);
		AnimationUtils.smoothRotateAll(partHolder.getlLeg(), lLeg, 0.3f * delta, 0.7f * delta);
	}
}
