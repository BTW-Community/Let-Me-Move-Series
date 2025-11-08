package net.dravigen.let_me_move_ex.mixin.server;

import net.dravigen.let_me_move_ex.animation.AnimRegistry;
import net.dravigen.dr_api_gen.interfaces.ICustomMovementEntity;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(NetServerHandler.class)
public abstract class NetServerHandlerMixin extends NetHandler {
	
	@Redirect(method = "handleFlying", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityPlayerMP;addExhaustionForJump()V"))
	private void a(EntityPlayerMP instance) {
		if (!((ICustomMovementEntity) instance).lmm_$isAnimation(AnimRegistry.SWIMMING.getID())) {
			instance.addExhaustionForJump();
		}
	}
}
