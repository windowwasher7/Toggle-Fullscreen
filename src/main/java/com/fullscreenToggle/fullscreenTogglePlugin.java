package com.fullscreenToggle;

import com.google.inject.Provides;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Window;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.HotkeyListener;

@PluginDescriptor(
		name = "Fullscreen Toggle",
		description = "Toggle a borderless fullscreen mode, similar to F11 in a browser"
)
public class fullscreenTogglePlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private KeyManager keyManager;

	@Inject
	private fullscreenToggleConfig config;

	private Frame frame;
	private Rectangle savedBounds;
	private boolean fullscreen;

	@Provides
	fullscreenToggleConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(fullscreenToggleConfig.class);
	}

	private final HotkeyListener hotkeyListener = new HotkeyListener(() -> config.toggleKey())
	{
		@Override
		public void hotkeyPressed()
		{
			toggleFullscreen();
		}
	};

	@Override
	protected void startUp()
	{
		keyManager.registerKeyListener(hotkeyListener);
	}

	@Override
	protected void shutDown()
	{
		keyManager.unregisterKeyListener(hotkeyListener);

		// don't leave the client borderless if the plugin is disabled mid-fullscreen
		if (fullscreen)
		{
			toggleFullscreen();
		}
	}

	private void toggleFullscreen()
	{
		if (frame == null)
		{
			Window window = SwingUtilities.getWindowAncestor(client.getCanvas());
			if (!(window instanceof Frame))
			{
				return;
			}
			frame = (Frame) window;
		}

		if (!fullscreen)
		{
			savedBounds = frame.getBounds();

			Rectangle screenBounds = frame.getGraphicsConfiguration()
					.getDevice()
					.getDefaultConfiguration()
					.getBounds();

			frame.dispose();
			frame.setUndecorated(true);
			frame.setBounds(screenBounds);
			frame.setVisible(true);

			fullscreen = true;
		}
		else
		{
			frame.dispose();
			frame.setUndecorated(false);
			frame.setBounds(savedBounds);
			frame.setVisible(true);

			fullscreen = false;
		}
	}
}