package net.dravigen.let_me_see.mixin;

import net.dravigen.dranimation_lib.utils.AnimationUtils;
import net.dravigen.let_me_see.LetMeSeeAddon;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.dravigen.dranimation_lib.utils.GeneralUtils.*;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {
	@Shadow private Minecraft mc;
	@Unique
	float strafAngle = 0;
	@Unique
	float jumpAngle = 0;
	@Unique
	float frowAngle = 0;
	@Unique
	float prevYaw = 0;
	
	@Redirect(method = "setupViewBobbing", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glRotatef(FFFF)V", ordinal = 0), remap = false)
	private void customBobbingRoll(float angle, float x, float y, float z) {
		if (mc.gameSettings.thirdPersonView == 0) {
			
			float delta = AnimationUtils.delta;
			float goal;
			
			EntityClientPlayerMP player = mc.thePlayer;
			
			if (player.moveStrafing != 0) {
				goal = -3 * player.moveStrafing * player.moveStrafing * player.moveStrafing;
			}
			else {
				goal = 1.5f * (player.rotationYaw - prevYaw);
			}
			
			float factor;
			
			if (player.moveStrafing == 0 || player.rotationYaw - prevYaw == 0) {
				factor = 0.15f;
			}
			else {
				factor = 0.005f;
			}
			
			prevYaw = player.rotationYaw;
			
			strafAngle = MathHelper.clamp_float(incrementUntilGoal(strafAngle, goal, delta * factor), -10, 10);
			
			GL11.glRotatef(angle * 3f + strafAngle, x, y, z);
		}
	}
	
	@Redirect(method = "setupViewBobbing", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glRotatef(FFFF)V", ordinal = 1), remap = false)
	private void customBobbingPitch(float angle, float x, float y, float z) {
		if (mc.gameSettings.thirdPersonView == 0) {
			
			float delta = AnimationUtils.delta;
			float goal = 0;
			
			EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
			
			if (player.moveForward != 0) {
				goal = 2 * player.moveForward;
			}
			
			float factor1;
			float factor2;
			
			if (player.moveForward == 0 || player.motionY == 0) {
				factor1 = 0.2f;
				factor2 = 0.4f;
			}
			else {
				factor1 = 0.04f;
				factor2 = 0.125f;
			}
			
			frowAngle = MathHelper.clamp_float(incrementUntilGoal(frowAngle, goal, delta * factor1), -5, 5);
			jumpAngle = MathHelper.clamp_float(incrementUntilGoal(jumpAngle,
																  8 * (float) player.motionY,
																  delta * factor2), -5, 5);
			
			GL11.glRotatef(angle * 2f + frowAngle, x, y, z);
			GL11.glRotatef(jumpAngle, x, y, z);
		}
	}
	
	@Inject(method = "renderHand", at = @At("HEAD"), cancellable = true)
	private void a(float par1, int par2, CallbackInfo ci) {
		ItemStack item = mc.thePlayer.getHeldItem();
		
		boolean holdingSpecialItem = item != null && item.itemID == Item.map.itemID;
		
		if (LetMeSeeAddon.enable3DFirstPerson && !holdingSpecialItem) {
			ci.cancel();
		}
	}
}
