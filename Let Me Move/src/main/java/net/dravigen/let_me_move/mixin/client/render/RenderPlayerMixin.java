package net.dravigen.let_me_move.mixin.client.render;

import net.dravigen.dranimation_lib.interfaces.ICustomMovementEntity;
import net.dravigen.dranimation_lib.utils.AnimationUtils;
import net.dravigen.dranimation_lib.utils.GeneralUtils;
import net.dravigen.dranimation_lib.utils.ModelPartHolder;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.dravigen.dranimation_lib.utils.GeneralUtils.*;

@Mixin(RenderPlayer.class)
public abstract class RenderPlayerMixin {
	
	@Shadow
	private ModelBiped modelBipedMain;
	
	@Redirect(method = "renderSpecials", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/AbstractClientPlayer;isSneaking()Z"))
	private boolean customSneakCheck(AbstractClientPlayer player) {
		return false;
	}
	
	@Redirect(method = "renderSpecials", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glRotatef(FFFF)V", ordinal = 5), remap = false)
	private void rotateCapeX(float angle, float x, float y, float z, AbstractClientPlayer player) {
		ICustomMovementEntity customEntity = (ICustomMovementEntity) player;
		ModelPartHolder partHolder = customEntity.lmm_$getParHolder();
		
		GL11.glTranslatef(partHolder.getBody()[3] / 16,
						  (partHolder.getBody()[4] - cos(partHolder.getBody()[0]) * 12) / 16,
						  (partHolder.getBody()[5] - sin(partHolder.getBody()[0]) * 12) / 16);
		
		customEntity.lmm_$setCapeRot(0,
									 GeneralUtils.incrementUntilGoal(customEntity.lmm_$getCapeRot()[0],
																	 MathHelper.clamp_float(angle + partHolder.getBody()[0] * 180 / pi, 0, 90),
																	 0.5f * AnimationUtils.delta));
		GL11.glRotatef(customEntity.lmm_$getCapeRot()[0], x, y, z);
	}
	
	@Redirect(method = "renderSpecials", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glRotatef(FFFF)V", ordinal = 6), remap = false)
	private void rotateCapeZ(float angle, float x, float y, float z, AbstractClientPlayer player) {
		ICustomMovementEntity customEntity = (ICustomMovementEntity) player;
		
		customEntity.lmm_$setCapeRot(1,
									 GeneralUtils.incrementUntilGoal(customEntity.lmm_$getCapeRot()[1],
																	 angle,
																	 0.1f * AnimationUtils.delta));
		
		GL11.glRotatef(customEntity.lmm_$getCapeRot()[1], x, y, z);
	}
	
	@Redirect(method = "renderSpecials", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glRotatef(FFFF)V", ordinal = 7), remap = false)
	private void rotateCapeY(float angle, float x, float y, float z, AbstractClientPlayer player) {
		ICustomMovementEntity customEntity = (ICustomMovementEntity) player;
		
		customEntity.lmm_$setCapeRot(2,
									 GeneralUtils.incrementUntilGoal(customEntity.lmm_$getCapeRot()[2],
																	 angle,
																	 0.1f * AnimationUtils.delta));
		
		GL11.glRotatef(customEntity.lmm_$getCapeRot()[2], x, y, z);
	}
	
	
	@Inject(method = "renderFirstPersonArm", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/ModelRenderer;render(F)V"))
	private void renderArm(EntityPlayer player, CallbackInfo ci) {
		this.modelBipedMain.bipedRightArm.rotateAngleX = 0;
		this.modelBipedMain.bipedRightArm.rotateAngleY = 0;
		this.modelBipedMain.bipedRightArm.rotateAngleZ = 0;
		this.modelBipedMain.bipedRightArm.rotationPointX = -5f;
		this.modelBipedMain.bipedRightArm.rotationPointY = 2;
		this.modelBipedMain.bipedRightArm.rotationPointZ = 0;
	}
}
