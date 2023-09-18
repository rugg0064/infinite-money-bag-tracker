package com.infinite.money.bag.tracker;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class infiniteMoneyBagTrackerTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(InfiniteMoneyBagTrackerPlugin.class);
		RuneLite.main(args);
	}
}