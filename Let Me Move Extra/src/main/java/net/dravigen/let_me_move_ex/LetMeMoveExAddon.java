package net.dravigen.let_me_move_ex;

import btw.AddonHandler;
import btw.BTWAddon;
import net.dravigen.let_me_move_ex.animation.AnimRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;
import org.lwjgl.input.Keyboard;

public class LetMeMoveExAddon extends BTWAddon {
	
	public static KeyBinding crawl_key;
	public static KeyBinding roll_key;
	
	public static KeyBinding[] addonKeys;
	
	public LetMeMoveExAddon() {
		super();
	}
	
	public void initKeybind() {
		crawl_key = new KeyBinding(StatCollector.translateToLocal("Crawl"), Keyboard.KEY_C);
		roll_key = new KeyBinding(StatCollector.translateToLocal("Roll"), Keyboard.KEY_V);
		
		addonKeys = new KeyBinding[]{crawl_key, roll_key};
	}
	
	@Override
	public void initialize() {
		if (!MinecraftServer.getIsServer()) {
			initKeybind();
		}
		AnimRegistry.registerAllAnimation();
		AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
	}
}