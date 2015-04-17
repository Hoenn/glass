package me.glassware.handlers;

import me.glassware.main.Game;
import me.glassware.screens.GameScreen;
import me.glassware.screens.Loading;
import me.glassware.screens.Menu;
import me.glassware.screens.Pause;
import me.glassware.screens.TestLevel;

public class GameScreenManager 
{
	private Game game;
	
	private GameScreen[] gameScreens;
	
	public final int NUMSTATES=4;
	public final int LOADING=0;
	public final int MENU = 1;
	public final int PAUSE=2;
	public final int TESTLEVEL=3;
	
	private int currentScreen;
	
	private GameScreen currentGameScreen;
	
	public GameScreenManager(Game game)
	{
		this.game = game;
		gameScreens = new GameScreen[NUMSTATES];
		
		//LoadingScreen
		setScreen(LOADING);
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
		if(screen==TESTLEVEL) return new TestLevel(this);
		return null;
	}
	public void setScreen(int screen)
	{
		if(gameScreens[screen]==null) //If the desired Screen does not exist
		{
			gameScreens[screen]=getScreen(screen); //Make it

		}
		Game.currentScreen = gameScreens[screen];
		currentGameScreen=gameScreens[screen];//Set current Screen to desired
		currentGameScreen.resume();//Resume Desired
		currentScreen=screen;//Update currentScreen array position
	}
	public void setScreen(int screen, boolean pause)
	{
		if(pause)
			currentGameScreen.pause(); //Pause calling Screen
		else if(!pause)
			disposeScreen(currentScreen);//Dispose calling Screen
		setScreen(screen);
	}

	public void disposeScreen(int screen)
	{
		GameScreen g= gameScreens[screen]; 
		gameScreens[screen]=null;
		g.dispose();
	}
}
