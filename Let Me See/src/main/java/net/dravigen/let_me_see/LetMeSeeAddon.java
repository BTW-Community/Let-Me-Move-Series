package net.dravigen.let_me_see;

import btw.AddonHandler;
import btw.BTWAddon;

public class LetMeSeeAddon extends BTWAddon {

    public static boolean enable3DFirstPerson = true;
    
    public LetMeSeeAddon() {
        super();
    }

    @Override
    public void initialize() {
        AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
    }
}