package net.dravigen.let_me_see.mixin;

import net.minecraft.src.Gui;
import net.minecraft.src.GuiIngame;
import net.minecraft.src.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiIngame.class)
public abstract class GuiInGameMixin extends Gui {
	@Shadow @Final private Minecraft mc;
	
	@Redirect(method = "renderGameOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/GuiIngame;drawTexturedModalRect(IIIIII)V", ordinal = 2))
	private void disable1CrossWhenF5(GuiIngame instance, int i, int i1, int i2, int i3, int i4, int i5) {
		if (this.mc.gameSettings.thirdPersonView == 0) {
			instance.drawTexturedModalRect(i, i1, i2, i3, i4, i5);
		}
	}
}
