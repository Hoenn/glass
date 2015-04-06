package me.glassware.handlers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class GameInputProcessor extends InputAdapter
{
	@Override
	public boolean keyDown(int keyCode)
	{
		switch(keyCode)
		{	
		
			//Movement
			case Keys.W:
				GameInput.setKey(GameInput.BUTTON_W, true);
					break;
			case Keys.A:
				GameInput.setKey(GameInput.BUTTON_A, true);
					break;
			case Keys.S:
				GameInput.setKey(GameInput.BUTTON_S, true);
					break;
			case Keys.D:
				GameInput.setKey(GameInput.BUTTON_D, true);
					break;
					
			//Actions
			case Keys.SPACE:
				GameInput.setKey(GameInput.BUTTON_SPACE, true);
					break;
			case Keys.Z:
				GameInput.setKey(GameInput.BUTTON_Z,  true);
					break;
			case Keys.X:
				GameInput.setKey(GameInput.BUTTON_X, true);
					break;
			case Keys.C:
				GameInput.setKey(GameInput.BUTTON_C, true);
					break;
			case Keys.R:
				GameInput.setKey(GameInput.BUTTON_R, true);
					break;
			case Keys.F:
				GameInput.setKey(GameInput.BUTTON_F, true);
					break;
			case Keys.NUM_1:
				GameInput.setKey(GameInput.BUTTON_NUM_1, true);
					break;
			case Keys.NUM_2:
				GameInput.setKey(GameInput.BUTTON_NUM_2, true);
					break;
			case Keys.NUM_3:
				GameInput.setKey(GameInput.BUTTON_NUM_3, true);
					break;
			case Keys.NUM_4:
				GameInput.setKey(GameInput.BUTTON_NUM_4, true);
					break;
			case Keys.NUM_5:
				GameInput.setKey(GameInput.BUTTON_NUM_5, true);
					break;
					
			//Rotation
			case Keys.UP:
				GameInput.setKey(GameInput.BUTTON_UP, true);
					break;
			case Keys.DOWN:
				GameInput.setKey(GameInput.BUTTON_DOWN, true);
					break;
			case Keys.RIGHT:
				GameInput.setKey(GameInput.BUTTON_RIGHT, true);
					break;
			case Keys.LEFT:
				GameInput.setKey(GameInput.BUTTON_LEFT, true);
					break;	
					
			//Menu related
			case Keys.ESCAPE:
				GameInput.setKey(GameInput.BUTTON_ESCAPE, true);
					break;
		}
			
		return true;
	}
	@Override
	public boolean keyUp(int keyCode)
	{
		switch(keyCode)
		{	
			//Movement
			case Keys.W:
				GameInput.setKey(GameInput.BUTTON_W, false);
					break;
			case Keys.A:
				GameInput.setKey(GameInput.BUTTON_A, false);
					break;
			case Keys.S:
				GameInput.setKey(GameInput.BUTTON_S, false);
					break;
			case Keys.D:
				GameInput.setKey(GameInput.BUTTON_D, false);
					break;
					
			//Actions
			case Keys.SPACE:
				GameInput.setKey(GameInput.BUTTON_SPACE, false);
					break;
			case Keys.Z:
				GameInput.setKey(GameInput.BUTTON_Z,  false);
					break;
			case Keys.X:
				GameInput.setKey(GameInput.BUTTON_X, false);
					break;
			case Keys.C:
				GameInput.setKey(GameInput.BUTTON_C, false);
					break;
			case Keys.R:
				GameInput.setKey(GameInput.BUTTON_R, false);
					break;
			case Keys.F:
				GameInput.setKey(GameInput.BUTTON_F, false);
					break;
			case Keys.NUM_1:
				GameInput.setKey(GameInput.BUTTON_NUM_1, false);
					break;
			case Keys.NUM_2:
				GameInput.setKey(GameInput.BUTTON_NUM_2, false);
					break;
			case Keys.NUM_3:
				GameInput.setKey(GameInput.BUTTON_NUM_3, false);
					break;
			case Keys.NUM_4:
				GameInput.setKey(GameInput.BUTTON_NUM_4, false);
					break;
			case Keys.NUM_5:
				GameInput.setKey(GameInput.BUTTON_NUM_5, false);
					break;
					
			//Rotation
			case Keys.UP:
				GameInput.setKey(GameInput.BUTTON_UP, false);
					break;
			case Keys.DOWN:
				GameInput.setKey(GameInput.BUTTON_DOWN, false);
					break;
			case Keys.RIGHT:
				GameInput.setKey(GameInput.BUTTON_RIGHT, false);
					break;
			case Keys.LEFT:
				GameInput.setKey(GameInput.BUTTON_LEFT, false);
					break;	
					
			//Menu related
			case Keys.ESCAPE:
				GameInput.setKey(GameInput.BUTTON_ESCAPE, false);
					break;
		}
			
		return true;
	}
}
