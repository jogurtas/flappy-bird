package com.gudu.flappybird.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gudu.flappybird.Application;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = (int) (144 * 2.5f);
		config.height = (int) (256 * 2.5f);
		new LwjglApplication(new Application(), config);
	}
}
