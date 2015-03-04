package me.glassware.main;

import java.util.Random;

import me.glassware.handlers.Content;
import me.glassware.handlers.GameInput;
import me.glassware.handlers.GameInputProcessor;
import me.glassware.handlers.GameStateManager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
	
	public static BitmapFont font;
	
	private float accum;
	
	private SpriteBatch sb;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;
		
	private GameStateManager gsm;	
	
	private boolean showFPS=false;
	
	public static Content res;
	
	public void create()
	{
		Gdx.input.setInputProcessor(new GameInputProcessor());
		
		//TODO:Resource File for items
		//Need a Resource Path File
		res = new Content();
		res.loadTexture("res/images/wizardSprite.png", "player");
		res.loadTexture("res/images/sword.png", "sword");
		res.loadTexture("res/images/potion.png", "potion");
		
		random = new Random();

		itemList = new Array<String>();
		itemList.add("sword");
		itemList.add("potion");
		
		
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
		accum+= Gdx.graphics.getDeltaTime();
		while(accum>=STEP)
		{
			accum-=STEP;
			gsm.update(STEP);
			gsm.render();
			GameInput.update();
		}
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
