package net.dravigen.dranimation_lib;

import btw.AddonHandler;
import btw.BTWAddon;

public class DraNimationLibAddon extends BTWAddon {
	
	public DraNimationLibAddon() {
		super();
	}
	
	@Override
	public void initialize() {
		AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
	}
}