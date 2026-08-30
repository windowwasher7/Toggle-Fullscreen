package com.fullscreenToggle; // keep your existing package line here

import com.google.inject.Provides;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Window;
import javax.inject.Inject;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
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
	private JMenuBar savedMenuBar;
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
		hotkeyListener.setEnabledOnLoginScreen(true);
		keyManager.registerKeyListener(hotkeyListener);
	}

	@Override
	protected void shutDown()
	{
		keyManager.unregisterKeyListener(hotkeyListener);

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
			if (!(window instanceof JFrame))
			{
				return;
			}
			frame = (Frame) window;
		}

		JFrame jFrame = (JFrame) frame;

		if (!fullscreen)
		{
			savedBounds = frame.getBounds();
			savedMenuBar = jFrame.getJMenuBar(); // this is the top grey bar

			Rectangle screenBounds = frame.getGraphicsConfiguration()
					.getDevice()
					.getDefaultConfiguration()
					.getBounds();

			frame.dispose();
			frame.setUndecorated(true);
			jFrame.setJMenuBar(null); // hide it
			frame.setBounds(screenBounds);
			frame.setVisible(true);

			fullscreen = true;
		}
		else
		{
			frame.dispose();
			frame.setUndecorated(false);
			jFrame.setJMenuBar(savedMenuBar); // give it back
			frame.setBounds(savedBounds);
			frame.setVisible(true);

			fullscreen = false;
		}
	}
}