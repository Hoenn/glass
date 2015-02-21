package me.glassware.entities;

import com.badlogic.gdx.math.Vector2;

public class Player extends Entity
{
	private int health;
	
	public Player(Vector2 position,int health)
	{
		super(position);
		this.health=health;
		
	}
	public int getHealth()
	{
		return this.health;
	}
}
