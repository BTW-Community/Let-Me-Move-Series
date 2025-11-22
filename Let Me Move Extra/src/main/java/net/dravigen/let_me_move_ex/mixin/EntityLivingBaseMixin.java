package net.dravigen.let_me_move_ex.mixin;

import net.dravigen.dranimation_lib.interfaces.ICustomMovementEntity;
import net.dravigen.dranimation_lib.utils.AnimationUtils;
import net.dravigen.let_me_move.animation.player.poses.AnimHighFalling;
import net.dravigen.let_me_move_ex.animation.player.actions.AnimDiving;
import net.dravigen.let_me_move_ex.animation.player.actions.AnimRolling;
import net.dravigen.let_me_move_ex.animation.player.actions.AnimSkyDiving;
import net.dravigen.let_me_move_ex.animation.player.actions.AnimWallSliding;
import net.minecraft.src.*;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityLivingBase.class)
public abstract class EntityLivingBaseMixin extends Entity implements ICustomMovementEntity {
	
	@Shadow
	public float moveForward;
	
	public EntityLivingBaseMixin(World par1World) {
		super(par1World);
	}
	
	@Shadow
	public abstract boolean attackEntityFrom(DamageSource par1DamageSource, float par2);
	
	@Inject(method = "getSpeedModifier", at = @At("RETURN"), cancellable = true)
	private void applyAnimationSpeedModifier(CallbackInfoReturnable<Float> cir) {
		if (!AnimationUtils.extraIsPresent) return;
		
		if (this.lmm_$getAnimation() == null) return;
		
		cir.setReturnValue(cir.getReturnValueF() * this.lmm_$getAnimation().speedModifier);
	}
	
	@ModifyArg(method = "moveEntityWithHeading", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityLivingBase;moveFlying(FFF)V", ordinal = 2), index = 2)
	private float customFallSpeed(float par1) {
		if (this.lmm_$getAnimation() == null) return par1;
		
		if ((EntityLivingBase) (Object) this instanceof EntityPlayer player && !player.capabilities.isFlying) {
			if (this.lmm_$isAnimation(AnimSkyDiving.id)) {
				if (this.moveForward == 0) {
					this.motionY *= 0.96;
				}
				else {
					this.motionY *= 0.98;
				}
				
				return this.lmm_$getAnimation().speedModifier;
			}
			else if (this.lmm_$isAnimation(AnimHighFalling.id)) {
				this.motionY *= 0.98;
				
				return this.lmm_$getAnimation().speedModifier;
			}
			else if (this.lmm_$isAnimation(AnimDiving.id)) {
				if (this.motionY < 0) {
					this.motionY *= 1.02;
				}
				
				return this.lmm_$getAnimation().speedModifier;
			}
			else if (this.lmm_$isAnimation(AnimWallSliding.id)) {
				this.motionY *= 0.85;
				
				if (fallDistance > 4) {
					this.fallDistance *= 0.85f;
				}
				
				if (fallDistance > 6) {
					if (this.attackEntityFrom(DamageSource.generic, (float) (2 * -this.motionY))) {
						this.hurtResistantTime -= 8;
					}
				}
			}
		}
		
		return par1;
	}
	
	@Redirect(method = "onLivingUpdate", at = @At(value = "FIELD", target = "Lnet/minecraft/src/EntityLivingBase;onGround:Z", opcode = Opcodes.GETFIELD))
	private boolean allowJumpWhileWallSliding(EntityLivingBase instance) {
		if (((ICustomMovementEntity) instance).lmm_$isAnimation(AnimWallSliding.id)) {
			return true;
		}
		
		return instance.onGround;
	}
	
	@ModifyArg(method = "entityLivingBaseFall", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityLivingBase;attackEntityFrom(Lnet/minecraft/src/DamageSource;F)Z"), index = 1)
	private float lessFallDamageIfRolling(float damage) {
		if ((EntityLivingBase) (Object) this instanceof EntityPlayer) {
			int t = this.lmm_$getTimeRendered();
			
			if (this.lmm_$isAnimation(AnimRolling.id) && t >= 10 && t <= 20) {
				return damage * 0.85f;
			}
		}
		
		return damage;
	}
}