package net.dravigen.let_me_move_ex.animation.player.actions;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ResourceLocation;

import static net.dravigen.dr_api_gen.utils.GeneralUtils.isEntityHeadNormalHeightInsideBlock;
import static net.dravigen.dr_api_gen.utils.GeneralUtils.isInsideWater;
import static net.dravigen.let_me_move_ex.LetMeMoveExAddon.crawl_key;

public class AnimCrawling extends AnimSwimming {
	public static final ResourceLocation id = new ResourceLocation("LMM", "crawling");
	
	public AnimCrawling() {
		super(id);
	}
	
	@Override
	public boolean isGeneralConditonsMet(EntityPlayer player, AxisAlignedBB axisAlignedBB) {
		return !player.capabilities.isFlying && player.onGround && !isInsideWater(player) && !player.isOnLadder();
	}
	
	@Override
	public boolean isActivationConditonsMet(EntityPlayer player, AxisAlignedBB axisAlignedBB) {
		return crawl_key.pressed || isEntityHeadNormalHeightInsideBlock(player);
	}
}
