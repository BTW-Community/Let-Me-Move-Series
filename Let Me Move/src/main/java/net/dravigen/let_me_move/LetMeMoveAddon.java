package net.dravigen.let_me_move;

import btw.AddonHandler;
import btw.BTWAddon;
import btw.world.util.data.DataEntry;
import btw.world.util.data.DataProvider;
import net.dravigen.let_me_move.animation.AnimRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;
import org.lwjgl.input.Keyboard;

public class LetMeMoveAddon extends BTWAddon {
	
	private static final String CURRENT_ANIMATION_NAME = "CurrentAnimation";
	public static final DataEntry.PlayerDataEntry<String> CURRENT_ANIMATION = DataProvider.getBuilder(String.class)
			.name(CURRENT_ANIMATION_NAME)
			.defaultSupplier(() -> String.valueOf(AnimRegistry.STANDING.getID()))
			.readNBT(NBTTagCompound::getString)
			.writeNBT(NBTTagCompound::setString)
			.player()
			.syncPlayer()
			.buildPlayer();
	
	
	public LetMeMoveAddon() {
		super();
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
	
	public static boolean isExtraLoaded() {
		return FabricLoader.getInstance().isModLoaded("let_me_move_ex");
	}
}