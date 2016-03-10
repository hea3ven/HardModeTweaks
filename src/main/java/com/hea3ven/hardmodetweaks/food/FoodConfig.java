package com.hea3ven.hardmodetweaks.food;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FoodConfig {
	private static Logger logger = LogManager.getLogger("HardModeTweaks.Food");
	private String name;
	private int value;
	private float saturation;

	public FoodConfig(String name, int value, float saturation) {
		this.name = name;
		this.value = value;
		this.saturation = saturation;
	}

	public String getName() {
		return name;
	}

	public int getValue() {
		return value;
	}

	public float getSaturation() {
		return saturation;
	}

	public static FoodConfig parse(String foodConfigString) {
		String[] parts = foodConfigString.split("\\|", 3);
		if (parts.length != 3) {
			logger.warn("Could not parse food config from '{}'", foodConfigString);
			return null;
		}
		try {
			return new FoodConfig(parts[0], Integer.parseInt(parts[1]),
					Float.parseFloat(parts[2]));
		} catch (NumberFormatException e) {
			return null;
		}
	}

}

