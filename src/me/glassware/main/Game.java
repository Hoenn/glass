package me.glassware.main;

import static me.glassware.handlers.B2DVars.PPM;
import me.glassware.handlers.GameInput;
import me.glassware.handlers.GameInputProcessor;
import me.glassware.handlers.GameScreenManager;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

public class Game extends ApplicationAdapter
{
	public static final String TITLE ="Glass";
	public static final int V_WIDTH = 320;
	public static final int V_HEIGHT = 180;
	public static final int SCALE = 2;
	
	public static final float STEP = 1/60f;
	
	public static Array<String> itemList;
	public static final AssetManager manager= new AssetManager();
	public static BitmapFont font;

	
	private SpriteBatch sb;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;
	private OrthographicCamera b2dCam;
		
	private GameScreenManager gsm;	
	
	private boolean showFPS=true;
	private boolean vsync=true;
		
	public static TextureAtlas atlas;
	
	public void create()
	{
		Gdx.input.setInputProcessor(new GameInputProcessor());
		
		//TODO:Resource File for items
		//Need a Resource Path File
				
		//GLProfiler.enable();
		
		itemList = new Array<String>();
		itemList.add("sword");
		itemList.add("potion");
		
		Gdx.graphics.setVSync(vsync);
		Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height,true);

		sb = new SpriteBatch();
		cam = new OrthographicCamera();
		cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		hudCam = new OrthographicCamera();
		hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		b2dCam=new OrthographicCamera();
		b2dCam.setToOrtho(false, Game.V_WIDTH/PPM, Game.V_HEIGHT/PPM);
		font=new BitmapFont();
		
		gsm = new GameScreenManager(this);
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
	public OrthographicCamera getb2dCamera()
	{
		return b2dCam;
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
			//font.draw(sb, "Draws "+Integer.toString(GLProfiler.drawCalls), 40, 15);
			//font.draw(sb, "Bindings "+Integer.toString(GLProfiler.textureBindings),150, 15);
			//font.draw(sb,"Shader Switches "+Integer.toString( GLProfiler.shaderSwitches), 10, 45);
			sb.end();
		}
	}

}
