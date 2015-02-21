package me.glassware.handlers;

import java.util.Stack;

import me.glassware.main.Game;
import me.glassware.states.GameState;
import me.glassware.states.Menu;

public class GameStateManager
{
	private Game game;
	
	private Stack<GameState> gameStates;
	
	public final int MENU = 1;
	
	public GameStateManager(Game game)
	{
		this.game = game;
		gameStates = new Stack<GameState>();
		pushState(MENU);
	}
	public Game getGame()
	{
		return game;
	}
	public void update(float dt)
	{
		gameStates.peek().update(dt);
	}
	
	public void render()
	{
		gameStates.peek().render();
	}
	private GameState getState(int state)
	{
		if(state==MENU) return new Menu(this);
		return null;
	}
	public void setState(int state)
	{
		popState();
		pushState(state);
	}
	public void pushState(int state)
	{
		gameStates.push(getState(state));
	}
	public void popState()
	{
		GameState g= gameStates.pop();
		g.dispose();
	}
}
