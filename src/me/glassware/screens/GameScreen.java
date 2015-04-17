package me.glassware.screens;


import me.glassware.handlers.GameScreenManager;
import me.glassware.main.Game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

public abstract class GameScreen implements Screen
{
	protected GameScreenManager gsm;
	protected Game game;
	
	protected SpriteBatch sb;
	protected OrthographicCamera cam;
	protected OrthographicCamera hudCam;
	protected OrthographicCamera b2dCam;
	
	protected Array<Body> bodyList;
	
	
	private final float[] zoomDepth = new float[]
				{.125f, .25f, .5f, .75f, 1f, 1.25f, 1.5f, 1.75f, 2f, 3f, 4f, 5f};
	private final int defaultZoomDepthPosition=4; 
	private int currentZoomDepth=defaultZoomDepthPosition;
		
	protected GameScreen(GameScreenManager gsm)
	{
		this.gsm = gsm;
		game = gsm.getGame();
		sb=game.getSpriteBatch();
		cam=game.getCamera();
		hudCam = game.getHUDCamera();
		b2dCam=game.getb2dCamera();
		zoomDefault();
	}
	public Array<Body> getBodyList()
	{
		Game.world.getBodies(bodyList);
		return bodyList;
	}
	protected void zoomIn()
	{
		if(currentZoomDepth>0)
		{
			cam.zoom= zoomDepth[currentZoomDepth-1];
			cam.update();
			
			b2dCam.zoom=zoomDepth[currentZoomDepth-1];
			b2dCam.update();
			
			currentZoomDepth--;
		}
	}
	
	protected void zoomOut()
	{
		if(currentZoomDepth<zoomDepth.length-1)
		{
			cam.zoom= zoomDepth[currentZoomDepth+1];
			cam.update();
			
			b2dCam.zoom=zoomDepth[currentZoomDepth+1];
			b2dCam.update();
			
			currentZoomDepth++;
		}
	}
	
	protected void zoomDefault()
	{
		cam.zoom=zoomDepth[defaultZoomDepthPosition];
		cam.update();
		
		b2dCam.zoom=zoomDepth[defaultZoomDepthPosition];
		b2dCam.update();
		
		currentZoomDepth=defaultZoomDepthPosition;
	}
	public abstract void handleInput();
	public abstract void update(float dt);
	public abstract void render();
	public abstract void dispose();

}
