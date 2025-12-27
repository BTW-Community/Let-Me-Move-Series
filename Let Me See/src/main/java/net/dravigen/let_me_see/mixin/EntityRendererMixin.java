package net.dravigen.let_me_see.mixin;

import api.item.items.ProgressiveCraftingItem;
import btw.item.items.FoodItem;
import net.dravigen.dranimation_lib.utils.AnimationUtils;
import net.dravigen.dranimation_lib.utils.GeneralUtils;
import net.dravigen.let_me_see.LetMeSeeAddon;
import net.dravigen.let_me_see.config.LMS_Settings;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {
	@Unique
	float bobbing = 0;
	@Unique
	float jumpAngle = 0;
	@Unique
	float frowAngle = 0;
	@Unique
	float prevYaw = 0;
	@Shadow
	private Minecraft mc;
	
	@Unique
	private static float incrementUntilGoal(float currentValue, float goalValue, float easeFactor) {
		float difference = goalValue - currentValue;
		
		float stepSize = difference * easeFactor;
		
		return currentValue + stepSize;
	}
	
	@Redirect(method = "setupViewBobbing", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glRotatef(FFFF)V", ordinal = 0))
	private void customBobbingRoll(float angle, float x, float y, float z) {
		if (mc.gameSettings.thirdPersonView == 0) {
			
			float delta = AnimationUtils.delta;
			
			EntityClientPlayerMP player = mc.thePlayer;
			
			float strafingMul = (float) LMS_Settings.STRAFING_MULTIPLIER.getDouble();
			float cameraMul = (float) LMS_Settings.CAMERA_MULTIPLIER.getDouble();
			float eatingMul = (float) LMS_Settings.EATING_MULTIPLIER.getDouble();
			float swingMul = (float) LMS_Settings.SWING_MULTIPLIER.getDouble();
			
			ItemStack heldItem = player.getHeldItem();
			
			boolean isEating = player.isEating() && heldItem != null && (heldItem.getItem() instanceof FoodItem ||
					heldItem.getItem() instanceof ItemFood ||
					heldItem.getItem() instanceof ItemPotion ||
					heldItem.getItem() instanceof ProgressiveCraftingItem);
			
			float goal = (float) ((player.moveStrafing != 0 ? -1.5 * Math.pow(player.moveStrafing, 3) : 0) *
					strafingMul +
					1.5 * swingMul *
							GeneralUtils.lerpF(mc.getTimer().renderPartialTicks,
											   player.prevSwingProgress,
											   player.swingProgress) +
					(0.5 * (player.rotationYaw - prevYaw)) * cameraMul +
					(isEating ? 1.5f * eatingMul * GeneralUtils.cos(player.ticksExisted) : 0));
			
			float factor = player.moveStrafing == 0 || player.rotationYaw - prevYaw == 0 ? 0.25f : 0.005f;
			
			prevYaw = player.rotationYaw;
			
			bobbing = MathHelper.clamp_float(incrementUntilGoal(bobbing, goal, delta * factor), -10, 10);
			
			float mul = (float) LMS_Settings.BOBBING_MULTIPLIER.getDouble();
			
			GL11.glRotatef(angle * 3f * mul + bobbing, x, y, z);
		}
	}
	
	@Redirect(method = "setupViewBobbing", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glRotatef(FFFF)V", ordinal = 1))
	private void customBobbingPitch(float angle, float x, float y, float z) {
		if (mc.gameSettings.thirdPersonView == 0) {
			float delta = AnimationUtils.delta;
			
			EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
			
			float goal = player.moveForward != 0 ? player.isSprinting()
												   ? 3.75f * player.moveForward
												   : 2.5f * player.moveForward : 0;
			float jumpGoal = 8 * (float) (player.posY - player.prevPosY);
			
			boolean still = player.moveForward == 0 || player.motionY == 0;
			
			float factor1 = still ? 0.2f : 0.04f;
			float factor2 = 0.4f;
			
			frowAngle = MathHelper.clamp_float(incrementUntilGoal(frowAngle, goal, delta * factor1), -5, 10);
			jumpAngle = MathHelper.clamp_float(incrementUntilGoal(jumpAngle, jumpGoal, delta * factor2), -5, 8);
			
			float mul = (float) LMS_Settings.BOBBING_MULTIPLIER.getDouble();
			float forwardMul = (float) LMS_Settings.FORWARD_MULTIPLIER.getDouble();
			float jumpMul = (float) LMS_Settings.JUMP_MULTIPLIER.getDouble();
			
			GL11.glRotatef(angle * 2f * mul + frowAngle * forwardMul, x, y, z);
			GL11.glRotatef(jumpAngle * jumpMul, x, y, z);
		}
	}
	
	@Inject(method = "renderHand", at = @At("HEAD"), cancellable = true)
	private void disable1stPersonHandUnlessHoldingMap(float par1, int par2, CallbackInfo ci) {
		ItemStack item = mc.thePlayer.getHeldItem();
		
		boolean holdingSpecialItem = item != null && item.itemID == Item.map.itemID;
		
		if (LetMeSeeAddon.isIsCustomFirstPerson(mc) && !holdingSpecialItem) {
			ci.cancel();
		}
	}
}
