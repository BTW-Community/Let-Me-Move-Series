package net.dravigen.dranimation_lib;

import btw.AddonHandler;
import btw.BTWAddon;
import net.dravigen.dranimation_lib.settings.DVS_ConfigManager;

import java.io.File;

public class DraNimationLibAddon extends BTWAddon {
	
	public static DVS_ConfigManager settingsManager;
	
	public DraNimationLibAddon() {
		super();
	}
	
	@Override
	public void initialize() {
		AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
		
		settingsManager = new DVS_ConfigManager(new File("config/dynamic_visuals.properties"));
		
		settingsManager.save();
	}
}