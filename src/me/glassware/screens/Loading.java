package me.glassware.screens;

import me.glassware.handlers.GameScreenManager;
import me.glassware.main.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

public class Loading extends GameScreen
{
	private float progress;
	private BitmapFont font;
	
	public Loading(GameScreenManager gsm)
	{
		super(gsm);
		Game.currentScreen=this;

		progress=0;
		font = new BitmapFont();
		
		Game.manager.load("res/images/assets.pack", TextureAtlas.class); //Game's Texture Atlas
		//Game.atlas.findRegions("LoadingTextures");
		//Make animation of loading textures
		//animated loading textures with progress float
		Game.manager.load("res/songs/testmusic.ogg", Music.class); //Menu music
		Game.manager.load("res/sounds/magic154.ogg", Sound.class);
	}
	public void handleInput()
	{
		//no input to be handled in the loading screen
	}
	public void update(float dt)
	{
		progress=Game.manager.getProgress(); // gets a % of loading completed
		if(Game.manager.update())
		{
			Game.atlas=Game.manager.get("res/images/assets.pack");
			System.out.println("TEST1");

			gsm.setScreen(gsm.MENU, false);
		}

			
	}
	public void render()
	{
		sb.begin();
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);		
		font.draw(sb, Float.toString(progress), 50, 50);
		sb.end();
	}
	public void dispose()
	{
		font.dispose();
	}

	public void pause(){}
	public void resume(){}

}
