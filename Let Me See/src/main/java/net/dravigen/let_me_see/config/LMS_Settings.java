package net.dravigen.let_me_see.config;

import net.dravigen.dranimation_lib.settings.DVS_ConfigManager;

public class LMS_Settings {
	private static final String category = "Let Me See !";
	public static DVS_ConfigManager.ConfigValue<Boolean> FIRST_PERSON_MODEL;
	public static DVS_ConfigManager.ConfigValue<Double> HEAD_OFFSET;
	public static DVS_ConfigManager.ConfigValue<Double> JUMP_MULTIPLIER;
	public static DVS_ConfigManager.ConfigValue<Double> SWING_MULTIPLIER;
	public static DVS_ConfigManager.ConfigValue<Double> BOBBING_MULTIPLIER;
	public static DVS_ConfigManager.ConfigValue<Double> FORWARD_MULTIPLIER;
	public static DVS_ConfigManager.ConfigValue<Double> STRAFING_MULTIPLIER;
	public static DVS_ConfigManager.ConfigValue<Double> CAMERA_MULTIPLIER;
	
	public static void registerAllSettings() {
		FIRST_PERSON_MODEL = DVS_ConfigManager.registerBool("3dModel",
															"First Person Model",
															false,
															"Renders the player's model while in first person",
															category);
		
		HEAD_OFFSET = DVS_ConfigManager.registerDouble("headOffset",
													   "First Person Head Offset",
													   1.0,
													   -1.0,
													   2.0,
													   "Handles how far your camera is set when first person model is ON",
													   category);
		
		SWING_MULTIPLIER = DVS_ConfigManager.registerDouble("swingingMul",
															"Swinging Multiplier",
															1.0,
															-16,
															16,
															"Handles head's movement when swinging your arm",
															category);
		
		BOBBING_MULTIPLIER = DVS_ConfigManager.registerDouble("bobbingMul",
															  "Bobbing Multiplier",
															  1.0,
															  -16,
															  16,
															  "Handles head's movement when moving",
															  category);
		
		STRAFING_MULTIPLIER = DVS_ConfigManager.registerDouble("strafingMul",
															   "Strafing Multiplier",
															   1.0,
															   -16,
															   16,
															   "Handles strafing head's movement",
															   category);
		
		FORWARD_MULTIPLIER = DVS_ConfigManager.registerDouble("forwardMul",
															  "Forward Multiplier",
															  1.0,
															  -16,
															  16,
															  "Handles forward head's movement",
															  category);
		
		JUMP_MULTIPLIER = DVS_ConfigManager.registerDouble("jumpMul",
														   "Jump Multiplier",
														   1.0,
														   -16,
														   16,
														   "Handles jumping head's movement",
														   category);
		
		CAMERA_MULTIPLIER = DVS_ConfigManager.registerDouble("cameraMoveMul",
															 "Camera Movement Multiplier",
															 1.0,
															 -16,
															 16,
															 "Handles head's movement when looking around",
															 category);
		
	}
}
