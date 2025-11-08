package net.dravigen.let_me_move_ex.mixin.client.render;

import net.dravigen.let_me_move_ex.animation.AnimRegistry;
import net.dravigen.dr_api_gen.interfaces.ICustomMovementEntity;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {
	
	@Shadow
	private Minecraft mc;
	
	@Inject(method = "setupViewBobbing", at = @At("HEAD"), cancellable = true)
	private void disableBobbingWhileFastSwim(float par1, CallbackInfo ci) {
		if (((ICustomMovementEntity) this.mc.thePlayer).lmm_$isAnimation(AnimRegistry.SWIMMING.getID())) {
			ci.cancel();
		}
	}
}
