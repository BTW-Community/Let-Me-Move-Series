package net.dravigen.let_me_see.config;

import net.dravigen.dranimation_lib.settings.DVS_ConfigManager;

public class LmsSettings {
	public static final String firstPersonModelID = "3dModel";
	public static final String jumpMul = "jumpMul";
	public static final String bobbingMul = "bobbingMul";
	public static final String forwardMul = "forwardMul";
	public static final String strafingMul = "strafingMul";
	public static final String cameraMoveMul = "cameraMoveMul";
	
	public static DVS_ConfigManager.ConfigValue<Boolean> FIRST_PERSON_MODEL;
	public static DVS_ConfigManager.ConfigValue<Double> JUMP_MULTIPLIER;
	public static DVS_ConfigManager.ConfigValue<Double> BOBBING_MULTIPLIER;
	public static DVS_ConfigManager.ConfigValue<Double> FORWARD_MULTIPLIER;
	public static DVS_ConfigManager.ConfigValue<Double> STRAFING_MULTIPLIER;
	public static DVS_ConfigManager.ConfigValue<Double> CAMERA_MULTIPLIER;
	
	private static final String category = "Let Me See !";
	
	public static void registerAllSettings(DVS_ConfigManager settingsManager) {
		FIRST_PERSON_MODEL = DVS_ConfigManager.registerBool(firstPersonModelID,
														  "First Person Model",
														  false,
														  "Render the player's model while in first person",
														  category);
		
		BOBBING_MULTIPLIER = settingsManager.registerDouble(bobbingMul,
															"Bobbing Multiplier",
															1.0,
															"Handle camera roll movement when moving",
															category);
		
		STRAFING_MULTIPLIER = settingsManager.registerDouble(strafingMul,
															 "Strafing Camera Movement Multiplier",
															 1.0,
															 "",
															 category);
		
		FORWARD_MULTIPLIER = settingsManager.registerDouble(forwardMul,
															"Forward Camera Movement Multiplier",
															1.0,
															"",
															category);
		
		JUMP_MULTIPLIER = settingsManager.registerDouble(jumpMul, "Jump Camera Movement Multiplier", 1.0, "", category);
		
		CAMERA_MULTIPLIER = settingsManager.registerDouble(cameraMoveMul,
														   "Camera Roll Movement Multiplier",
														   1.0,
														   "Handle the camera's roll when looking around",
														   category);
	}
}
