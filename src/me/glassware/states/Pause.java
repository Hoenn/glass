package me.glassware.states;

import me.glassware.handlers.GameInput;
import me.glassware.handlers.GameStateManager;
import me.glassware.main.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Pause extends GameState
{
	private OrthographicCamera cam;
	
	private String pauseMessage;
	private float pauseMessageX;
	private float pauseMessageY;
	
	private BitmapFont font;
	
	private Color color;
	
	public Pause(GameStateManager gsm)
	{
		super(gsm);
		pauseMessage="Press ESC to Continue";
		pauseMessageX=Game.V_WIDTH/2-Game.font.getBounds(pauseMessage).width/2;
		pauseMessageY=Game.V_HEIGHT/2+Game.font.getBounds(pauseMessage).height/2;

		font = new BitmapFont();
		color = new Color(Color.BLACK);
		
		cam = new OrthographicCamera();
		cam.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGHT);
		
	}
	public void render()
	{
		sb.begin();
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);		
		font.setColor(color);
		font.draw(sb, pauseMessage, pauseMessageX, pauseMessageY);
		sb.end();
	}
	public void handleInput()
	{

		if(GameInput.isPressed(GameInput.BUTTON_ESC))
		{
			font.setColor(Color.WHITE);
			gsm.setState(gsm.MENU);
			gsm.disposeState(gsm.PAUSE);
		}
	}

	public void update(float dt)
	{
		handleInput();
		updateFontColor();

	}
	private void updateFontColor()
	{
		color=color.lerp(Color.WHITE, .005f);
	}

	public void dispose()
	{
		
	}
}
