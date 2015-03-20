package me.glassware.handlers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class GameInputProcessor extends InputAdapter
{
	@Override
	public boolean keyDown(int k)
	{
		if(k==Keys.W)
		{
			GameInput.setKey(GameInput.BUTTON_W, true);
		}
		if(k==Keys.A)
		{
			GameInput.setKey(GameInput.BUTTON_A, true);
		}
		if(k==Keys.S)
		{
			GameInput.setKey(GameInput.BUTTON_S, true);
		}
		if(k==Keys.D)
		{
			GameInput.setKey(GameInput.BUTTON_D, true);
		}
		if(k==Keys.Z)
		{
			GameInput.setKey(GameInput.BUTTON_Z, true);
		}
		if(k==Keys.X)
		{
			GameInput.setKey(GameInput.BUTTON_X, true);
		}
		if(k==Keys.C)
		{
			GameInput.setKey(GameInput.BUTTON_C, true);
		}
		if(k==Keys.UP)
		{
			GameInput.setKey(GameInput.BUTTON_UP, true);
		}
		if(k==Keys.DOWN)
		{
			GameInput.setKey(GameInput.BUTTON_DOWN, true);
		}
		if(k==Keys.LEFT)
		{
			GameInput.setKey(GameInput.BUTTON_LEFT, true);
		}
		if(k==Keys.RIGHT)
		{
			GameInput.setKey(GameInput.BUTTON_RIGHT, true);
		}
		if(k==Keys.ESCAPE)
		{
			GameInput.setKey(GameInput.BUTTON_ESC, true);
		}
		return true;
	}
	@Override
	public boolean keyUp(int k)
	{
		if(k==Keys.W)
		{
			GameInput.setKey(GameInput.BUTTON_W, false);
		}
		if(k==Keys.A)
		{
			GameInput.setKey(GameInput.BUTTON_A, false);
		}
		if(k==Keys.S)
		{
			GameInput.setKey(GameInput.BUTTON_S, false);
		}
		if(k==Keys.D)
		{
			GameInput.setKey(GameInput.BUTTON_D, false);
		}
		if(k==Keys.Z)
		{
			GameInput.setKey(GameInput.BUTTON_Z, false);
		}
		if(k==Keys.X)
		{
			GameInput.setKey(GameInput.BUTTON_X, false);
		}
		if(k==Keys.C)
		{
			GameInput.setKey(GameInput.BUTTON_C, false);
		}
		if(k==Keys.UP)
		{
			GameInput.setKey(GameInput.BUTTON_UP, false);
		}
		if(k==Keys.DOWN)
		{
			GameInput.setKey(GameInput.BUTTON_DOWN, false);
		}
		if(k==Keys.LEFT)
		{
			GameInput.setKey(GameInput.BUTTON_LEFT, false);
		}
		if(k==Keys.RIGHT)
		{
			GameInput.setKey(GameInput.BUTTON_RIGHT, false);
		}
		if(k==Keys.ESCAPE)
		{
			GameInput.setKey(GameInput.BUTTON_ESC, false);
		}
		return true;
	}
}
