package net.dravigen.let_me_see.mixin;

import net.dravigen.let_me_see.LetMeSeeAddon;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RenderGlobal.class)
public abstract class RenderGlobalMixin {
	
	@Shadow
	private Minecraft mc;
	
	@Inject(method = "renderEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Profiler;endStartSection(Ljava/lang/String;)V", ordinal = 2))
	private void renderPlayerIn1stPerson(Vec3 par1Vec3, ICamera par2ICamera, float par3, CallbackInfo ci) {
		if (LetMeSeeAddon.isIsCustomFirstPerson(mc)) {
			RenderManager.instance.renderEntity(this.mc.thePlayer, par3);
			
		}
		
		LetMeSeeAddon.isPlayerRenderedByFreeCam = false;
	}
	
	@Redirect(method = "renderEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderManager;renderEntity(Lnet/minecraft/src/Entity;F)V"))
	private void isPlayerRendered(RenderManager instance, Entity par1Entity, float par2) {
		if (par1Entity == this.mc.thePlayer) {
			LetMeSeeAddon.isPlayerRenderedByFreeCam = true;
		}
		
		instance.renderEntity(par1Entity, par2);
	}
}
