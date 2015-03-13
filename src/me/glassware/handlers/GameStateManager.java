package me.glassware.handlers;

import java.util.Stack;

import me.glassware.main.Game;
import me.glassware.states.GameState;
import me.glassware.states.Loading;
import me.glassware.states.Menu;
import me.glassware.states.Pause;

public class GameStateManager
{
	private Game game;
	
	private GameState[] gameStates;
	
	public final int NUMSTATES=3;
	public final int LOADING=0;
	public final int MENU = 1;
	public final int PAUSE=2;
	
	private int currentState;
	
	private GameState currentGameState;
	
	public GameStateManager(Game game)
	{
		this.game = game;
		gameStates = new GameState[NUMSTATES];
		currentState=0;
		setState(currentState);
	}
	public Game getGame()
	{
		return game;
	}
	public void update(float dt)
	{
		currentGameState.update(dt);
	}	
	public void render()
	{
		currentGameState.render();
	}
	private GameState getState(int state)
	{
		if(state==LOADING) return new Loading(this);
		if(state==MENU) return new Menu(this);
		if(state==PAUSE) return new Pause(this);
		return null;
	}
	public void setState(int state)
	{
		if(gameStates[state]==null)
		{
			gameStates[state]=getState(state);
			currentGameState=gameStates[state];
		}
		else
		{
			currentGameState=gameStates[state];
		}
	}

	public void disposeState(int state)
	{
		GameState g= gameStates[state];
		gameStates[state]=null;
		g.dispose();
	}
}
