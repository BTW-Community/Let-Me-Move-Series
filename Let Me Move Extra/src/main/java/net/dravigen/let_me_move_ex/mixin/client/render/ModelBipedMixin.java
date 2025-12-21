package net.dravigen.let_me_move_ex.mixin.client.render;

import btw.block.BTWBlocks;
import btw.entity.model.PlayerArmorModel;
import net.dravigen.dranimation_lib.animation.BaseAnimation;
import net.dravigen.dranimation_lib.interfaces.ICustomMovementEntity;
import net.dravigen.dranimation_lib.utils.AnimationUtils;
import net.dravigen.dranimation_lib.utils.GeneralUtils;
import net.dravigen.let_me_move.animation.player.poses.AnimClimbing;
import net.dravigen.let_me_move.animation.player.poses.AnimHighFalling;
import net.dravigen.let_me_move_ex.animation.player.actions.AnimCrawling;
import net.dravigen.let_me_move_ex.animation.player.actions.AnimPullingUp;
import net.dravigen.let_me_move_ex.animation.player.actions.AnimWallSliding;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelBiped.class)
public abstract class ModelBipedMixin extends ModelBase {
	
	@Inject(method = "render", at = @At("HEAD"))
	private void rotateBody(Entity entity, float f, float g, float h, float i, float j, float u, CallbackInfo ci) {
		if (!AnimationUtils.extraIsPresent) return;
		
		if (!(entity instanceof EntityPlayer player) || (ModelBiped) (Object) this instanceof PlayerArmorModel) return;
		
		ICustomMovementEntity customEntity = (ICustomMovementEntity) player;
		
		{
			if (player != Minecraft.getMinecraft().thePlayer) {
				if (!entity.isRiding() && !entity.inWater) {
					boolean onGround = player.worldObj.checkBlockCollision(player.boundingBox.copy().offset(0, -0.001f, 0));
					
					if (customEntity.lmm_$getOnGround() && !onGround) {
						customEntity.lmm_$setJumpSwing();
					}
					customEntity.lmm_$setOnGround(onGround);
				}
				else {
					customEntity.lmm_$setOnGround(true);
				}
			}
		}
		
		if (!customEntity.lmm_$getOnGround() && !customEntity.lmm_$getIsFlying() && !this.isRiding) {
			customEntity.lmm_$setJumpTime(customEntity.lmm_$getJumpTime() + 1);
		}
		else {
			customEntity.lmm_$setJumpTime(0);
		}
		
		BaseAnimation animation = customEntity.lmm_$getAnimation();
		
		if (animation == null) return;
		
		float leaningPitch = customEntity.lmm_$getLeaningPitch(Minecraft.getMinecraft().getTimer().renderPartialTicks);
		
		float delta = AnimationUtils.delta;
		
		float prevXRotation;
		float prevYRotation;
		float prevZRotation;
		float prevOffset;
		
		float[] renderRotOff = customEntity.lmm_$getRenderRotOff();
		
		if (animation.needYOffsetUpdate) {
			if (customEntity.lmm_$isAnimation(AnimHighFalling.id)) {
				prevOffset = GeneralUtils.incrementUntilGoal(renderRotOff[0], 0.5f, 0.4f * delta);
				prevYRotation = GeneralUtils.incrementAngleUntilGoal(renderRotOff[2],
																	 (12f * leaningPitch) % 360,
																	 0.3f * delta);
				prevZRotation = GeneralUtils.incrementAngleUntilGoal(renderRotOff[3],
																	 (22.5f * leaningPitch) % 360,
																	 0.3f * delta);
				prevXRotation = GeneralUtils.incrementAngleUntilGoal(renderRotOff[1],
																	 (45f * leaningPitch) % 360,
																	 0.3f * delta);
			}
			else {
				prevOffset = animation.yOffset != 0 ? animation.yOffset : 1.98f - entity.height;
				prevYRotation = GeneralUtils.incrementAngleUntilGoal(renderRotOff[2], 0, 0.2f * delta);
				prevZRotation = GeneralUtils.incrementAngleUntilGoal(renderRotOff[3], 0, 0.2f * delta);
				prevXRotation = GeneralUtils.incrementAngleUntilGoal(renderRotOff[1],
																	 90 * leaningPitch,
																	 (customEntity.lmm_$isAnimation(AnimCrawling.id)
																	  ? 0.8f
																	  : 0.4f) * delta);
			}
		}
		else {
			prevOffset = GeneralUtils.incrementUntilGoal(renderRotOff[0], 0, 0.5f * delta);
			prevYRotation = GeneralUtils.incrementAngleUntilGoal(renderRotOff[2], 0, 0.75f * delta);
			prevZRotation = GeneralUtils.incrementAngleUntilGoal(renderRotOff[3], 0, 0.75f * delta);
			prevXRotation = GeneralUtils.incrementAngleUntilGoal(renderRotOff[1], 0, 0.75f * delta);
		}
		
		float[] newRenderRotOff = new float[]{prevOffset, prevXRotation, prevYRotation, prevZRotation};
		
		customEntity.lmm_$setRenderRotOff(newRenderRotOff);
		
		GL11.glTranslatef(0, prevOffset, 0);
		GL11.glRotatef(prevYRotation, 0, 1, 0);
		GL11.glRotatef(prevZRotation, 0, 0, 1);
		GL11.glRotatef(prevXRotation, 1, 0, 0);
		
		if (customEntity.lmm_$isAnimation(AnimHighFalling.id)) GL11.glTranslatef(0, -prevOffset, 0);
		else if (customEntity.lmm_$isAnimation(AnimWallSliding.id)) {
			GeneralUtils.coords side = GeneralUtils.getWallSide(player, 0, entity.height);
			
			if (side != null) {
				player.renderYawOffset = side == GeneralUtils.coords.EAST
										 ? 45
										 : side == GeneralUtils.coords.SOUTH
										   ? 135
										   : side == GeneralUtils.coords.WEST ? 225 : 315;
			}
		}
		else if (customEntity.lmm_$isAnimation(AnimPullingUp.id)) {
			GeneralUtils.coords side = GeneralUtils.getWallSide(player, 0, entity.height);
			
			if (side != null) {
				player.renderYawOffset = side == GeneralUtils.coords.EAST
										 ? 270
										 : side == GeneralUtils.coords.SOUTH
										   ? 0
										   : side == GeneralUtils.coords.WEST ? 90 : 180;
			}
		}
		else if (customEntity.lmm_$isAnimation(AnimClimbing.id)) {
			int x = MathHelper.floor_double(entity.posX);
			int y = MathHelper.floor_double(entity.boundingBox.minY);
			int z = MathHelper.floor_double(entity.posZ);
			
			if (entity.worldObj.getBlockId(x, y, z) == BTWBlocks.ladder.blockID) {
				int ladderMeta = entity.worldObj.getBlockMetadata(x, y, z);
				
				player.renderYawOffset = switch (ladderMeta) {
					case 0 -> GeneralUtils.incrementAngleUntilGoal(player.renderYawOffset, 0, 1);
					case 1 -> GeneralUtils.incrementAngleUntilGoal(player.renderYawOffset, 180, 1);
					case 2 -> GeneralUtils.incrementAngleUntilGoal(player.renderYawOffset, 270, 1);
					case 3 -> GeneralUtils.incrementAngleUntilGoal(player.renderYawOffset, 90, 1);
					default -> player.renderYawOffset;
				};
			}
		}
	}
}