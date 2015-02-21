package me.glassware.handlers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class GameInputProcessor extends InputAdapter
{
	@Override
	public boolean keyDown(int k)
	{
		if(k==Keys.Z)
		{
			GameInput.setKey(GameInput.BUTTON1, true);
		}
		if(k==Keys.X)
		{
			GameInput.setKey(GameInput.BUTTON2, true);
		}
		return true;
	}
	@Override
	public boolean keyUp(int k)
	{
		if(k==Keys.Z)
		{
			GameInput.setKey(GameInput.BUTTON1, false);
		}
		if(k==Keys.X)
		{
			GameInput.setKey(GameInput.BUTTON2, false);
		}
		return true;
	}
}
