package me.glassware.handlers;

import java.util.Stack;

import me.glassware.main.Game;
import me.glassware.screens.GameScreen;
import me.glassware.screens.Loading;
import me.glassware.screens.Menu;
import me.glassware.screens.Pause;

public class GameScreenManager 
{
	private Game game;
	
	private GameScreen[] gameScreens;
	
	public final int NUMSTATES=3;
	public final int LOADING=0;
	public final int MENU = 1;
	public final int PAUSE=2;
	
	private int currentScreen;
	
	private GameScreen currentGameScreen;
	
	public GameScreenManager(Game game)
	{
		this.game = game;
		gameScreens = new GameScreen[NUMSTATES];
		
		currentScreen=0; //Set First Game State/Screen, LOADING
		setScreen(currentScreen, false);
	}
	public Game getGame()
	{
		return game; 
	}
	public void update(float dt)
	{
		currentGameScreen.update(dt);
	}	
	public void render()
	{
		currentGameScreen.render();
	}
	private GameScreen getScreen(int screen)
	{
		if(screen==LOADING) return new Loading(this);
		if(screen==MENU) return new Menu(this);
		if(screen==PAUSE) return new Pause(this);
		return null;
	}
	public void setScreen(int screen, boolean pause)
	{
		if(gameScreens[screen]==null) //If the desired Screen does not exist
		{
			gameScreens[screen]=getScreen(screen); //Make it
		}
		else if(!pause)
			disposeScreen(currentScreen); //If the desired Screen does exist and you don't want to pause it

		if(pause)
			currentGameScreen.pause(); //Pause calling Screen

		
		currentGameScreen=gameScreens[screen];//Set current Screen to desired
		currentGameScreen.resume();//Resume Desired
		currentScreen=screen;//Update currentScreen array position
	}
	public void disposeScreen(int screen)
	{
		GameScreen g= gameScreens[screen]; //Instantiate Desired Screen
		gameScreens[screen]=null; //Set to Null for Garbage Collection
		g.dispose(); //Proper dispose
	}
}
