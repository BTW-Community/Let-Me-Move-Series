package net.dravigen.let_me_move_ex;

import api.AddonHandler;
import api.BTWAddon;
import net.dravigen.dranimation_lib.settings.DVS_ConfigManager;
import net.dravigen.dranimation_lib.utils.AnimationUtils;
import net.dravigen.let_me_move_ex.animation.AnimRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.StatCollector;
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
		AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
		
		if (!MinecraftServer.getIsServer()) {
			initKeybind();
		}
		else {
			AnimationUtils.extraIsPresent = true;
		}
		
		AnimRegistry.registerAllAnimation();
		
		DVS_ConfigManager.loadFromFile();
		
		LmmEx_Settings.registerAllSettings();
		
		DVS_ConfigManager.save();
		
	}
}