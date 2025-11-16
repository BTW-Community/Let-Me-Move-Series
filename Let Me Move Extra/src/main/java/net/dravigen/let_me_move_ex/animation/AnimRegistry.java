package net.dravigen.let_me_move_ex.animation;

import net.dravigen.dranimation_lib.animation.BaseAnimation;
import net.dravigen.let_me_move_ex.animation.player.actions.*;

import static net.dravigen.dranimation_lib.utils.AnimationUtils.registerAnimation;

public class AnimRegistry {
	public final static BaseAnimation SWIMMING = new AnimSwimming();
	public final static BaseAnimation DIVING = new AnimDiving();
	public final static BaseAnimation SKY_DIVING = new AnimSkyDiving();
	public final static BaseAnimation DASHING = new AnimDashing();
	public final static BaseAnimation ROLLING = new AnimRolling();
	public final static BaseAnimation WALL_SLIDING = new AnimWallSliding();
	public final static BaseAnimation PULLING_UP = new AnimPullingUp();
	public final static BaseAnimation CRAWLING = new AnimCrawling();
	
	/**
	 * Register the animations here, the higher the animation the higher the priority (will be checked first)
	 */
	public static void registerAllAnimation() {
		int i = 0;
		
		registerAnimation(CRAWLING, i += 5);
		
		registerAnimation(SWIMMING, i += 5);
		
		registerAnimation(DASHING, i += 5);
		
		registerAnimation(ROLLING, i += 5);
		
		registerAnimation(PULLING_UP, i += 5);
		
		registerAnimation(WALL_SLIDING, i += 5);
		
		registerAnimation(DIVING, i += 5);
		
		registerAnimation(SKY_DIVING, i += 5);
	}
}
