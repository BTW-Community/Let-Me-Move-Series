package net.dravigen.let_me_move.mixin.client;

import net.dravigen.dr_api_gen.animation.BaseAnimation;
import net.dravigen.dr_api_gen.interfaces.ICustomMovementEntity;
import net.dravigen.dr_api_gen.packet.PacketUtils;
import net.dravigen.dr_api_gen.utils.AnimationUtils;
import net.dravigen.dr_api_gen.utils.GeneralUtils;
import net.dravigen.let_me_move.LetMeMoveAddon;
import net.minecraft.src.*;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.dravigen.let_me_move.animation.AnimRegistry.*;

@Mixin(EntityPlayerSP.class)
public abstract class EntityPlayerSPMixin extends AbstractClientPlayer {
	
	public EntityPlayerSPMixin(World par1World, String par2Str) {
		super(par1World, par2Str);
	}
	
	@Inject(method = "onLivingUpdate", at = @At("HEAD"))
	private void updateAnimation(CallbackInfo ci) {
		if (LetMeMoveAddon.isExtraLoaded()) return;
		if (this.sleeping) return;
		
		ICustomMovementEntity customPlayer = (ICustomMovementEntity) this;
		
		customPlayer.lmm_$getAnimation().updateLeaning(this);
		
		if (Minecraft.getMinecraft().gameSettings.keyBindLeft.pressed) {
			customPlayer.lmm_$setSide(ICustomMovementEntity.side.LEFT);
		}
		else if (Minecraft.getMinecraft().gameSettings.keyBindRight.pressed) {
			customPlayer.lmm_$setSide(ICustomMovementEntity.side.RIGHT);
		}
		
		ResourceLocation newID = new ResourceLocation("");
		
		for (BaseAnimation animationb : AnimationUtils.getAnimationsMap().values()) {
			if (!animationb.shouldActivateAnimation(this, this.boundingBox)) continue;
			if (animationb.isGeneralConditonsMet(this, this.boundingBox)) {
				newID = animationb.getID();
				
				break;
			}
		}
		
		newID = newID.equals(new ResourceLocation("")) ? STANDING.getID() : newID;
		
		if (!newID.equals(customPlayer.lmm_$getAnimationID())) {
			customPlayer.lmm_$setAnimation(newID);
		}
		
		customPlayer.lmm_$setOnGround(this.onGround);
		customPlayer.lmm_$setIsFlying(this.capabilities.isFlying);
		
		PacketUtils.sendAnimationDataToServer((EntityPlayerSP)(Object) this);
	}
	
	@Redirect(method = "onLivingUpdate", at = @At(value = "FIELD", target = "Lnet/minecraft/src/MovementInput;sneak:Z", ordinal = 0, opcode = Opcodes.GETFIELD))
	private boolean disableVanillaSneakLowerCamera(MovementInput instance) {
		return ((ICustomMovementEntity) this).lmm_$isAnimation(CROUCHING.getID());
	}
}
