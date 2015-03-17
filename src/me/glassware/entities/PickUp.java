package me.glassware.entities;

import me.glassware.main.Game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class PickUp extends Entity
{
	private String name;

	public PickUp(Body body, String name)
	{
		super(body);
		this.name=name;
		
		TextureRegion frames = Game.atlas.findRegion(name);
		setAnimation(frames.split(18,18)[0], 0f);
	}
	public String getName()
	{
		return name;
	}
	public void moveUp(){}
	public void moveLeft(){}
	public void moveDown(){}
	public void moveRight(){}
}
