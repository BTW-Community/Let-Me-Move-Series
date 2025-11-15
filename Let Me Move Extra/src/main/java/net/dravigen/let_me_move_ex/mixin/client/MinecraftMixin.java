package net.dravigen.let_me_move_ex.mixin.client;

import net.dravigen.dranimation_lib.interfaces.ICustomMovementEntity;
import net.dravigen.dranimation_lib.utils.AnimationUtils;
import net.dravigen.dranimation_lib.utils.GeneralUtils;
import net.dravigen.let_me_move_ex.animation.player.actions.AnimRolling;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
	
	@Unique
	private static float prevPitch;
	@Shadow
	public WorldClient theWorld;
	@Shadow
	public EntityClientPlayerMP thePlayer;
	@Shadow
	public GameSettings gameSettings;
	@Shadow
	private boolean isGamePaused;
	
	@Shadow public abstract Timer getTimer();
	
	@Inject(method = "runGameLoop", at = @At(value = "HEAD"))
	private void updateRender(CallbackInfo ci) {
		if (this.theWorld == null && AnimationUtils.extraIsPresent) {
			AnimationUtils.extraIsPresent = false;
		}
		
		if (!AnimationUtils.extraIsPresent) return;
		
		EntityPlayer player = this.thePlayer;
		ICustomMovementEntity customPlayer = (ICustomMovementEntity) player;
		
		if (!this.isGamePaused && player != null) {
			player.yOffset = GeneralUtils.incrementUntilGoal(player.yOffset, player.height - 0.18f, 0.4f * AnimationUtils.delta);
			
			if (customPlayer.lmm_$isAnimation(AnimRolling.id) && this.gameSettings.thirdPersonView == 0) {
				float leaning = customPlayer.lmm_$getLeaningPitch(this.getTimer().renderPartialTicks);
				
				if (leaning == 0) {
					prevPitch = player.cameraPitch;
				}
				else if (leaning < 4) {
					player.cameraPitch = (prevPitch + leaning * 90) % 360;
				}
				else {
					player.cameraPitch = 0;
					player.prevCameraPitch = player.cameraPitch;
				}
			}
			
			float yaw = (player.renderYawOffset) % (360);
			
			yaw = yaw < 0 ? 360 + yaw : yaw;
			
			player.renderYawOffset = yaw;
		}
	}
}
