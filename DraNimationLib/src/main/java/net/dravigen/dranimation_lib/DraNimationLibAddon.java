package net.dravigen.dranimation_lib;

import api.AddonHandler;
import api.BTWAddon;

public class DraNimationLibAddon extends BTWAddon {
	public DraNimationLibAddon() {
		super();
	}
	
	@Override
	public void initialize() {
		AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
	}
}