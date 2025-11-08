package net.dravigen.let_me_move_ex.mixin.client;

import net.dravigen.dr_api_gen.animation.BaseAnimation;
import net.dravigen.dr_api_gen.interfaces.ICustomMovementEntity;
import net.dravigen.dr_api_gen.packet.PacketUtils;
import net.dravigen.dr_api_gen.utils.AnimationUtils;
import net.dravigen.dr_api_gen.utils.GeneralUtils;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.dravigen.let_me_move_ex.animation.AnimRegistry.*;
import static net.dravigen.let_me_move.animation.AnimRegistry.*;

@Mixin(EntityPlayerSP.class)
public abstract class EntityPlayerSPMixin extends AbstractClientPlayer {
	
	public EntityPlayerSPMixin(World par1World, String par2Str) {
		super(par1World, par2Str);
	}
	
	@Inject(method = "onLivingUpdate", at = @At(value = "HEAD"))
	private void updateAnimation(CallbackInfo ci) {
		if (this.sleeping) return;
		
		ICustomMovementEntity customPlayer = (ICustomMovementEntity) this;
		
		customPlayer.lmm_$getAnimation().updateLeaning(this);
		
		if (customPlayer.lmm_$getTimeRendered() == Integer.MAX_VALUE) {
			customPlayer.lmm_$setTimeRendered(0);
		}
		
		for (BaseAnimation animation : AnimationUtils.getAnimationsMap().values()) {
			animation.updateAnimationTime(customPlayer.lmm_$getAnimationID(), this);
		}
		
		if (Minecraft.getMinecraft().gameSettings.keyBindLeft.pressed) {
			customPlayer.lmm_$setSide(ICustomMovementEntity.side.LEFT);
		}
		else if (Minecraft.getMinecraft().gameSettings.keyBindRight.pressed) {
			customPlayer.lmm_$setSide(ICustomMovementEntity.side.RIGHT);
		}
		
		ResourceLocation newID = new ResourceLocation("");
		
		if (!customPlayer.lmm_$getAnimation().hasCooldown() ||
				customPlayer.lmm_$getAnimation().hasCooldown() &&
						customPlayer.lmm_$getTimeRendered() >= customPlayer.lmm_$getAnimation().totalDuration) {
			for (BaseAnimation animationb : AnimationUtils.getAnimationsMap().values()) {
				if (!animationb.shouldActivateAnimation(this, this.boundingBox)) continue;
				if (animationb.isGeneralConditonsMet(this, this.boundingBox)) {
					newID = animationb.getID();
					
					break;
				}
			}
			
			newID = newID.equals(new ResourceLocation("")) ? STANDING.getID() : newID;
			
			AxisAlignedBB bounds = new AxisAlignedBB(this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.minY + customPlayer.lmm_$getAnimation().height, this.boundingBox.maxZ);
			
			boolean noCollisionWithBlock = this.worldObj.getCollidingBoundingBoxes(this, bounds).isEmpty();
			
			if (!newID.equals(customPlayer.lmm_$getAnimationID())) {
				BaseAnimation newAnimation = AnimationUtils.getAnimationFromID(newID);
				float dHeight = newAnimation.height - customPlayer.lmm_$getAnimation().height;
				
				if (dHeight > 0) {
					noCollisionWithBlock = this.worldObj.getCollidingBoundingBoxes(this, bounds.addCoord(0, dHeight, 0))
							.isEmpty();
				}
				
				if (noCollisionWithBlock) {
					customPlayer.lmm_$setAnimation(newID);
				}
				else {
					if (!GeneralUtils.isEntityHeadInsideBlock(this, 0.41)) {
						customPlayer.lmm_$setAnimation(CROUCHING.getID());
					}
					else {
						customPlayer.lmm_$setAnimation(CRAWLING.getID());
					}
				}
			}
			else if (!this.worldObj.getCollidingBlockBounds(this.boundingBox).isEmpty() &&
					!GeneralUtils.isEntityFeetInsideBlock(this)) {
				if (this.capabilities.isFlying) {
					customPlayer.lmm_$setAnimation(DIVING.getID());
				}
				else {
					customPlayer.lmm_$setAnimation(CRAWLING.getID());
				}
			}
		}
		
		customPlayer.lmm_$setOnGround(this.onGround);
		customPlayer.lmm_$setIsFlying(this.capabilities.isFlying);
		
		PacketUtils.sendAnimationDataToServer((EntityPlayerSP)(Object) this);
	}
	
	@Redirect(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityPlayerSP;isSneaking()Z"))
	private boolean disableSprintOnCrawl(EntityPlayerSP instance) {
		ICustomMovementEntity customEntity = (ICustomMovementEntity) instance;
		if (instance.isSneaking() ||
				!(customEntity.lmm_$isAnimation(RUNNING.getID()) ||
						customEntity.lmm_$isAnimation(WALKING.getID()) ||
						customEntity.lmm_$isAnimation(STANDING.getID()) ||
						customEntity.lmm_$isAnimation(SWIMMING.getID()) ||
						customEntity.lmm_$isAnimation(LOW_FALLING.getID()))) {
			instance.setSprinting(false);
			
			return true;
		}
		
		return false;
	}
	
	@Redirect(method = "pushOutOfBlocks", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityPlayerSP;isBlockTranslucent(III)Z", ordinal = 0))
	private boolean customCollisionOne(EntityPlayerSP instance, int par1, int par2, int par3) {
		return !instance.worldObj.getCollidingBoundingBoxes(instance, instance.boundingBox).isEmpty();
	}
	
	@Redirect(method = "pushOutOfBlocks", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityPlayerSP;isBlockTranslucent(III)Z", ordinal = 1))
	private boolean disableUselessCheck(EntityPlayerSP instance, int par1, int par2, int par3) {
		return false;
	}
}
