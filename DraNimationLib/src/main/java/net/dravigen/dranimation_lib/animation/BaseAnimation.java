package net.dravigen.dranimation_lib.animation;

import api.world.difficulty.DifficultyParam;
import net.dravigen.dranimation_lib.interfaces.ICustomMovementEntity;
import net.minecraft.src.*;

@SuppressWarnings("unused")
public abstract class BaseAnimation {
	
	public final float speedModifier;
	public final boolean needYOffsetUpdate;
	public final int maxCooldown;
	public final int totalDuration;
	public final boolean shouldAutoUpdate;
	private final ResourceLocation id;
	public float height;
	public float yOffset;
	public int priority;
	protected boolean movedOnce;
	
	public BaseAnimation(ResourceLocation id, float height, float speedModifier, boolean needYOffsetUpdate,
			int maxCooldown, int totalDuration, boolean shouldAutoUpdate, float yOffset) {
		this.id = id;
		this.height = height;
		this.speedModifier = speedModifier;
		this.needYOffsetUpdate = needYOffsetUpdate;
		this.maxCooldown = maxCooldown;
		this.totalDuration = totalDuration;
		this.shouldAutoUpdate = shouldAutoUpdate;
		this.yOffset = yOffset;
		this.priority = 0;
	}
	/*
	public BaseAnimation(ResourceLocation id, float height, float speedModifier, boolean needYOffsetUpdate,
			int maxCooldown, int duration, boolean shouldAutoUpdate) {
		this.id = id;
		this.height = height;
		this.speedModifier = speedModifier;
		this.needYOffsetUpdate = needYOffsetUpdate;
		this.maxCooldown = maxCooldown;
		this.duration = duration;
		this.shouldAutoUpdate = shouldAutoUpdate;
		this.yOffset = 0;
		this.priority = 0;
	}
	
	public BaseAnimation(ResourceLocation id, float height, float speedModifier, boolean needYOffsetUpdate,
			int maxCooldown, int duration) {
		this.id = id;
		this.height = height;
		this.speedModifier = speedModifier;
		this.needYOffsetUpdate = needYOffsetUpdate;
		this.maxCooldown = maxCooldown;
		this.duration = duration;
		this.shouldAutoUpdate = true;
		this.yOffset = 0;
		this.priority = 0;
	}
	
	public BaseAnimation(ResourceLocation id, float height, float speedModifier, boolean needYOffsetUpdate,
			int maxCooldown) {
		this.id = id;
		this.height = height;
		this.speedModifier = speedModifier;
		this.needYOffsetUpdate = needYOffsetUpdate;
		this.maxCooldown = maxCooldown;
		this.duration = 0;
		this.shouldAutoUpdate = true;
		this.yOffset = 0;
		this.priority = 0;
	}
	
	public BaseAnimation(ResourceLocation id, float height, float speedModifier, boolean needYOffsetUpdate) {
		this.id = id;
		this.height = height;
		this.speedModifier = speedModifier;
		this.needYOffsetUpdate = needYOffsetUpdate;
		this.maxCooldown = 0;
		this.duration = 0;
		this.shouldAutoUpdate = true;
		this.yOffset = 0;
		this.priority = 0;
	}
	
	public BaseAnimation(ResourceLocation id, float height, float speedModifier) {
		this.id = id;
		this.height = height;
		this.speedModifier = speedModifier;
		this.needYOffsetUpdate = false;
		this.maxCooldown = 0;
		this.duration = 0;
		this.shouldAutoUpdate = true;
		this.yOffset = 0;
		this.priority = 0;
	}*/
	
	public ResourceLocation getID() {
		return this.id;
	}
	
	public String getName(EntityPlayer player) {
		if (player.capabilities.isFlying && !player.isRiding()) {
			return StatCollector.translateToLocal("LMM.animation.flying");
		}
		
		return StatCollector.translateToLocal(this.id.getResourceDomain() + ".animation." + this.id.getResourcePath());
	}
	
	public boolean hasCooldown() {
		return this.maxCooldown != 0;
	}
	
	public void startCooldown(ICustomMovementEntity customPlayer) {
		customPlayer.lmm_$setCooldown(this.maxCooldown, id);
		customPlayer.lmm_$setTimeRendered(this.totalDuration);
	}
	
	public void updateAnimationTime(ResourceLocation currentAnimation, EntityLivingBase player) {
		ICustomMovementEntity customPlayer = (ICustomMovementEntity) player;
		
		if (currentAnimation.equals(this.id)) {
			if (this.hasCooldown()) {
				if (customPlayer.lmm_$getTimeRendered() < this.totalDuration) {
					customPlayer.lmm_$setTimeRendered(customPlayer.lmm_$getTimeRendered() + 1);
				}
				else {
					customPlayer.lmm_$setCooldown(this.maxCooldown, this.id);
				}
			}
			else {
				customPlayer.lmm_$setTimeRendered(0);
			}
		}
		
		int cooldown = customPlayer.lmm_$getCooldown(this.id);
		
		if (cooldown > 0) {
			customPlayer.lmm_$setCooldown(cooldown - 1, this.id);
		}
	}
	
	public boolean shouldActivateAnimation(EntityPlayer player, AxisAlignedBB axisAlignedBB) {
		if (!this.hasCooldown() || ((ICustomMovementEntity) player).lmm_$getCooldown(this.id) == 0) {
			return this.isActivationConditonsMet(player, axisAlignedBB);
		}
		else return false;
	}
	
	public abstract boolean isGeneralConditonsMet(EntityPlayer player, AxisAlignedBB axisAlignedBB);
	
	public abstract boolean isActivationConditonsMet(EntityPlayer player, AxisAlignedBB axisAlignedBB);
	
	protected float getHungerDifficultyMultiplier(EntityPlayer player) {
		return player.worldObj.getDifficultyParameter(DifficultyParam.HungerIntensiveActionCostMultiplier.class);
	}
	
	public abstract void renderAnimation(ModelBiped model, EntityLivingBase entity, float f, float g, float h, float i,
			float j, float u, float delta);
	
	public abstract void updateLeaning(EntityLivingBase entity);
	
	public abstract boolean getHungerCost(EntityPlayer player, double distX, double distY, double distZ);
	
	public abstract boolean getCustomMove(EntityPlayer player);
	
	public abstract boolean customBodyHeadRotation(EntityLivingBase entity);
}
