package net.dravigen.let_me_move.mixin.server;

import net.dravigen.dr_api_gen.packet.PacketUtils;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayerMP.class)
public abstract class EntityPlayerMPMixin extends EntityPlayer {
	
	public EntityPlayerMPMixin(World par1World, String par2Str) {
		super(par1World, par2Str);
	}
	
	@Inject(method = "onUpdate", at = @At("HEAD"))
	private void syncTrackingPlayersAnimation(CallbackInfo ci) {
		PacketUtils.sendAnimationDataToTrackingPlayer((EntityPlayerMP) (Object)this);
	}
	
	@Inject(method = "getEyeHeight", at = @At("RETURN"), cancellable = true)
	private void customEyeHeight(CallbackInfoReturnable<Float> cir) {
		if (this.sleeping) {
			cir.setReturnValue(0.18f);
		}
		
		cir.setReturnValue(1.62f - (1.62f - (this.height - 0.18f)));
	}
}
