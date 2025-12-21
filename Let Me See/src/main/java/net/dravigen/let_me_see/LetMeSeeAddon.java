package net.dravigen.let_me_see;

import api.AddonHandler;
import api.BTWAddon;
import net.dravigen.dranimation_lib.settings.DVS_ConfigManager;
import net.dravigen.let_me_see.config.LMS_Settings;

public class LetMeSeeAddon extends BTWAddon {
	
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