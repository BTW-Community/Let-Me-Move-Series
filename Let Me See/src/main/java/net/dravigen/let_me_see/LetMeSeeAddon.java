package net.dravigen.let_me_see;

import btw.AddonHandler;
import btw.BTWAddon;
import net.dravigen.dranimation_lib.settings.DVS_ConfigManager;
import net.dravigen.let_me_see.config.LmsSettings;

import java.io.File;

public class LetMeSeeAddon extends BTWAddon {
	
	public static DVS_ConfigManager settingsManager;
	
	public LetMeSeeAddon() {
		super();
	}
	
	@Override
	public void initialize() {
		AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
		
		settingsManager = new DVS_ConfigManager(new File("config/dynamic_visuals.properties"));
		
		LmsSettings.registerAllSettings(settingsManager);
		
		settingsManager.save();
	}
}