package me.glassware.entities;

import me.glassware.main.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

public class Player extends B2DSprite
{
	private int totalHealth;
	private int currentHealth;
	
	private Array<Item> inventory;
	
	public Player(Body body)
	{
		super(body);
		
		inventory = new Array<Item>();
		
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
	public void addItem(Item i)
	{
		inventory.add(i);
	}
	public void useItemAt(int i)
	{
		if(i<inventory.size)
		{
			inventory.get(i).useItem();
			inventory.removeIndex(i);
		}
	}
	public Array<Item> getInventory()
	{
		return inventory;
	}
}

