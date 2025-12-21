package net.dravigen.let_me_move.mixin.client;

import net.dravigen.dranimation_lib.animation.BaseAnimation;
import net.dravigen.dranimation_lib.interfaces.ICustomMovementEntity;
import net.dravigen.dranimation_lib.packet.PacketUtils;
import net.dravigen.dranimation_lib.utils.AnimationUtils;
import net.dravigen.let_me_move.LetMeMoveAddon;
import net.dravigen.let_me_move.animation.player.poses.AnimCrouching;
import net.dravigen.let_me_move.animation.player.poses.AnimStanding;
import net.minecraft.src.*;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(EntityPlayerSP.class)
public abstract class EntityPlayerSPMixin extends AbstractClientPlayer {
	
	public EntityPlayerSPMixin(World par1World, String par2Str) {
		super(par1World, par2Str);
	}
	
	@Inject(method = "onLivingUpdate", at = @At("HEAD"))
	private void updateAnimation(CallbackInfo ci) {
		if (LetMeMoveAddon.isExtraLoaded()) return;
		
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
		
		newID = newID.equals(new ResourceLocation("")) ? AnimStanding.id : newID;
		
		if (!newID.equals(customPlayer.lmm_$getAnimationID())) {
			customPlayer.lmm_$setAnimation(newID);
		}
		
		if (!this.inWater && !this.isRiding() && !this.capabilities.isFlying) {
			if (customPlayer.lmm_$getOnGround() && !this.onGround) {
				customPlayer.lmm_$setJumpSwing();
			}
		}
		
		customPlayer.lmm_$setOnGround(this.onGround);
		customPlayer.lmm_$setIsFlying(this.capabilities.isFlying);
		
		PacketUtils.sendAnimationDataToServer((EntityPlayerSP) (Object) this);
	}
	
	@Redirect(method = "onLivingUpdate", at = @At(value = "FIELD", target = "Lnet/minecraft/src/MovementInput;sneak:Z", ordinal = 0, opcode = Opcodes.GETFIELD))
	private boolean disableVanillaSneakLowerCamera(MovementInput instance) {
		return ((ICustomMovementEntity) this).lmm_$isAnimation(AnimCrouching.id);
	}
}
