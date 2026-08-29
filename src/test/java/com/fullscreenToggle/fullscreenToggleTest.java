package com.fullscreenToggle;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class fullscreenToggleTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(fullscreenTogglePlugin.class);
		RuneLite.main(args);
	}
}