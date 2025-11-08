package net.dravigen.let_me_move.animation.player.poses;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Minecraft;
import net.minecraft.src.ResourceLocation;

public class AnimGui extends AnimCommon{
	public static final ResourceLocation id = new ResourceLocation("LMM", "busy");
	
	public AnimGui() {
		super(id);
	}
	
	@Override
	public boolean isGeneralConditonsMet(EntityPlayer player, AxisAlignedBB axisAlignedBB) {
		return player == Minecraft.getMinecraft().thePlayer && Minecraft.getMinecraft().currentScreen != null;
	}
}
