package me.glassware.main;

import java.util.Random;

import me.glassware.handlers.GameInput;
import me.glassware.handlers.GameInputProcessor;
import me.glassware.handlers.GameStateManager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

public class Game implements ApplicationListener
{
	public static final String TITLE ="Glass";
	public static final int V_WIDTH = 320;
	public static final int V_HEIGHT = 240;
	public static final int SCALE = 2;
	
	public static final float STEP = 1/60f;
	
	public static Array<String> itemList;
	public static Random random;
	public static AssetManager manager;
	public static BitmapFont font;
	
	private float accum;
	
	private SpriteBatch sb;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;
		
	private GameStateManager gsm;	
	
	private boolean showFPS=true;
	private boolean vsync=true;
	
	public static TextureAtlas atlas;
	
	public void create()
	{
		Gdx.input.setInputProcessor(new GameInputProcessor());
		
		//TODO:Resource File for items
		//Need a Resource Path File
		
		manager= new AssetManager();
		
		random = new Random();

		itemList = new Array<String>();
		itemList.add("sword");
		itemList.add("potion");
		
		Gdx.graphics.setVSync(vsync);
		
		sb = new SpriteBatch();
		cam = new OrthographicCamera();
		cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		hudCam = new OrthographicCamera();
		hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		
		font=new BitmapFont();
		
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

		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render();
		GameInput.update();

		if(showFPS)
		{
			sb.begin();
			sb.setProjectionMatrix(hudCam.combined);
			font.draw(sb, Integer.toString(Gdx.graphics.getFramesPerSecond()), 10, 15);
			sb.end();
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
