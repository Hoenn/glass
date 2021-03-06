package me.glassware.main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class GlassDesktop
{
	public static void main(String[] args)
	{
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		
		cfg.title = Game.TITLE;
		cfg.width = Game.V_WIDTH*Game.SCALE;
		cfg.height = Game.V_HEIGHT*Game.SCALE;
		cfg.backgroundFPS = 60;
		
		new LwjglApplication(new Game(), cfg);
	}

}
