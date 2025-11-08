package net.dravigen.let_me_move.animation.player.poses;

import net.dravigen.dr_api_gen.interfaces.ICustomMovementEntity;
import net.dravigen.dr_api_gen.utils.AnimationUtils;
import net.dravigen.dr_api_gen.utils.ModelPartHolder;
import net.minecraft.src.*;

import static net.dravigen.dr_api_gen.utils.GeneralUtils.*;

public class AnimClimbing extends AnimCommon {
	public static final ResourceLocation id = new ResourceLocation("LMM", "climbing");
	
	public AnimClimbing() {
		super(id, 1.8f, 1f);
	}
	
	@Override
	public boolean isGeneralConditonsMet(EntityPlayer player, AxisAlignedBB axisAlignedBB) {
		return !player.onGround && player.isOnLadder() && !player.capabilities.isFlying;
	}
	
	@Override
	public void renderAnimation(ModelBiped model, EntityLivingBase entity, float f, float g, float h, float i, float j,
			float u, float delta) {
		ICustomMovementEntity customEntity = (ICustomMovementEntity) entity;
		ModelPartHolder partHolder = customEntity.lmm_$getParHolder();
		
		//partHolder.resetAnimationRotationPoints();
		
		i = clampedI(i);
		
		float[] head = new float[]{
				j * (pi / 180.0f),
				MathHelper.clamp_float(i * (pi / 180.0f) / 1.25f, -pi(2, 5), pi(2, 5)),
				i * (pi / 180.0f) / 6,
				0,
				0,
				0
		};
		float[] body = new float[]{0, 0, 0, 0, 12, 0};
		float[] rArm = new float[]{0, 0, 0, -4, 2, 0};
		float[] lArm = new float[]{0, 0, 0, 4, 2, 0};
		float[] rLeg = new float[]{0, 0, 0, -1.9f, 12, 0.1f};
		float[] lLeg = new float[]{0, 0, 0, 1.9f, 12, 0.1f};
		
		rArm[0] = -pi(3, 5);
		lArm[0] = -pi(3, 5);
		
		rArm[0] += cos((float) entity.posY * 4) * pi(1, 4);
		lArm[0] += cos((float) entity.posY * 4 + pi) * pi(1, 4);
		
		rArm[1] += Math.max(0, -sin((float) entity.posY * 4 + pi)) * pi(1, 6);
		lArm[1] += Math.min(0, -sin((float) entity.posY * 4 + pi)) * pi(1, 6);
		
		body[1] += -cos((float) entity.posY * 4) * pi(1, 4) * pi(1, 16);
		
		head[3] -= cos((float) entity.posY * 4) * 0.75f;
		
		body[4] -= 1;
		
		rArm[4] = (cos((float) entity.posY * 4) + 0.75f) * 2;
		lArm[4] = (cos((float) entity.posY * 4 + pi) + 0.75f) * 2;
		
		rArm[5] = Math.max(-3, (sin((float) entity.posY * 4)) * 1.5f);
		lArm[5] = Math.max(-3, (sin((float) entity.posY * 4 + pi)) * 1.5f);
		
		rLeg[0] = -pi(1, 12);
		lLeg[0] = -pi(1, 12);
		
		rLeg[2] = pi(1, 100);
		lLeg[2] = -pi(1, 100);
		rLeg[1] = pi(1, 100);
		lLeg[1] = -pi(1, 100);
		
		body[0] = -pi(1, 20);
		
		body[5] = sin(body[0]) * 12;

		rLeg[5] = sin(body[0]) * 12 +
				Math.max((sin((float) (entity.posY * 4 + pi)) - 1), -1.5f);
		
		lLeg[5] = sin(body[0]) * 12 + Math.max((sin((float) (entity.posY * 4)) - 1), -1.5f);
		
		rLeg[4] += -2 + (cos((float) (entity.posY * 4 + pi)) - 1) * 2;
		lLeg[4] += -2 + (cos((float) (entity.posY * 4)) - 1) * 2;
		
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
	public boolean customBodyHeadRotation(EntityLivingBase entity) {
		return true;
	}
}
