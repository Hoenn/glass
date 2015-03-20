package me.glassware.handlers;

public class GameInput
{
	public static boolean[] keys;
	public static boolean[] pkeys;
	
	public static final int NUM_KEYS=13;
	
	public static final int BUTTON_SPACE=0;
	public static final int BUTTON_W=1;
	public static final int BUTTON_A=2;
	public static final int BUTTON_S=3;
	public static final int BUTTON_D=4;
	public static final int BUTTON_Z=5;
	public static final int BUTTON_X=6;
	public static final int BUTTON_C=7;
	public static final int BUTTON_UP=8;
	public static final int BUTTON_DOWN=9;
	public static final int BUTTON_LEFT=10;
	public static final int BUTTON_RIGHT=11;
	public static final int BUTTON_ESC=12;
	
	
	static{
		keys=new boolean[NUM_KEYS];
		pkeys=new boolean[NUM_KEYS];
	}
	public static void update()
	{
		for(int i=0; i<NUM_KEYS;i++)
		{
			pkeys[i]=keys[i];
		}
	}
	public static void setKey(int i, boolean b)
	{
		keys[i]=b;
	}
	public static boolean isDown(int i)
	{
		return keys[i];
	}
	public static boolean isPressed(int i)
	{
		return keys[i] && !pkeys[i];
	}
}
