package me.glassware.entities;

import com.badlogic.gdx.math.Vector2;

public abstract class Entity
{
	private Vector2 position;

	public Entity(Vector2 position)
	{
		this.position=position;
	}
	public Vector2 getPosition()
	{
		return this.position;
	}
	public abstract int getHealth();
}
