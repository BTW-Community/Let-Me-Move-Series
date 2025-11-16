package net.dravigen.let_me_move.animation.player.poses;

import net.dravigen.dranimation_lib.animation.BaseAnimation;
import net.dravigen.dranimation_lib.interfaces.ICustomMovementEntity;
import net.dravigen.dranimation_lib.utils.AnimationUtils;
import net.dravigen.dranimation_lib.utils.GeneralUtils;
import net.dravigen.dranimation_lib.utils.ModelPartHolder;
import net.minecraft.src.*;

import static net.dravigen.dranimation_lib.utils.GeneralUtils.*;

@SuppressWarnings("unused")
public class AnimCommon extends BaseAnimation {
	public AnimCommon(ResourceLocation id, float height, float speedModifier, boolean needYOffsetUpdate,
			int maxCooldown, int duration, boolean shouldAutoUpdate, float yOffset) {
		super(id, height, speedModifier, needYOffsetUpdate, maxCooldown, duration, shouldAutoUpdate, yOffset);
	}
	
	public AnimCommon(ResourceLocation id, float height, float speedModifier, boolean needYOffsetUpdate,
			int maxCooldown, int duration, boolean shouldAutoUpdate) {
		super(id, height, speedModifier, needYOffsetUpdate, maxCooldown, duration, shouldAutoUpdate, 0);
	}
	
	public AnimCommon(ResourceLocation id, float height, float speedModifier, boolean needYOffsetUpdate,
			int maxCooldown, int duration) {
		super(id, height, speedModifier, needYOffsetUpdate, maxCooldown, duration, true, 0);
	}
	
	public AnimCommon(ResourceLocation id, float height, float speedModifier, boolean needYOffsetUpdate,
			int maxCooldown) {
		super(id, height, speedModifier, needYOffsetUpdate, maxCooldown, 0, true, 0);
	}
	
	public AnimCommon(ResourceLocation id, float height, float speedModifier, boolean needYOffsetUpdate) {
		super(id, height, speedModifier, needYOffsetUpdate, 0, 0, true, 0);
	}
	
	public AnimCommon(ResourceLocation id, float height, float speedModifier) {
		super(id, height, speedModifier, false, 0, 0, true, 0);
	}
	
	public AnimCommon(ResourceLocation id, float height) {
		super(id, height, 1, false, 0, 0, true, 0);
	}
	
	public AnimCommon(ResourceLocation id) {
		super(id, 1.8f, 1, false, 0, 0, true, 0);
	}
	
	protected void eatFood(float h, EntityLivingBase player, float[] head, float[] rArm) {
		if (player.isEating() && (player.getHeldItem().getItem() instanceof ItemFood)) {
			head[0] = sin(h * 2) * pi(1, 32) + pi(1, 12);
			head[1] = 0;
			
			head[2] += sin(h) * pi(1, 16);
			
			rArm[0] -= pi(8, 16);
			rArm[2] += pi(3, 16);
			
			rArm[4] += 2;
			rArm[5] += 1;
		}
	}
	
	protected void swingArm(ModelBiped model, float[] body, float[] rArm, float[] lArm, float[] head) {
		float onGround = model.onGround;
		if (!(onGround <= 0.0F)) {
			onGround *= 1f;
			float v2 = 2f;
			
			body[1] = MathHelper.sin(MathHelper.sqrt_float(onGround) * (pi * 2)) * 0.2F * v2;
			
			rArm[5] = sin(body[1]) * 5.0F;
			rArm[3] = -cos(body[1]) * 5.0F;
			
			lArm[5] = -sin(body[1]) * 5.0F;
			lArm[3] = cos(body[1]) * 5.0F;
			
			rArm[1] = rArm[1] + body[1];
			
			lArm[1] = lArm[1] + body[1];
			lArm[0] = lArm[0] + body[1];
			
			onGround = 1.0F - model.onGround;
			onGround *= onGround;
			onGround *= onGround;
			onGround = 1.0F - onGround;
			
			float v = MathHelper.sin(onGround * pi);
			float ground = model.onGround;
			float v1 = MathHelper.sin(ground * pi) * -(head[0] - 0.7F) * 0.75F;
			
			rArm[0] = MathHelper.clamp_float ((float) (rArm[0] - (v * 1.2 + v1)) * v2, -pi(3, 4), pi(3, 4));
			rArm[1] = MathHelper.clamp_float(rArm[1] + body[1] * 1.2F, -pi(1, 4), pi(1, 4));
			rArm[2] = MathHelper.clamp_float(rArm[2] + MathHelper.sin(ground * pi) * -0.4F, -pi(1, 4), pi(1, 4));
		}
	}
	
	protected void breath(float h, float[] head, float[] rArm, float[] lArm, float[] rLeg,
			float[] lLeg, float[] body) {
		head[0] += sin(h / 8) * pi(1, 80);
		head[2] += sin(h / 6) * pi(1, 64);
		
		rArm[0] += (sin(h / 16f)) * pi(1, 128);
		rArm[2] += (cos(h / 8f + pi(1, 5)) + 1) * pi(1, 64);
		
		lArm[0] += -(sin(h / (16f))) * pi(1, 128);
		lArm[2] += -(cos(h / (8f) + pi(1, 5)) + 1) * pi(1, 64);
		
		float v0 = cos(h / 12) * 0.05f;
		float v2 = cos(h / 16) * 0.025f;
		
		rLeg[0] += v0;
		rLeg[2] += v2;
		
		lLeg[0] += v0;
		lLeg[2] += v2;
		
		body[0] -= v0 * 0.5f;
		body[2] -= v2 * 0.5f;
		
		float v3 = (sin(h / 8) + 0.65f) * 0.25f;
		
		head[4] += v3;
		body[4] += v3;
		rArm[4] += v3;
		lArm[4] += v3;
	}
	
	protected void moveAround(float h, float[] head, float[] rArm, float[] lArm, float[] rLeg,
			float[] lLeg, float[] body) {
		breath(h, head, rArm, lArm, rLeg, lLeg, body);
		
		float v0 = cos(h / 12) * 0.05f;
		float v2 = cos(h / 16) * 0.025f;
		
		head[3] += sin(v2) * 12;
		head[5] -= sin(v0) * 12;
		body[3] += sin(v2) * 12;
		body[5] -= sin(v0) * 12;
		rArm[3] += sin(v2) * 12;
		rArm[5] -= sin(v0) * 12;
		lArm[3] += sin(v2) * 12;
		lArm[5] -= sin(v0) * 12;
		rLeg[3] += sin(v2) * 12;
		rLeg[5] -= sin(v0) * 12;
		lLeg[3] += sin(v2) * 12;
		lLeg[5] -= sin(v0) * 12;
	}
	
	@Override
	public boolean isGeneralConditonsMet(EntityPlayer player, AxisAlignedBB axisAlignedBB) {
		return false;
	}
	
	@Override
	public boolean isActivationConditonsMet(EntityPlayer player, AxisAlignedBB axisAlignedBB) {
		return true;
	}
	
	@Override
	public void renderAnimation(ModelBiped model, EntityLivingBase entity, float f, float g, float h, float i, float j,
			float u, float delta) {
		ICustomMovementEntity customEntity = (ICustomMovementEntity) entity;
		
		EntityPlayer player = (EntityPlayer) entity;
		
		ModelPartHolder partHolder = customEntity.lmm_$getParHolder();
		
		i = clampedI(i);
		
		float[] head = new float[]{
				j * (pi / 180.0f) / 1.25f,
				i * (pi / 180.0f) / 1.25f,
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
		
		float k = 1;
		
		MovementType movementType = GeneralUtils.getRelativeMovement(player);
		
		boolean isFloating = player.inWater && !player.onGround;
		double motionY = player.posY - player.prevPosY;
		boolean isJumping = customEntity.lmm_$getJumpTime() > 0 && !player.inWater;
		boolean isCrouching = model.isSneak || customEntity.lmm_$isAnimation(AnimCrouching.id);
		boolean isFlying = customEntity.lmm_$getIsFlying();
		float forw = GeneralUtils.getMovementComponents(player)[0];
		float straf = GeneralUtils.getMovementComponents(player)[1];
		forw = forw > 0 ? forw < 0.2 ? 0 : forw : forw > -0.2 ? 0 : forw;
		straf = straf > 0 ? straf < 0.2 ? 0 : straf : straf > -0.2 ? 0 : straf;
		boolean bSprint = player.isSprinting();
		boolean backward = movementType == MovementType.BACKWARD && straf == 0;
		int jumpSwing = customEntity.lmm_$getJumpSwing();
		boolean isMoving = player.posX != player.prevPosX || player.posZ != player.prevPosZ;
		
		if (entity == Minecraft.getMinecraft().thePlayer && !entity.isRiding()) {
			float yaw;
			
			if (isFlying) {
				yaw = forw > 0
					  ? (straf == 0 ? 0 : straf > 0 ? -45 : straf < 0 ? 45 : 0)
					  : forw == 0
						? (straf > 0 ? -90 : straf < 0 ? 90 : 0)
						: forw < 0 ? (straf > 0 ? 45 : straf < 0 ? -45 : 0) : 0;
				
				entity.renderYawOffset = incrementAngleUntilGoal(entity.renderYawOffset,
																 entity.rotationYaw + yaw,
																 delta * 0.1f);
			}
			else if (isMoving) {
				yaw = forw > 0
					  ? (straf == 0 ? 0 : straf > 0 ? -45 : straf < 0 ? 45 : 0)
					  : forw == 0
						? (straf > 0 ? -90 : straf < 0 ? 90 : 0)
						: forw < 0 ? (straf > 0 ? -135 : straf < 0 ? 135 : 0) : 0;
				
				player.renderYawOffset = incrementAngleUntilGoal(player.renderYawOffset,
																 player.rotationYaw + yaw,
																 delta * 0.1f);
			}
		}
		float mul = bSprint ? 0.525f : 0.5f;
		
		if (model.isRiding) {
			Entity mount = entity.ridingEntity;
			entity.renderYawOffset = mount.rotationYaw + 90;
			
			if (mount instanceof EntityMinecart minecart) {
				
				rLeg[4] = 4;
				lLeg[4] = 4;
				rLeg[5] = -6;
				lLeg[5] = -6;
				AnimationUtils.offsetAllRotationPoints(0, 0, 5, head, rArm, lArm, rLeg, lLeg, body);
				rArm[0] -= pi(2, 5);
				lArm[0] -= pi(2, 5);
				rArm[1] -= pi(1, 8);
				lArm[1] += pi(1, 8);
				rLeg[0] = -pi(1, 8);
				lLeg[0] = -pi(1, 8);
			}
			else {
				
				rArm[0] -= 0.62831855f;
				lArm[0] -= 0.62831855f;
				rLeg[0] -= 1.2566371f;
				lLeg[0] -= 1.2566371f;
				rLeg[1] += 0.31415927f;
				lLeg[1] -= 0.31415927f;
			}
		}
		else {
			if (isFlying) {
				backward = forw < 0;
				
				mul = motionY < 0 ? (float) -(motionY * 48) : motionY > 0 ? 0 : 1;
				
				rArm[2] = pi(1, 48) * mul;
				lArm[2] = -pi(1, 48) * mul;
				rLeg[2] = pi(1, 128);
				lLeg[2] = -pi(1, 128);
				
				if (isMoving) {
					g *= backward ? -1 : 1;
					
					head[0] += -g * pi(1, 12);
					rArm[2] += pi(1, 16);
					lArm[2] -= pi(1, 16);
					rArm[0] = g * pi(1, 4);
					lArm[0] = g * pi(1, 4);
					rLeg[0] = g * pi(1, 4);
					lLeg[0] = g * pi(1, 4);
					rLeg[2] += pi(1, 64);
					lLeg[2] -= pi(1, 64);
					
				}
				
				head[0] += sin(h / 8) * pi(1, 80);
				
				rArm[0] += (sin(h / 18f)) * pi(1, 16);
				lArm[0] += -(sin(h / (14f))) * pi(1, 16);
				rLeg[0] += (cos(h / 6f)) * pi(1, 16);
				lLeg[0] += (cos(h / 6f + pi)) * pi(1, 16);
				
				rArm[2] += (cos(h / 10f - pi(1, 4)) + 1) * pi(1, (int) (12 + 40 * Math.abs(motionY)));
				lArm[2] += -(cos(h / (10f) - pi(1, 4)) + 1) * pi(1, (int) (12 + 40 * Math.abs(motionY)));
				rLeg[2] += (cos(h / 10f - pi(1, 6)) + 1) * pi(1, 128);
				lLeg[2] += -(cos(h / (10f) - pi(1, 6)) + 1) * pi(1, 128);
				
				float v = sin(h / 10) * 1.25f;
				head[4] += v;
				body[4] += v;
				rArm[4] += v;
				lArm[4] += v;
				rLeg[4] += v;
				lLeg[4] += v;
				
				if (forw > 0 || straf != 0 && !(forw < 0)) {
					rLeg[4] -= 2;
					rLeg[5] -= 3;
				}
			}
			else if (isJumping) {
				double motYposRev = 1 - Math.max(0, motionY);
				
				if (isMoving) {
					body[0] = (bSprint ? pi(1, 32) : 0);
					body[1] = jumpSwing * pi(1, 16) * g;
					
					if (!isCrouching) {
						rLeg[1] = pi(1, 64);
						lLeg[1] = -pi(1, 64);
					}
					
					rLeg[2] = pi(1, 100);
					lLeg[2] = -pi(1, 100);
					
					if (bSprint) {
						head[4] = 12 - cos(body[0]) * 12;
						head[5] = -sin(body[0]) * 12;
						
						rArm[3] = -cos(body[1]) * 5.0F;
						rArm[4] = 12 - cos(body[0]) * 10;
						rArm[5] = sin(body[1]) * 5.0F - sin(body[0]) * 12;
						
						lArm[3] = cos(body[1]) * 5.0F;
						lArm[4] = 12 - cos(body[0]) * 10;
						lArm[5] = -sin(body[1]) * 5.0F - sin(body[0]) * 12;
						
						rLeg[3] = -cos(body[1]) * 2f;
						rLeg[5] = -sin(body[1]) * 2f;
						
						lLeg[3] = cos(body[1]) * 2f;
						lLeg[5] = sin(body[1]) * 2f;
						
						rArm[1] += body[1];
						lArm[1] += body[1];
						head[0] += body[0];
						
						
						rArm[0] = (float) (jumpSwing * pi(1, 3) * g * (1 - motionY));
						lArm[0] = (float) (-jumpSwing * pi(1, 3) * g * (1 - motionY));
						rLeg[0] = (float) (-jumpSwing * pi(1, 4) * g * (1 - motionY));
						lLeg[0] = (float) (jumpSwing * pi(1, 4) * g * (1 - motionY));
						
						rLeg[2] = (float) Math.abs(jumpSwing * pi(1, 32) * g * (1 - motionY));
						lLeg[2] = (float) -Math.abs(jumpSwing * pi(1, 32) * g * (1 - motionY));
					}
					else {
						float v = isCrouching ? 0.25f : 1;
						float v1 = isCrouching ? 0.4f : 1;
						rArm[0] = (float) (jumpSwing * pi(1, 3) * (1 - motionY) * v1);
						lArm[0] = (float) (-jumpSwing * pi(1, 3) * (1 - motionY) * v1);
						
						rLeg[0] = pi(1, 16) * (jumpSwing < 0 ? backward ? 0 : 3 : backward ? -2 : 2) * v;
						lLeg[0] = pi(1, 16) * (-jumpSwing < 0 ? backward ? 0 : 3 : backward ? -2 : 2) * v;
						
						rLeg[4] = (float) (12 - Math.max(0, jumpSwing * 2) * 2 * motYposRev * v1);
						rLeg[5] = (float) (-Math.max(0, jumpSwing * 2) * g * 2 * motYposRev * v);
						lLeg[4] = (float) (12 - Math.max(0, -jumpSwing * 2) * 2 * motYposRev * v1);
						lLeg[5] = (float) (-Math.max(0, -jumpSwing * 2) * g * 2 * motYposRev * v);
					}
				}
				else {
					rArm[1] = rArm[1] + body[1];
					lArm[1] = lArm[1] + body[1];
					head[1] += -body[1] / 4;
					
					rLeg[2] += pi(1, 64);
					lLeg[2] -= pi(1, 64);
					
					float v = isCrouching ? 0.25f : 1;
					float v1 = isCrouching ? 0.4f : 1;
					
					rArm[0] = (float) (jumpSwing * pi(1, 3) * (1 - motionY) * v1);
					lArm[0] = (float) (-jumpSwing * pi(1, 3) * (1 - motionY) * v1);
					
					rLeg[0] = pi(1, 16) * Math.max(0, jumpSwing * 2) * v;
					lLeg[0] = pi(1, 16) * Math.max(0, -jumpSwing * 2) * v;
					
					
					rLeg[4] = (float) (12 - Math.max(0, jumpSwing * 4) * motYposRev * v1);
					rLeg[5] = (float) (-Math.max(0, jumpSwing * 3) * motYposRev * v);
					lLeg[4] = (float) (12 - Math.max(0, -jumpSwing * 4) * motYposRev * v1);
					lLeg[5] = (float) (-Math.max(0, -jumpSwing * 3) * motYposRev * v);
				}
			}
			else {
				f *= backward ? -0.5f : 1;
				g *= backward ? 0.75f : 1;
				
				f = isCrouching ? backward ? -h * 0.75f : h : f;
				g = isCrouching ? 0.4f : g;
				
				g *= model.aimedBow || model.heldItemRight == 3 ? 2f : 1;
				
				
				if (isFloating) {
					rArm[2] = (cos(h / 6) + 1) * pi(1, 8);
					lArm[2] = -(cos(h / 6) + 1) * pi(1, 8);
					
					rArm[0] = sin(h / 6 + pi) * g * pi(1, 2);
					lArm[0] = sin(h / 6 + pi) * g * pi(1, 2);
					
					rArm[4] -= cos(h / 6 + pi(1, 2));
					lArm[4] -= cos(h / 6 + pi(1, 2));
					
					rLeg[0] = cos(h / 3) * (g + 1) * pi(1, 5);
					lLeg[0] = cos(h / 3 + pi) * (g + 1) * pi(1, 5);
				}
				else {
					f = player.inWater ? h / 3 : f;
					g = player.inWater ? 0.5f : g;
					
					if (isMoving) {
						rArm[0] = cos(f * mul) * 2.0F * g * (bSprint ? 0.8f : 0.65F) / k;
						lArm[0] = cos(f * mul + pi) * 2.0F * g * (bSprint ? 0.8f : 0.65F) / k;
						
						body[0] = (cos(f * mul * 2) + 1) * g * (bSprint ? 0.1f : 0) / k + (bSprint ? pi(1, 32) : 0);
						body[1] = cos(f * mul) *
								g *
								(bSprint ? 0.0f : backward ? 0.8f : 0.5F) *
								(isCrouching ? 0.25f : 1) / k;
						
						if (!isCrouching) {
							rLeg[1] = pi(1, 64);
							lLeg[1] = -pi(1, 64);
						}
						
						rLeg[0] = Math.max(-2, (cos(f * mul + pi)) * g * (bSprint ? 0.9f : 0.6f) / k);
						rLeg[2] = pi(1, 100);
						
						lLeg[0] = Math.max(-2, (cos(f * mul)) * g * (bSprint ? 0.9f : 0.6f) / k);
						lLeg[2] = -pi(1, 100);
						
						if (player.inWater && !isHeadInsideWater(player)) {
							body[0] = (cos(f * mul * 2) + 1) * g * 0.25F / k;
							body[1] = cos(f * mul) * g * 0.6F / k;
							
							rArm[2] += pi(1, 3);
							lArm[2] -= pi(1, 3);
							
							rArm[0] = cos(f * mul) * 2.0F * g * 1f / k;
							lArm[0] = cos(f * mul + pi) * 2.0F * g * 1f / k;
							
							rArm[4] -= 1;
							lArm[4] -= 1;
						}
						
						head[4] = 12 - cos(body[0]) * 12;
						head[5] = -sin(body[0]) * 12;
						rArm[3] = -cos(body[1]) * 5.0F;
						rArm[4] = 12 - cos(body[0]) * 10;
						rArm[5] = sin(body[1]) * 5.0F - sin(body[0]) * 12;
						lArm[3] = cos(body[1]) * 5.0F;
						lArm[4] = 12 - cos(body[0]) * 10;
						lArm[5] = -sin(body[1]) * 5.0F - sin(body[0]) * 12;
						rLeg[3] = -cos(body[1]) * 2f;
						rLeg[4] = Math.max((sin(f * mul) - 1) * g * (bSprint ? 2.5f : 1.75f) + 12, 6);
						rLeg[5] = -sin(body[1]) * 2f;
						lLeg[3] = cos(body[1]) * 2f;
						lLeg[4] = Math.max((sin(f * mul + pi) - 1) * g * (bSprint ? 2.5f : 1.75f) + 12, 6);
						lLeg[5] = sin(body[1]) * 2f;
						
						rArm[1] += body[1] + (bSprint ? cos(f * mul) * pi(1, 8) : 0);
						lArm[1] += body[1] + (bSprint ? cos(f * mul) * pi(1, 8) : 0);
						head[0] += body[0];
						//head[1] += -body[1] / 3;
						
						float v = 2f;
						float v1 = bSprint ? 3.5f : backward ? 0.2f : 2f;
						float v2 = Math.min(cos(f * mul * v) * g * v1, 2) *
								(entity == Minecraft.getMinecraft().thePlayer &&
										 Minecraft.getMinecraft().gameSettings.thirdPersonView == 0 ? 0.25f : 1);
						
						head[4] -= v2;
						body[4] -= v2;
						rArm[4] -= v2;
						lArm[4] -= v2;
						rLeg[4] -= v2;
						lLeg[4] -= v2;
					}
					else {
						rArm[1] = rArm[1] + body[1];
						lArm[1] = lArm[1] + body[1];
						body[1] += head[1] * (isCrouching ? 0 : 0.5f);
						rArm[1] += body[1] * 0.75f;
						lArm[1] += body[1] * 0.75f;
						
						rArm[3] = -cos(body[1] * 0.75f) * 5.0F;
						rArm[5] = sin(body[1] * 0.75f) * 5.0F;
						lArm[3] = cos(body[1] * 0.75f) * 5.0F;
						lArm[5] = -sin(body[1] * 0.75f) * 5.0F;
						
						rLeg[0] -= pi(1, 80);
						rLeg[2] += pi(1, 50);
						lLeg[0] += pi(1, 128);
						lLeg[2] -= pi(1, 50);
						rLeg[1] += pi(1, 32);
						lLeg[1] -= pi(1, 12);
						
						rLeg[5] -= 0.6f;
						lLeg[5] += 0.5f;
					}
				}
			}
		}
		
		this.swingArm(model, body, rArm, lArm, head);
		
		if (isCrouching && !isFlying) {
			body[0] = 0.5F;
			
			rArm[0] += 0.4F;
			lArm[0] += 0.4F;
			
			body[5] += 4.8f;
			rLeg[5] += 4f;
			lLeg[5] += 4f;
			
			head[4] += 4.2f;
			body[4] += 1f;
			rArm[4] += 3.2f;
			lArm[4] += 3.2f;
			rLeg[4] += 0.2f;
			lLeg[4] += 0.2f;
		}
		
		this.moveAround(h, head, rArm, lArm, rLeg, lLeg, body);
		
		if (model.aimedBow) {
			float var8 = 0.0f;
			float var9 = 0.0f;
			
			rArm[0] = -1.5707964f + head[0];
			rArm[1] = -(0.1f - var8 * 0.6f) + head[1];
			rArm[2] = 0;
			
			rArm[0] -= var8 * 1.2f - var9 * 0.4f;
			rArm[2] += MathHelper.cos(h * 0.09f) * 0.05f + 0.05f;
			rArm[0] += MathHelper.sin(h * 0.067f) * 0.05f;
			
			lArm[0] = -1.5707964f + head[0];
			lArm[1] = 0.1f - var8 * 0.6f + head[1] + 0.4f;
			lArm[2] = 0;
			
			lArm[0] -= var8 * 1.2f - var9 * 0.4f;
			lArm[2] -= MathHelper.cos(h * 0.09f) * 0.05f + 0.05f;
			lArm[0] -= MathHelper.sin(h * 0.067f) * 0.05f;
		}
		else {
			rArm[0] = model.heldItemRight != 0 ? rArm[0] * 0.5f - 0.31415927f * (float) model.heldItemRight : rArm[0];
			
			lArm[0] = model.heldItemLeft != 0 ? lArm[0] * 0.5f - 0.31415927f * (float) model.heldItemLeft : lArm[0];
		}
		
		this.eatFood(h, player, head, rArm);
		
		if (this.getID() == AnimGui.id) {
			rArm[0] -= pi(1, 3) + sin(h / 4) * pi(1, 64);
			lArm[0] -= pi(1, 3) + sin(h / 4 + pi) * pi(1, 64);
			rArm[1] -= pi(1, 7);
			lArm[1] += pi(1, 7);
			
			head[0] = MathHelper.clamp_float(pi(1, 6) + sin(h / 24) * pi(1, 12), pi(1, 16), pi(1, 5));
			head[2] += MathHelper.clamp_float(sin(h / 12) * pi(1, 8), -pi(1, 16), pi(1, 16));
		}
		
		AnimationUtils.smoothRotateAll(partHolder.getHead(), head, 0.3f * delta, 0.7f * delta);
		AnimationUtils.smoothRotateAll(partHolder.getBody(), body, 0.8f * delta, 0.7f * delta);
		AnimationUtils.smoothRotateAll(partHolder.getrArm(), rArm, 0.3f * delta, 0.7f * delta);
		AnimationUtils.smoothRotateAll(partHolder.getlArm(), lArm, 0.3f * delta, 0.7f * delta);
		AnimationUtils.smoothRotateAll(partHolder.getrLeg(), rLeg, 0.3f * delta, 0.7f * delta);
		AnimationUtils.smoothRotateAll(partHolder.getlLeg(), lLeg, 0.3f * delta, 0.7f * delta);
	}
	
	protected static float clampedI(float i) {
		i %= 360;
		i = i < -180 ? i + 360 : i > 180 ? i - 360 : i;
		return i;
	}
	
	@Override
	public void updateLeaning(EntityLivingBase entity) {
		ICustomMovementEntity customEntity = (ICustomMovementEntity) entity;
		float goal = 0;
		
		customEntity.lmm_$setLeaningPitch(goal);
	}
	
	@Override
	public boolean getHungerCost(EntityPlayer player, double distX, double distY, double distZ) {
		return false;
	}
	
	@Override
	public boolean getCustomMove(EntityPlayer player) {
		return false;
	}
	
	@Override
	public boolean customBodyHeadRotation(EntityLivingBase entity) {
		return ((EntityPlayer) entity).capabilities.isFlying;
	}
}
