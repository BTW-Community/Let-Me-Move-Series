package net.dravigen.let_me_move.animation;

import net.dravigen.dranimation_lib.animation.BaseAnimation;
import net.dravigen.let_me_move.animation.player.poses.*;

import static net.dravigen.dranimation_lib.utils.AnimationUtils.registerAnimation;

public class AnimRegistry {
	public final static BaseAnimation GUI = new AnimGui();
	public final static BaseAnimation CLIMBING = new AnimClimbing();
	public final static BaseAnimation HIGH_FALLING = new AnimHighFalling();
	public final static BaseAnimation LOW_FALLING = new AnimLowFalling();
	public final static BaseAnimation CROUCHING = new AnimCrouching();
	public final static BaseAnimation STANDING = new AnimStanding();
	public final static BaseAnimation WALKING = new AnimWalking();
	public final static BaseAnimation RUNNING = new AnimRunning();
	public final static BaseAnimation RIDING = new AnimRiding();
	public final static BaseAnimation FLOATING = new AnimFloating();
	public final static BaseAnimation SLEEPING = new AnimSleeping();
	
	/**
	 * Register the animations here, the higher the animation the higher the priority (will be checked first)
	 */
	public static void registerAllAnimation() {
		int i = 500;
		
		registerAnimation(SLEEPING, i += 5);
		
		registerAnimation(RIDING, i += 5);
		
		registerAnimation(FLOATING, i += 5);
		
		registerAnimation(LOW_FALLING, i += 5);
		
		registerAnimation(HIGH_FALLING, i += 5);
		
		registerAnimation(GUI, i += 5);
		
		registerAnimation(CLIMBING, i += 5);
		
		registerAnimation(CROUCHING, i += 5);
		
		registerAnimation(RUNNING, i += 5);
		
		registerAnimation(WALKING, i += 5);
		
		registerAnimation(STANDING, i += 5);
	}
}
