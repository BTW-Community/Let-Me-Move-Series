package net.dravigen.dranimation_lib.mixin;

import net.dravigen.dranimation_lib.DraNimationLibAddon;
import net.dravigen.dranimation_lib.utils.AnimationUtils;
import net.minecraft.src.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
	
	@Unique
	long prevTime = 0;
	
	@Inject(method = "runGameLoop", at = @At("HEAD"))
	private void updateDelta(CallbackInfo ci) {
		long t = System.currentTimeMillis();
		float delta = (t - this.prevTime) / 25f;
		this.prevTime = t;
		
		delta = delta > 8 ? 8 : delta;
		
		AnimationUtils.delta = delta;
	}
	
	@Inject(method = "shutdownMinecraftApplet", at = @At("HEAD"))
	private void saveSettings(CallbackInfo ci) {
		DraNimationLibAddon.settingsManager.save();
	}
}
