package net.dravigen.let_me_move.mixin;

import net.dravigen.dranimation_lib.animation.BaseAnimation;
import net.dravigen.dranimation_lib.interfaces.ICustomMovementEntity;
import net.dravigen.dranimation_lib.packet.PacketUtils;
import net.dravigen.dranimation_lib.utils.AnimationUtils;
import net.dravigen.dranimation_lib.utils.GeneralUtils;
import net.dravigen.dranimation_lib.utils.ModelPartHolder;
import net.dravigen.let_me_move.LetMeMoveAddon;
import net.dravigen.let_me_move.animation.player.poses.AnimStanding;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.HashMap;
import java.util.Map;

@Mixin(EntityLivingBase.class)
public abstract class EntityLivingBaseMixin extends Entity implements ICustomMovementEntity {
	
	@Unique
	private float leaningPitch;
	@Unique
	private float lastLeaningPitch;
	@Unique
	private ResourceLocation currentAnimation;
	@Unique
	private side side;
	@Unique
	private int jumpSwing = 1;
	@Unique
	private int prevJumpTime = -1;
	@Unique
	private int jumpTime = 0;
	@Unique
	private int timeRendered = 0;
	@Unique
	private Map<ResourceLocation, Integer> animationCooldowns = new HashMap<>();
	@Unique
	private boolean isOnGround = false;
	@Unique
	private ModelPartHolder partHolder = new ModelPartHolder();
	@Unique
	private boolean isFlying = false;
	@Unique
	private float[] renderRotOff = new float[]{0, 0, 0, 0};
	@Unique
	private float[] capeRot = new float[]{0, 0, 0};
	@Unique
	private float renderDelta = 1;
	@Unique
	private float[] limbSwing = new float[]{0, 0};
	@Unique
	private float[] prevLimbSwing = new float[]{0, 0};
	
	public EntityLivingBaseMixin(World par1World) {
		super(par1World);
	}
	
	@Shadow
	protected abstract float func_110146_f(float par1, float par2);
	
	@Shadow
	public abstract boolean attackEntityFrom(DamageSource par1DamageSource, float par2);
	
	@Shadow
	public float renderYawOffset;
	
	@Override
	public float lmm_$getLeaningPitch(float tickDelta) {
		return GeneralUtils.lerpF(tickDelta, this.lastLeaningPitch, this.leaningPitch);
	}
	
	@Override
	public void lmm_$setLeaningPitch(float pitch) {
		this.lastLeaningPitch = this.leaningPitch;
		this.leaningPitch = pitch;
	}
	
	@Override
	public ResourceLocation lmm_$getAnimationID() {
		if ((EntityLivingBase) (Object) this instanceof EntityPlayer) {
			return this.currentAnimation;
		}
		else return null;
	}
	
	@Override
	public BaseAnimation lmm_$getAnimation() {
		if ((EntityLivingBase) (Object) this instanceof EntityPlayer) {
			if (this.currentAnimation == null) this.currentAnimation = AnimStanding.id;
			
			return AnimationUtils.getAnimationFromID(this.currentAnimation);
		}
		else {
			return null;
		}
	}
	
	@Override
	public void lmm_$setAnimation(ResourceLocation ID) {
		if ((EntityLivingBase) (Object) this instanceof EntityPlayer player && this.currentAnimation != null) {
			
			if (this.worldObj.isRemote) {
				PacketUtils.animationCtoSSync(ID);
			}
			
			/*if (!ID.equals(this.currentAnimation)) {
				this.animationCooldowns.put(this.currentAnimation, this.lmm_$getAnimation().maxCooldown);
			}*/
			
			this.currentAnimation = ID;
			
			player.setData(LetMeMoveAddon.CURRENT_ANIMATION, String.valueOf(ID));
		}
	}
	
	@Override
	public void lmm_$setAnimationFromSync(ResourceLocation id) {
		this.currentAnimation = id;
	}
	
	@Override
	public boolean lmm_$isAnimation(ResourceLocation id) {
		if ((EntityLivingBase) (Object) this instanceof EntityPlayer) {
			if (this.currentAnimation == null) return false;
			
			return this.currentAnimation.equals(id);
		}
		else return false;
	}
	
	@Override
	public side lmm_$getSide() {
		return this.side;
	}
	
	@Override
	public float lmm_$getSideValue() {
		return side.equals(ICustomMovementEntity.side.LEFT) ? -1 : 1;
	}
	
	@Override
	public void lmm_$setSide(side side) {
		this.side = side;
	}
	
	@Override
	public void lmm_$setJumpTime(int jumpTime) {
		this.jumpTime = jumpTime;
	}
	
	@Override
	public int lmm_$getJumpTime() {
		return this.jumpTime;
	}
	
	@Override
	public void lmm_$setPrevJumpTime(int prevJumpTime) {
		this.prevJumpTime = prevJumpTime;
	}
	
	@Override
	public int lmm_$getPrevJumpTime() {
		return this.prevJumpTime;
	}
	
	@Override
	public void lmm_$setJumpSwing() {
		this.jumpSwing = -this.jumpSwing;
	}
	
	@Override
	public int lmm_$getJumpSwing() {
		return this.jumpSwing;
	}
	
	@Override
	public void lmm_$setTimeRendered(int time) {
		this.timeRendered = time;
	}
	
	@Override
	public int lmm_$getTimeRendered() {
		return this.timeRendered;
	}
	
	@Override
	public void lmm_$setCooldown(int time, ResourceLocation id) {
		this.animationCooldowns.put(id, time);
	}
	
	@Override
	public int lmm_$getCooldown(ResourceLocation id) {
		return this.animationCooldowns.getOrDefault(id, 0);
	}
	
	@Override
	public void lmm_$setOnGround(boolean onGround) {
		this.isOnGround = onGround;
	}
	
	@Override
	public boolean lmm_$getOnGround() {
		return this.isOnGround;
	}
	
	@Override
	public ModelPartHolder lmm_$getParHolder() {
		return this.partHolder;
	}
	
	@Override
	public Map<ResourceLocation, Integer> lmm_$getAllCooldown() {
		return animationCooldowns;
	}
	
	@Override
	public void lmm_$setIsFlying(boolean isFlying) {
		this.isFlying = isFlying;
	}
	
	@Override
	public boolean lmm_$getIsFlying() {
		return this.isFlying;
	}
	
	@Override
	public void lmm_$setRenderRotOff(float[] rotOff) {
		this.renderRotOff = rotOff;
	}
	
	@Override
	public float[] lmm_$getRenderRotOff() {
		return this.renderRotOff;
	}
	
	@Override
	public void lmm_$setCapeRot(int i, float v) {
		this.capeRot[i] = v;
	}
	
	@Override
	public float[] lmm_$getCapeRot() {
		return this.capeRot;
	}
	
	@Override
	public void lmm_$setDelta(float delta) {
		this.renderDelta = delta;
	}
	
	@Override
	public float lmm_$getDelta() {
		return this.renderDelta;
	}
	
	@Override
	public void lmm_$setLimbSwing(float[] floats) {
		this.limbSwing = floats;
	}
	
	@Override
	public float[] lmm_$getLimbSwing() {
		return limbSwing;
	}
	
	@Override
	public void lmm_$setPrevLimbSwing(float[] floats) {
		this.prevLimbSwing = floats;
	}
	
	@Override
	public float[] lmm_$getPrevLimbSwing() {
		return this.prevLimbSwing;
	}
	
	@Redirect(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityLivingBase;func_110146_f(FF)F"))
	private float disableHeadTurn(EntityLivingBase instance, float par1, float par2) {
		ICustomMovementEntity customEntity = (ICustomMovementEntity) instance;
		
		if (instance instanceof EntityPlayer player) {
			if (!customEntity.lmm_$getAnimation().customBodyHeadRotation(instance)) {
				if (this.isRiding()) {
					float var4 = MathHelper.wrapAngleTo180_float(this.rotationYaw - this.renderYawOffset);
					
					if (var4 < -45.0f) {
						player.renderYawOffset = player.rotationYaw + 45.0f;
					}
					if (var4 >= 45.0f) {
						player.renderYawOffset = player.rotationYaw - 45.0f;
					}
				}
				else {
					float var3 = MathHelper.wrapAngleTo180_float(par1 - this.renderYawOffset);
					this.renderYawOffset += var3 * 0.3f;
					float var4 = MathHelper.wrapAngleTo180_float(this.rotationYaw - this.renderYawOffset);
					boolean var5 = var4 < -90.0f || var4 >= 90.0f;
				
					if (var4 < -75.0f) {
						var4 = -75.0f;
					}
					if (var4 >= 75.0f) {
						var4 = 75.0f;
					}
					
					this.renderYawOffset = this.rotationYaw - var4;
					
					if (var4 * var4 > 75 * 75) {
						this.renderYawOffset += var4;
					}
					if (var5) {
						par2 *= -1.0f;
					}
				}
			}
			
			return par2;
		}
		
		return this.func_110146_f(par1, par2);
	}
}