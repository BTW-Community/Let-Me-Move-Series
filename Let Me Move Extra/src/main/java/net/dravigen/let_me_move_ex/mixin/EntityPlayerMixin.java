package net.dravigen.let_me_move_ex.mixin;

import net.dravigen.dr_api_gen.animation.BaseAnimation;
import net.dravigen.dr_api_gen.interfaces.ICustomMovementEntity;
import net.dravigen.dr_api_gen.packet.PacketUtils;
import net.dravigen.dr_api_gen.utils.GeneralUtils;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.dravigen.let_me_move_ex.animation.AnimRegistry.*;

@Mixin(EntityPlayer.class)
public abstract class EntityPlayerMixin extends EntityLivingBase {
	
	@Shadow
	protected boolean sleeping;
	
	public EntityPlayerMixin(World par1World) {
		super(par1World);
	}
	
	@Shadow
	public abstract float getEyeHeight();
	
	@Shadow
	public abstract boolean canJump();
	
	@Shadow
	public abstract boolean canSwim();
	
	@Shadow
	public abstract void addMovementStat(double par1, double par3, double par5);
	
	@Inject(method = "onUpdate", at = @At("HEAD"))
	private void updateAnimation(CallbackInfo ci) {
		if (this.sleeping) return;
		
		ICustomMovementEntity customPlayer = (ICustomMovementEntity) this;
		
		BaseAnimation currentAnimation = customPlayer.lmm_$getAnimation();
		
		this.setSize(0.6f, currentAnimation.height);
	}
	
	@Inject(method = "moveEntityWithHeading", at = @At("HEAD"), cancellable = true)
	private void handleCustomMove(CallbackInfo ci) {
		ICustomMovementEntity customPlayer = (ICustomMovementEntity) this;
		double prevX = this.posX;
		double prevY = this.posY;
		double prevZ = this.posZ;
		
		BaseAnimation animation = customPlayer.lmm_$getAnimation();
		
		if (animation == null) return;
		
		boolean shouldDisableBaseMove = animation.getCustomMove((EntityPlayer) (Object) this);
		
		if (shouldDisableBaseMove) {
			ci.cancel();
		}
		
		this.addMovementStat(this.posX - prevX, this.posY - prevY, this.posZ - prevZ);
	}
	
	@Inject(method = "addMovementStat", at = @At(value = "HEAD"), cancellable = true)
	private void customExhaustion(double distX, double distY, double distZ, CallbackInfo ci) {
		BaseAnimation animation = ((ICustomMovementEntity) this).lmm_$getAnimation();
		
		if (animation == null) return;
		
		boolean disableBaseHunger = animation.getHungerCost((EntityPlayer) (Object) this, distX, distY, distZ);
		
		if (disableBaseHunger) {
			ci.cancel();
		}
	}
	
	@Inject(method = "jump", at = @At("TAIL"))
	private void addHorizontalMotionFromJumpingOnWall(CallbackInfo ci) {
		if (((ICustomMovementEntity) this).lmm_$isAnimation(WALL_SLIDING.getID()) && this.canJump()) {
			GeneralUtils.coords side = GeneralUtils.getWallSide(this, 0, this.height);
			this.motionY += 0.15;
			this.motionX += side == GeneralUtils.coords.EAST ? -0.3f : side == GeneralUtils.coords.WEST ? 0.3f : 0;
			this.motionZ += side == GeneralUtils.coords.SOUTH ? -0.3f : side == GeneralUtils.coords.NORTH ? 0.3f : 0;
			PacketUtils.sendExhaustionToServer(1.33f * 0.25f);
		}
	}
}
