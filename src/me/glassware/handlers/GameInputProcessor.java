package me.glassware.handlers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class GameInputProcessor extends InputAdapter
{
	@Override
	public boolean keyDown(int k)
	{
		if(k==Keys.SPACE)
		{
			GameInput.setKey(GameInput.BUTTON_SPACE, true);
		}
		if(k==Keys.A)
		{
			GameInput.setKey(GameInput.BUTTON_A, true);
		}
		if(k==Keys.D)
		{
			GameInput.setKey(GameInput.BUTTON_D, true);
		}
		if(k==Keys.Z)
		{
			GameInput.setKey(GameInput.BUTTON_Z, true);
		}
		return true;
	}
	@Override
	public boolean keyUp(int k)
	{
		if(k==Keys.SPACE)
		{
			GameInput.setKey(GameInput.BUTTON_SPACE, false);
		}
		if(k==Keys.A)
		{
			GameInput.setKey(GameInput.BUTTON_A, false);
		}
		if(k==Keys.D)
		{
			GameInput.setKey(GameInput.BUTTON_D, false);
		}
		if(k==Keys.Z)
		{
			GameInput.setKey(GameInput.BUTTON_Z, false);
		}
		return true;
	}
}
