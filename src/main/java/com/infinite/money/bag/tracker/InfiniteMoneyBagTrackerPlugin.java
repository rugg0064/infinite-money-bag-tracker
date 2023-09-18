package com.infinite.money.bag.tracker;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;

@Slf4j
@PluginDescriptor(
	name = "Infinite Money Bag Tracker",
	description = "",
	tags = {"Infinite money bag"}
)
public class InfiniteMoneyBagTrackerPlugin extends Plugin
{
	@Inject private OverlayManager overlayManager;
	@Inject private TooltipManager tooltipManager;
	@Inject private Client client;
	@Inject private ConfigManager configManager;
	@Inject private Gson gson;

	private static final String CONFIG_GROUP = "infinitemoneybagtracker";
	private static final String CONFIG_KEY = "tracker";

	private InfiniteMoneyBagTrackerItemOverlay itemOverlay;
	private InfiniteMoneyBagTrackerHoverOverlay hoverOverlay;

	Map<String, Integer> tracker;

	private int thisSession = 0;

	@Override
	protected void startUp() throws Exception
	{
		loadFromConfig();
		itemOverlay = new InfiniteMoneyBagTrackerItemOverlay(this);
		overlayManager.add(itemOverlay);
		hoverOverlay = new InfiniteMoneyBagTrackerHoverOverlay(this, client, tooltipManager);
		overlayManager.add(hoverOverlay);
		thisSession = 0;
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(itemOverlay);
		overlayManager.remove(hoverOverlay);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if(gameStateChanged.getGameState() == GameState.LOGGING_IN) {
			thisSession = 0;
		}
	}


	@Subscribe
	public void onChatMessage(ChatMessage chat) {
		if(chat.getMessage().equals(Helper.MONEY_BAG_SUCCESS_MESSAGE)) {
			iterate();
		}
	}

	private void iterate() {
		Integer globalCount = tracker.get("global");
		int newGlobalCount = globalCount == null ? 1 : globalCount + 1;
		tracker.put("global", newGlobalCount);

		String hash = String.valueOf(client.getAccountHash());
		Integer account = tracker.get(hash);
		int newHashCount = account == null ? 1 : account + 1;
		tracker.put(hash, newHashCount);

		thisSession++;
		saveToConfig();
	}

	public int getThisSession()
	{
		return thisSession;
	}

	public int getGlobal()
	{
		Integer value = tracker.get("global");
		return value == null ? 0 : value;
	}

	// Lodas config, merges by boss name, and saves to bossDatabase
	private void loadFromConfig() {
		String json = configManager.getConfiguration(CONFIG_GROUP, CONFIG_KEY);
		tracker = stringToTracker(json);
		if(tracker == null) {
			tracker = new HashMap<String, Integer>();
		}
		saveToConfig();
	}

	// Saves the current bossDatabase to config
	private void saveToConfig() {
		configManager.unsetConfiguration(CONFIG_GROUP, CONFIG_KEY);
		String json = gson.toJson(tracker);
		configManager.setConfiguration(CONFIG_GROUP, CONFIG_KEY, json);
	}

	private Map<String, Integer>stringToTracker(String string) {
		TypeToken<HashMap<String, Integer>> typeToken = new TypeToken<HashMap<String, Integer>>() {};
		HashMap<String, Integer> hashMap = gson.fromJson(string, typeToken.getType());
		return hashMap;
	}
}
