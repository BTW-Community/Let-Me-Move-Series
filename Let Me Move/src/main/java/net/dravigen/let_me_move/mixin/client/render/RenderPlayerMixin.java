package net.dravigen.let_me_move.mixin.client.render;

import net.dravigen.let_me_move.animation.player.poses.AnimCrouching;
import net.dravigen.let_me_move.animation.player.poses.AnimStanding;
import net.dravigen.dr_api_gen.interfaces.ICustomMovementEntity;
import net.dravigen.dr_api_gen.utils.GeneralUtils;
import net.dravigen.dr_api_gen.utils.ModelPartHolder;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.dravigen.dr_api_gen.utils.GeneralUtils.*;

@Mixin(RenderPlayer.class)
public abstract class RenderPlayerMixin {
	
	@Redirect(method = "renderSpecials", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/AbstractClientPlayer;isSneaking()Z"))
	private boolean customSneakCheck(AbstractClientPlayer instance) {
		ResourceLocation animationID = ((ICustomMovementEntity) instance).lmm_$getAnimationID();
		
		if (animationID == AnimStanding.id) {
			return instance.isSneaking();
		}
		
		return animationID == AnimCrouching.id;
	}
	
	@Redirect(method = "renderSpecials", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glRotatef(FFFF)V", ordinal = 5), remap = false)
	private void customCape1(float angle, float x, float y, float z, AbstractClientPlayer player) {
		ICustomMovementEntity customEntity = (ICustomMovementEntity) player;
		ModelPartHolder partHolder = customEntity.lmm_$getParHolder();
		ResourceLocation animationID = customEntity.lmm_$getAnimationID();
		GL11.glTranslatef(0,
						  (partHolder.getBody()[4] - cos(partHolder.getBody()[0]) * 12) / 16,
						  animationID == AnimStanding.id
						  ? player.isSneaking()
							? 0
							: -((sin(partHolder.getBody()[0]) * 12) / 16)
						  : animationID == AnimCrouching.id ? 0 : -((sin(partHolder.getBody()[0]) * 12) / 16));
	
		customEntity.lmm_$getCapeRot()[0] = GeneralUtils.incrementUntilGoal(customEntity.lmm_$getCapeRot()[0], MathHelper.clamp_float(angle, 0, 135), 0.5f * customEntity.lmm_$getDelta());
		GL11.glRotatef(customEntity.lmm_$getCapeRot()[0], x, y, z);
	}
	
	@Redirect(method = "renderSpecials", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glRotatef(FFFF)V", ordinal = 6), remap = false)
	private void customCape2(float angle, float x, float y, float z, AbstractClientPlayer player) {
		ICustomMovementEntity customEntity = (ICustomMovementEntity) player;
		customEntity.lmm_$getCapeRot()[1] = GeneralUtils.incrementUntilGoal(customEntity.lmm_$getCapeRot()[1], angle, 0.1f * customEntity.lmm_$getDelta());
		GL11.glRotatef(customEntity.lmm_$getCapeRot()[1], x, y, z);
	}
	
	@Redirect(method = "renderSpecials", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glRotatef(FFFF)V", ordinal = 7), remap = false)
	private void customCape3(float angle, float x, float y, float z, AbstractClientPlayer player) {
		ICustomMovementEntity customEntity = (ICustomMovementEntity) player;
		customEntity.lmm_$getCapeRot()[2] = GeneralUtils.incrementUntilGoal(customEntity.lmm_$getCapeRot()[2], angle, 0.1f * customEntity.lmm_$getDelta());
		GL11.glRotatef(customEntity.lmm_$getCapeRot()[2], x, y, z);
	}
	
	
	@Redirect(method = "renderFirstPersonArm", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ModelRenderer;render(F)V"))
	private void renderArm(ModelRenderer arm, float var2) {
		arm.rotateAngleX = 0;
		arm.rotateAngleY = 0;
		arm.rotateAngleZ = 0;
		arm.rotationPointX = -5f;
		arm.rotationPointY = 2;
		arm.rotationPointZ = 0;
		
		arm.render(var2);
	}
}
