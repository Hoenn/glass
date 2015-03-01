package me.glassware.entities;

import me.glassware.main.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class Player extends B2DSprite
{
	private int totalHealth;
	private int currentHealth;
	
	public Player(Body body)
	{
		super(body);
		
		Texture tex = Game.res.getTexture("player");
		TextureRegion[] sprites = TextureRegion.split(tex, 20, 20)[0];
		setAnimation(sprites, 1/4f);
		
	}
	public void setTotalHealth(int h)
	{
		totalHealth=h;
	}
	public int getHealth()
	{
		return currentHealth;
	}
	public void takeDamage(int dmg)
	{
		currentHealth-=dmg;
		//TODO if dead
	}
}

