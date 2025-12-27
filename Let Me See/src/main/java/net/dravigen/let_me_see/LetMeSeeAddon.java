package net.dravigen.let_me_see;

import api.AddonHandler;
import api.BTWAddon;
import net.dravigen.dranimation_lib.settings.DVS_ConfigManager;
import net.dravigen.let_me_see.config.LMS_Settings;
import net.minecraft.src.GuiContainerCreative;
import net.minecraft.src.GuiInventory;
import net.minecraft.src.Minecraft;

public class LetMeSeeAddon extends BTWAddon {
	
	public static boolean isPlayerRenderedByFreeCam = false;
	
	public static boolean isIsCustomFirstPerson(Minecraft mc) {
		return !isPlayerRenderedByFreeCam && LMS_Settings.FIRST_PERSON_MODEL.getBool() &&
				mc.gameSettings.thirdPersonView == 0 &&
				!(mc.currentScreen instanceof GuiContainerCreative || mc.currentScreen instanceof GuiInventory);
	}
	
	public LetMeSeeAddon() {
		super();
	}
	
	@Override
	public void initialize() {
		AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
		
		DVS_ConfigManager.loadFromFile();
		
		LMS_Settings.registerAllSettings();
		
		DVS_ConfigManager.save();
	}
}