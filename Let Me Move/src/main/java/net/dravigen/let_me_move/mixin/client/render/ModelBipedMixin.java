package net.dravigen.let_me_move.mixin.client.render;

import btw.block.BTWBlocks;
import btw.entity.model.PlayerArmorModel;
import net.dravigen.dr_api_gen.animation.BaseAnimation;
import net.dravigen.dr_api_gen.interfaces.ICustomMovementEntity;
import net.dravigen.dr_api_gen.utils.AnimationUtils;
import net.dravigen.dr_api_gen.utils.GeneralUtils;
import net.dravigen.let_me_move.LetMeMoveAddon;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static net.dravigen.let_me_move.animation.AnimRegistry.*;

@Mixin(ModelBiped.class)
public abstract class ModelBipedMixin extends ModelBase {
	@Shadow public ModelRenderer bipedCloak;
	@Unique
	long prevTime;
	@Unique
	float delta = 1;
	
	@Inject(method = "render", at = @At("HEAD"))
	private void rotateBody(Entity entity, float f, float g, float h, float i, float j, float u, CallbackInfo ci) {
		if (LetMeMoveAddon.isExtraLoaded()) return;
		
		if (!(entity instanceof EntityPlayer player) || (ModelBiped) (Object) this instanceof PlayerArmorModel) return;
		
		ICustomMovementEntity customEntity = (ICustomMovementEntity) player;
		
		if (!entity.onGround) {
			List boundingBoxes = player.worldObj.getCollidingBoundingBoxes(player,
																		   new AxisAlignedBB(player.posX -
																									 player.width / 2,
																							 player.boundingBox.minY -
																									 0.1,
																							 player.posZ -
																									 player.width / 2,
																							 player.posX +
																									 player.width / 2,
																							 player.boundingBox.minY,
																							 player.posZ +
																									 player.width / 2));
			customEntity.lmm_$setOnGround(!boundingBoxes.isEmpty());
		}
		
		if (!customEntity.lmm_$getOnGround() && !customEntity.lmm_$getIsFlying()) {
			if (player.posY > player.prevPosY) {
				if (customEntity.lmm_$getPrevJumpTime() == -1) {
					customEntity.lmm_$setPrevJumpTime(0);
					customEntity.lmm_$setJumpSwing(customEntity.lmm_$getJumpSwing() * -1);
				}
			}
			else {
				customEntity.lmm_$setPrevJumpTime(-1);
			}
			
			customEntity.lmm_$setJumpTime(customEntity.lmm_$getJumpTime() + 1);
		}
		else {
			customEntity.lmm_$setJumpTime(0);
		}
		
		BaseAnimation animation = customEntity.lmm_$getAnimation();
		
		if (animation == null) return;
		
		float leaningPitch = customEntity.lmm_$getLeaningPitch(Minecraft.getMinecraft().getTimer().renderPartialTicks);
		
		float delta = (System.currentTimeMillis() - this.prevTime) / 25f;
		delta = delta > 8 ? 8 : delta;
		
		this.delta = delta;
		
		customEntity.lmm_$setDelta(delta);
		
		float prevXRotation;
		float prevYRotation;
		float prevZRotation;
		float prevOffset;
		
		float[] renderRotOff = customEntity.lmm_$getRenderRotOff();
		
		if (animation.needYOffsetUpdate) {
			if (customEntity.lmm_$isAnimation(HIGH_FALLING.getID())) {
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
				prevOffset = animation.yOffset != 0 ? animation.yOffset : 1.98f - (animation.height);
				prevYRotation = GeneralUtils.incrementAngleUntilGoal(renderRotOff[2], 0, 0.1f * delta);
				prevZRotation = GeneralUtils.incrementAngleUntilGoal(renderRotOff[3], 0, 0.1f * delta);
				prevXRotation = GeneralUtils.incrementAngleUntilGoal(renderRotOff[1], 90 * leaningPitch, 0.4f * delta);
			}
		}
		else {
			prevOffset = GeneralUtils.incrementUntilGoal(renderRotOff[0], 0, 0.4f * delta);
			prevYRotation = GeneralUtils.incrementAngleUntilGoal(renderRotOff[2], 0, 0.2f * delta);
			prevZRotation = GeneralUtils.incrementAngleUntilGoal(renderRotOff[3], 0, 0.2f * delta);
			prevXRotation = GeneralUtils.incrementAngleUntilGoal(renderRotOff[1], 0, 0.35f * delta);
		}
		
		
		float[] newRenderRotOff = new float[]{prevOffset, prevXRotation, prevYRotation, prevZRotation};
		
		customEntity.lmm_$setRenderRotOff(newRenderRotOff);
		
		this.prevTime = System.currentTimeMillis();
		
		GL11.glTranslatef(0, prevOffset, 0);
		GL11.glRotatef(prevYRotation, 0, 1, 0);
		GL11.glRotatef(prevZRotation, 0, 0, 1);
		GL11.glRotatef(prevXRotation, 1, 0, 0);
		
		if (customEntity.lmm_$isAnimation(HIGH_FALLING.getID())) GL11.glTranslatef(0, -prevOffset, 0);
		else if (customEntity.lmm_$isAnimation(CLIMBING.getID())) {
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
	
	@ModifyArg(method = "<init>(FFII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ModelRenderer;addBox(FFFIIIF)V", ordinal = 4), index = 1)
	private float changeBodyPos(float par1) {
		return -12;
	}
	
	@ModifyArg(method = "<init>(FFII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ModelRenderer;setRotationPoint(FFF)V", ordinal = 2), index = 1)
	private float changeBodyPosRot(float par1) {
		return par1 + 12;
	}
	
	@Inject(method = "setRotationAngles", at = @At("HEAD"), cancellable = true)
	public void setAngles(float f, float g, float h, float i, float j, float u, Entity entity, CallbackInfo ci) {
		ICustomMovementEntity customEntity = (ICustomMovementEntity) entity;
		
		if (customEntity.lmm_$getAnimation() == null) return;
		
		ci.cancel();
		
		if (entity instanceof EntityPlayer player) {
			customEntity.lmm_$getAnimation()
					.renderAnimation((ModelBiped) (Object) this,
									 player,
									 f,
									 g,
									 h,
									 i,
									 j,
									 u,
									 this.delta);
			
			AnimationUtils.updateAnimationRotation(customEntity.lmm_$getParHolder(), (ModelBiped) (Object) this);
			
			if (player.getCurrentItemOrArmor(2) == null) {
				this.bipedCloak.rotationPointZ = 0.0F;
				this.bipedCloak.rotationPointY = 0.0F;
			}
			else {
				this.bipedCloak.rotationPointZ = -1.1F;
				this.bipedCloak.rotationPointY = -0.85F;
			}
		}
	}
}
