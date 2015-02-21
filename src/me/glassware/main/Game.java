package me.glassware.main;

import me.glassware.handlers.GameInput;
import me.glassware.handlers.GameInputProcessor;
import me.glassware.handlers.GameStateManager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Game implements ApplicationListener
{
	public static final String TITLE ="Glass";
	public static final int V_WIDTH = 320;
	public static final int V_HEIGHT = 240;
	public static final int SCALE = 2;
	
	public static final float STEP = 1/60f;
	private float accum;
	
	private SpriteBatch sb;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;
	
	private GameStateManager gsm;	
	
	public void create()
	{
		Gdx.input.setInputProcessor(new GameInputProcessor());
		sb = new SpriteBatch();
		cam = new OrthographicCamera();
		cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		hudCam = new OrthographicCamera();
		hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		
		gsm = new GameStateManager(this);

		
	}
	public SpriteBatch getSpriteBatch()
	{
		return sb;
	}
	
	public OrthographicCamera getCamera()
	{
		return cam;
	}
	
	public OrthographicCamera getHUDCamera()
	{
		return hudCam;
	}
	public void render()
	{
		accum+= Gdx.graphics.getDeltaTime();
		while(accum>=STEP)
		{
			accum-=STEP;
			gsm.update(STEP);
			gsm.render();
			GameInput.update();
		}
		
	}
	public void dispose()
	{
		
		
	}
	public void pause()
	{
		
		
	}

	public void resize(int arg0, int arg1)
	{
		
		
	}
	public void resume()
	{
		
		
	}
}
