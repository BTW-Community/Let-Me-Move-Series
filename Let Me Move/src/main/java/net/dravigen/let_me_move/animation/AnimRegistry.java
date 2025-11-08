package net.dravigen.let_me_move.animation;

import net.dravigen.dr_api_gen.animation.BaseAnimation;
import net.dravigen.let_me_move.animation.player.poses.*;

import static net.dravigen.dr_api_gen.utils.AnimationUtils.registerAnimation;

public class AnimRegistry {
	public final static BaseAnimation GUI = new AnimGui();
	public final static BaseAnimation CLIMBING = new AnimClimbing();
	public final static BaseAnimation HIGH_FALLING = new AnimHighFalling();
	public final static BaseAnimation LOW_FALLING = new AnimLowFalling();
	public final static BaseAnimation CROUCHING = new AnimCrouching();
	public final static BaseAnimation STANDING = new AnimStanding();
	public final static BaseAnimation WALKING = new AnimWalking();
	public final static BaseAnimation RUNNING = new AnimRunning();
	
	/**
	 * Register the animations here, the higher the animation the higher the priority (will be checked first)
	 */
	public static void registerAllAnimation() {
		//Poses
		registerAnimation(LOW_FALLING, 100);
		
		registerAnimation(HIGH_FALLING, 105);
		
		registerAnimation(GUI, 110);
		
		registerAnimation(CLIMBING, 115);
		
		registerAnimation(CROUCHING, 120);
		
		registerAnimation(RUNNING, 125);
		
		registerAnimation(WALKING, 130);
		
		registerAnimation(STANDING, 135);
	}
}
