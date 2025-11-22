package net.dravigen.dranimation_lib.settings;

public record BaseSetting(String id, DVS_ConfigManager.Type type, String name, Object defaultValue, double min, double max,
						  String description, String category) {}
