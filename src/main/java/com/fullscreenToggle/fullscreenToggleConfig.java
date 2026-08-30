package com.fullscreenToggle;

import java.awt.event.KeyEvent;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Keybind;
import net.runelite.client.config.ModifierlessKeybind;

@ConfigGroup("fullscreenToggle")
public interface fullscreenToggleConfig extends Config
{
	@ConfigItem(
			keyName = "toggleKey",
			name = "Toggle Fullscreen Key",
			description = "Hotkey that toggles fullscreen mode"
	)
	default Keybind toggleKey()
	{
		return new ModifierlessKeybind(KeyEvent.VK_F11, 0);
	}
}