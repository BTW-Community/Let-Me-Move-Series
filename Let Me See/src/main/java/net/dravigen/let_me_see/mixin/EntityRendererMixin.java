package net.dravigen.let_me_see.mixin;

import net.dravigen.dranimation_lib.DraNimationLibAddon;
import net.dravigen.dranimation_lib.utils.AnimationUtils;
import net.dravigen.let_me_see.config.LmsSettings;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.dravigen.dranimation_lib.utils.GeneralUtils.incrementUntilGoal;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {
	@Unique
	float strafAngle = 0;
	@Unique
	float jumpAngle = 0;
	@Unique
	float frowAngle = 0;
	@Unique
	float prevYaw = 0;
	@Shadow
	private Minecraft mc;
	
	@Redirect(method = "setupViewBobbing", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glRotatef(FFFF)V", ordinal = 0))
	private void customBobbingRoll(float angle, float x, float y, float z) {
		if (mc.gameSettings.thirdPersonView == 0) {
			
			float delta = AnimationUtils.delta;
			
			EntityClientPlayerMP player = mc.thePlayer;
			
			float strafingMul = (float) LmsSettings.STRAFING_MULTIPLIER.getDouble();
			float cameraMul = (float) LmsSettings.CAMERA_MULTIPLIER.getDouble();

			float goal = (player.moveStrafing != 0 ? (float) (-2.5f * Math.pow(player.moveStrafing, 3)) : 0) * strafingMul +
								(1.25f * (player.rotationYaw - prevYaw)) * cameraMul;
			
			float factor = player.moveStrafing == 0 || player.rotationYaw - prevYaw == 0 ? 0.15f : 0.005f;
			
			prevYaw = player.rotationYaw;
			
			strafAngle = MathHelper.clamp_float(incrementUntilGoal(strafAngle, goal, delta * factor), -10, 10);
			
			float mul = (float) LmsSettings.BOBBING_MULTIPLIER.getDouble();
			
			GL11.glRotatef(angle * Math.max(0, 3f * mul) + strafAngle, x, y, z);
		}
	}
	
	@Redirect(method = "setupViewBobbing", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glRotatef(FFFF)V", ordinal = 1))
	private void customBobbingPitch(float angle, float x, float y, float z) {
		if (mc.gameSettings.thirdPersonView == 0) {
			
			float delta = AnimationUtils.delta;
			
			EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
			
			float goal = player.moveForward != 0 ? player.isSprinting() ? 5 * player.moveForward : 3 * player.moveForward : 0;
			
			boolean still = player.moveForward == 0 || player.motionY == 0;
			
			float factor1 = still ? 0.2f : 0.04f;
			float factor2 = 0.4f;
			
			frowAngle = MathHelper.clamp_float(incrementUntilGoal(frowAngle, goal, delta * factor1), -5, 10);
			jumpAngle = MathHelper.clamp_float(incrementUntilGoal(jumpAngle,
																  12 * (float) player.motionY,
																  delta * factor2), -5, 8);
			
			float mul = (float) LmsSettings.BOBBING_MULTIPLIER.getDouble();
			float forwardMul = (float) LmsSettings.FORWARD_MULTIPLIER.getDouble();
			float jumpMul = (float) LmsSettings.JUMP_MULTIPLIER.getDouble();
			
			GL11.glRotatef(angle * Math.max(0, 2f * mul) + frowAngle * forwardMul, x, y, z);
			GL11.glRotatef(jumpAngle * jumpMul, x, y, z);
		}
	}
	
	@Inject(method = "renderHand", at = @At("HEAD"), cancellable = true)
	private void disable1stPersonHandUnlessHoldingMap(float par1, int par2, CallbackInfo ci) {
		ItemStack item = mc.thePlayer.getHeldItem();
		
		boolean holdingSpecialItem = item != null && item.itemID == Item.map.itemID;
		
		if (DraNimationLibAddon.settingsManager.getBoolean(LmsSettings.firstPersonModelID) && !holdingSpecialItem) {
			ci.cancel();
		}
	}
}
