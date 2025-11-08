package net.dravigen.let_me_move_ex.animation;

import net.dravigen.dr_api_gen.animation.BaseAnimation;
import net.dravigen.let_me_move_ex.animation.player.actions.*;

import static net.dravigen.dr_api_gen.utils.AnimationUtils.registerAnimation;

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
		registerAnimation(CRAWLING, 5);
		
		registerAnimation(SWIMMING, 10);
		
		registerAnimation(DASHING, 15);
		
		registerAnimation(ROLLING, 20);
		
		registerAnimation(PULLING_UP, 25);
		
		registerAnimation(WALL_SLIDING, 30);
		
		registerAnimation(DIVING, 35);
		
		registerAnimation(SKY_DIVING, 40);
	}
}
