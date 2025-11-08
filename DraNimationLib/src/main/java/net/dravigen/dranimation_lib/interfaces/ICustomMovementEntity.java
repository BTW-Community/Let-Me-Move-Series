package net.dravigen.dranimation_lib.interfaces;

import net.dravigen.dranimation_lib.animation.BaseAnimation;
import net.dravigen.dranimation_lib.utils.ModelPartHolder;
import net.minecraft.src.ResourceLocation;

import java.util.Map;

public interface ICustomMovementEntity {
	float lmm_$getLeaningPitch(float tickDelta);
	
	void lmm_$setLeaningPitch(float pitch);
	
	ResourceLocation lmm_$getAnimationID();
	
	BaseAnimation lmm_$getAnimation();
	
	void lmm_$setAnimation(ResourceLocation animation);
	
	void lmm_$setAnimationFromSync(ResourceLocation id);
	
	boolean lmm_$isAnimation(ResourceLocation id);
	
	side lmm_$getSide();
	
	float lmm_$getSideValue();
	
	void lmm_$setSide(side side);
	
	void lmm_$setJumpTime(int jumpTime);
	
	int lmm_$getJumpTime();
	
	void lmm_$setPrevJumpTime(int prevJumpTime);
	
	int lmm_$getPrevJumpTime();
	
	void lmm_$setJumpSwing(int jumpSwing);
	
	int lmm_$getJumpSwing();
	
	void lmm_$setTimeRendered(int time);
	
	int lmm_$getTimeRendered();
	
	void lmm_$setCooldown(int time, ResourceLocation id);
	
	int lmm_$getCooldown(ResourceLocation id);
	
	void lmm_$setOnGround(boolean onGround);
	
	boolean lmm_$getOnGround();
	
	ModelPartHolder lmm_$getParHolder();
	
	Map<ResourceLocation, Integer> lmm_$getAllCooldown();
	
	void lmm_$setIsFlying(boolean isFlying);
	
	boolean lmm_$getIsFlying();
	
	void lmm_$setRenderRotOff(float[] rotOff);
	
	float[] lmm_$getRenderRotOff();
	
	float[] lmm_$getCapeRot();
	
	void lmm_$setDelta(float delta);
	
	float lmm_$getDelta();
	
	enum side {
		LEFT,
		RIGHT
	}
}
