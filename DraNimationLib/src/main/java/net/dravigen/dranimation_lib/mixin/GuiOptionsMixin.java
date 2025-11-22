package net.dravigen.dranimation_lib.mixin;

import net.dravigen.dranimation_lib.DraNimationLibAddon;
import net.dravigen.dranimation_lib.gui.GuiDVS_Settings;
import net.dravigen.dranimation_lib.gui.GuiButtonCustom;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiOptions;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiOptions.class)
public class GuiOptionsMixin extends GuiScreen {
	@Unique
	private static final int VDS_SETTINGS_ID = 650;
	
	@Inject(method = "initGui", at = @At("RETURN"))
	private void addCustomButton(CallbackInfo ci) {
		this.buttonList.add(new GuiButtonCustom(VDS_SETTINGS_ID,
												this.width / 2 - 152 - 4 - 20,
												this.height / 6 + 96 - 6,
												20,
												20,
												20,
												20,
												"",
												new ResourceLocation("dranimation_lib:textures/gui/texture.png")));
	}
	
	@Inject(method = "actionPerformed", at = @At("HEAD"))
	private void onActionPerformed(GuiButton button, CallbackInfo ci) {
		if (button.id == VDS_SETTINGS_ID) {
			this.mc.displayGuiScreen(new GuiDVS_Settings(this, DraNimationLibAddon.settingsManager));
		}
	}
}
