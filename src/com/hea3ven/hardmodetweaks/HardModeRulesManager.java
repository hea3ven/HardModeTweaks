package com.hea3ven.hardmodetweaks;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameRules;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class HardModeRulesManager {

	private Map<String, String> rulesValues;

	public HardModeRulesManager() {
		rulesValues = new HashMap<String, String>();
	}

	@SubscribeEvent
	public void worldLoad(WorldEvent.Load e) {
		if (e.world.provider.dimensionId == 0 && !e.world.isRemote) {
			if ((e.world.difficultySetting != null && e.world.difficultySetting == EnumDifficulty.HARD)
					|| Minecraft.getMinecraft().gameSettings.difficulty == EnumDifficulty.HARD) {
				GameRules rules = MinecraftServer.getServer()
						.worldServerForDimension(0).getGameRules();
				for (Entry<String, String> entry : rulesValues.entrySet()) {
					rules.setOrCreateGameRule(entry.getKey(), entry.getValue());
				}
			} else {
				GameRules defaultRules = new GameRules();
				GameRules rules = MinecraftServer.getServer()
						.worldServerForDimension(0).getGameRules();
				for (String ruleName : defaultRules.getRules()) {
					rules.setOrCreateGameRule(ruleName,
							defaultRules.getGameRuleStringValue(ruleName));
				}
			}
		}
	}

	public void setRule(String ruleName, String value) {
		rulesValues.put(ruleName, value);
	}
}
