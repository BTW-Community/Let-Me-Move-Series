package net.dravigen.let_me_move;

import api.AddonHandler;
import api.BTWAddon;
import api.world.data.DataEntry;
import api.world.data.DataProvider;
import net.dravigen.dranimation_lib.utils.AnimationUtils;
import net.dravigen.let_me_move.animation.AnimRegistry;
import net.dravigen.let_me_move.animation.player.poses.AnimStanding;
import net.minecraft.src.NBTTagCompound;

public class LetMeMoveAddon extends BTWAddon {
	
	private static final String CURRENT_ANIMATION_NAME = "CurrentAnimation";
	public static final DataEntry.PlayerDataEntry<String> CURRENT_ANIMATION = DataProvider.getBuilder(String.class)
			.name(CURRENT_ANIMATION_NAME)
			.defaultSupplier(() -> String.valueOf(AnimStanding.id))
			.readNBT(NBTTagCompound::getString)
			.writeNBT(NBTTagCompound::setString)
			.player()
			.syncPlayer()
			.buildPlayer();
	
	public static boolean isExtraLoaded() {
		return AddonHandler.isModInstalled("let_me_move_ex") && AnimationUtils.extraIsPresent;
	}
	
	@Override
	public void preInitialize() {
		CURRENT_ANIMATION.register();
	}
	
	@Override
	public void initialize() {
		AnimRegistry.registerAllAnimation();
		AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
	}
}